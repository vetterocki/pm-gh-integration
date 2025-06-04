package pm.gh.integration.infrastructure.mongo

import io.github.serpro69.kfaker.Faker
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import pm.gh.integration.application.service.ProjectBoardService
import pm.gh.integration.application.service.ProjectLabelService
import pm.gh.integration.application.service.ProjectService
import pm.gh.integration.application.service.TeamMemberService
import pm.gh.integration.application.service.TeamService
import pm.gh.integration.application.service.TicketService
import pm.gh.integration.application.service.TicketStatusService
import pm.gh.integration.domain.Actor
import pm.gh.integration.domain.PullRequest
import pm.gh.integration.domain.PullRequestStatus
import pm.gh.integration.domain.WorkflowRun
import pm.gh.integration.infrastructure.mongo.model.Project
import pm.gh.integration.infrastructure.mongo.model.ProjectBoard
import pm.gh.integration.infrastructure.mongo.model.ProjectLabel
import pm.gh.integration.infrastructure.mongo.model.Team
import pm.gh.integration.infrastructure.mongo.model.TeamMember
import pm.gh.integration.infrastructure.mongo.model.TeamMember.Role
import pm.gh.integration.infrastructure.mongo.model.Ticket
import pm.gh.integration.infrastructure.mongo.model.TicketStatus
import java.time.Instant
import java.util.UUID

@Configuration
class DataInserter(
    private val teamMemberService: TeamMemberService,
    private val teamService: TeamService,
    private val projectService: ProjectService,
    private val projectLabelService: ProjectLabelService,
    private val ticketService: TicketService,
    private val ticketStatusService: TicketStatusService,
    private val projectBoardService: ProjectBoardService,
    private val mongoTemplate: ReactiveMongoTemplate,
    private val passwordEncoder: PasswordEncoder
) {

    private val faker = Faker()
    private val logger = LoggerFactory.getLogger(DataInserter::class.java)

    @Bean
    fun commandLineRunner(): CommandLineRunner {
        return CommandLineRunner {
            logger.info("Starting DataInserter CommandLineRunner...")

            listOf(
                Team.COLLECTION_NAME,
                TeamMember.COLLECTION_NAME,
                Project.COLLECTION_NAME,
                ProjectLabel.COLLECTION_NAME,
                ProjectBoard.COLLECTION_NAME,
                Ticket.COLLECTION_NAME,
                TicketStatus.COLLECTION_NAME
            ).forEach { collection ->
                mongoTemplate.dropCollection(collection).block()
                logger.info("Cleared collection: $collection")
            }

            val statuses = listOf(
                "TO DO", "DONE", "IN PROGRESS", "WAITING FOR MERGE", "STOP PROGRESS", "WAITING FOR REVIEW"
            ).map { statusName ->
                ticketStatusService.create(TicketStatus(id = null, name = statusName)).block()!!
            }


            val colors = listOf(
                "#FF6347",  // Tomato
                "#1E90FF",  // DodgerBlue
                "#32CD32",  // LimeGreen
                "#FFD700",  // Gold
                "#2F4F4F",  // DarkSlateGray
                "#FF69B4",  // HotPink
                "#8A2BE2",  // BlueViolet
                "#00CED1",  // DarkTurquoise
                "#FFA07A",  // LightSalmon
                "#20B2AA",  // LightSeaGreen
                "#7FFF00",  // Chartreuse
                "#DC143C",  // Crimson
                "#FF8C00",  // DarkOrange
                "#BA55D3",  // MediumOrchid
                "#5F9EA0",  // CadetBlue
                "#ADFF2F",  // GreenYellow
                "#00FA9A",  // MediumSpringGreen
                "#00BFFF",  // DeepSkyBlue
                "#D2691E",  // Chocolate
                "#9932CC"   // DarkOrchid
            )

            var labels = (1..15).map {
                val labelName =
                    faker.programmingLanguage.name() + listOf("Bug", "Release", "Blocker", "Feature").random()
                mongoTemplate.insert<ProjectLabel>(
                    ProjectLabel(
                        id = null,
                        name = labelName,
                        color = colors.random(),
                        description = LABEL_DESCRIPTION,
                        projectId = null
                    ),
                ).block()!!
            }

            repeat(10) { teamIndex ->
                val projectManager = mongoTemplate.insert<TeamMember>(fakeTeamMember(null, Role.MANAGER)).block()!!

                val teamName = "Team ${faker.color.name()} ${UUID.randomUUID().toString().take(4)}"
                val team = teamService.create(
                    Team(
                        id = null,
                        name = teamName,
                        projectManager = projectManager,
                        teamMemberIds = null,
                        projectIds = null
                    ), projectManagerName = projectManager.fullName
                ).block()!!

                if (projectManager.teamId == null) {
                    mongoTemplate.save(projectManager.copy(teamId = team.id)).block()
                }

                val teamMembers = (1..8).map {
                    teamMemberService.create(fakeTeamMember(team.id, Role.DEFAULT), team.id.toString()).block()!!
                }

                var updatedTeam = mongoTemplate.save(
                    team.copy(teamMemberIds = teamMembers.mapNotNull { it.id })
                ).block()!!

                val projectName = "Project ${faker.space.galaxy()} ${UUID.randomUUID().toString().take(4)}"
                val projectKey = "PRJ${(1000 + teamIndex)}"
                val project = projectService.create(
                    Project(
                    id = null,
                    fullName = projectName,
                    key = projectKey,
                    projectBoardIds = listOf(),
                    team = updatedTeam,
                    projectOwner = projectManager,
                    projectLabelIds = labels.map { it.id!! }),
                    teamName = updatedTeam.name,
                    projectOwnerName = projectManager.fullName).block()!!

                updatedTeam = mongoTemplate.save(
                    updatedTeam.copy(projectIds = (updatedTeam.projectIds ?: emptyList()) + project.id!!)
                ).block()!!

                val projectBoards = (1..5).map { projectBoardIndex ->
                    projectBoardService.create(
                        ProjectBoard(
                            id = null,
                            name = "${project.fullName} Board #${projectBoardIndex}",
                            projectId = project.id,
                            ticketIds = listOf()
                        )
                    ).block()!!
                }.toList()


                val updatedProject = mongoTemplate.save(
                    project.copy(projectBoardIds = projectBoards.map { it.id!! })
                ).block()!!

                labels = labels.map { it.copy(projectId = updatedProject.id!!) }
                    .map { mongoTemplate.save<ProjectLabel>(it).block()!! }


                var tickets = (1..100).map { ticketIndex ->
                    val summary = "Fix ${faker.hacker.verb()} ${faker.hacker.noun()}"
                    ticketService.create(
                        Ticket(
                            id = null,
                            ticketIdentifier = "${updatedProject.key}-${ticketIndex}",
                            projectId = updatedProject.id,
                            summary = summary,
                            description = DESCRIPTION,
                            reporter = projectManager,
                            assignee = teamMembers.random(),
                            reviewerIds = listOf(projectManager.id!!),
                            linkedTicketIds = listOf(),
                            linkedPullRequests = listOf(),
                            linkedWorkflowRuns = listOf(),
                            priority = Ticket.TicketPriority.entries.random(),
                            status = statuses.random(),
                            githubDescription = GITHUB_DESCRIPTION,
                            projectBoardId = projectBoards.random().id,
                            createdAt = Instant.now(),
                            labels = listOf(labels.random(), labels.random()),
                        ),
                        projectId = updatedProject.id.toString(),
                        projectBoardId = projectBoards.random().id.toString(),
                        reporterId = teamMembers.random().id.toString(),
                        labelsIds = listOf(labels.random(), labels.random()).map { it.id.toString() },
                    ).block()!!
                }


                tickets = tickets.map { ticket ->

                    val pullRequests = List(2) {
                        PullRequest(
                            htmlUrl = "https://github.com/example/repo/pull/${it + 1}",
                            title = "Fix bug ${it + 1}",
                            repositoryName = "example-repo",
                            ticketIdentifier = ticket.ticketIdentifier.orEmpty(),
                            actor = Actor(
                                email = teamMembers.random().email,
                                login = teamMembers.random().loginInGithub,
                                name = teamMembers.random().fullName
                            ),
                            pullRequestStatus = PullRequestStatus(
                                branchRef = faker.internet.iPv4Address(),
                                status = PullRequestStatus.Status.entries.toTypedArray().random()
                            ),
                        )
                    }

                    val workflowRuns = List(2) {
                        WorkflowRun(
                            htmlUrl = "https://github.com/example/repo/actions/runs/${1000 + it}",
                            ticketIdentifier = ticket.ticketIdentifier.orEmpty(),
                            conclusion = listOf("success", "failure", "neutral").random(),
                            repositoryName = "example-repo",
                            actor = Actor(
                                email = teamMembers.random().email,
                                login = teamMembers.random().loginInGithub,
                                name = teamMembers.random().fullName
                            ),
                        )
                    }


                    ticket.copy(
                        linkedTicketIds = listOf(tickets.random().id!!),
                        linkedPullRequests = pullRequests,
                        linkedWorkflowRuns = workflowRuns,
                    )
                }.map { mongoTemplate.save<Ticket>(it).block()!! }



                mongoTemplate.save(
                    projectBoards.random().copy(ticketIds = tickets.mapNotNull { it.id })
                ).block()

                logger.info("Inserted team #${teamIndex + 1}: ${team.name}")
            }

            logger.info("Fake data insertion completed.")
        }
    }

    private fun fakeTeamMember(teamId: ObjectId?, role: Role): TeamMember {
        val firstName = faker.name.firstName()
        val lastName = faker.name.lastName()
        return TeamMember(
            id = null,
            firstName = firstName,
            lastName = lastName,
            fullName = "$firstName $lastName",
            email = "${firstName.lowercase()}.${lastName.lowercase()}@example.com",
            teamId = teamId,
            loginInGithub = faker.control.objectOfPower(),
            position = faker.job.position(),
            avatarUrl = "https://i.pravatar.cc/150?u=${UUID.randomUUID()}",
            password = passwordEncoder.encode("1"),
            role = role
        )
    }

    companion object {
        const val DESCRIPTION =
            "\n" + "What is Lorem Ipsum?\n" + "\n" + "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n"

        const val GITHUB_DESCRIPTION =
            "\n" + "Why do we use it?\n" + "\n" + "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like).\n"

        const val LABEL_DESCRIPTION = "Where can I get some?\n" +
                "There are many variations of passages of Lorem Ipsum available, but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem Ipsum which looks reasonable. The generated Lorem Ipsum is therefore always free from repetition, injected humour, or non-characteristic words etc."
    }
}
