package pm.gh.integration.infrastructure.mongo

import io.github.serpro69.kfaker.Faker
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import pm.gh.integration.application.service.*
import pm.gh.integration.infrastructure.mongo.model.*
import java.time.Instant
import java.util.*

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
                "TO DO",
                "DONE",
                "IN PROGRESS",
                "WAITING FOR MERGE",
                "STOP PROGRESS",
                "WAITING FOR REVIEW"
            ).map { statusName ->
                ticketStatusService.create(TicketStatus(id = null, name = statusName)).block()!!
            }


            val labels = (1..30).map {
                val labelName = faker.programmingLanguage.name() + listOf("Bug", "Release", "Blocker", "Feature").random()
                projectLabelService.create(ProjectLabel(id = null, name = labelName)).block()!!
            }

            repeat(10) { teamIndex ->
                val projectManager = teamMemberService.create(fakeTeamMember(null)).block()!!

                val teamName = "Team ${faker.color.name()} ${UUID.randomUUID().toString().take(4)}"
                val team = teamService.create(
                    Team(
                        id = null,
                        name = teamName,
                        projectManager = projectManager,
                        teamMemberIds = null,
                        projectIds = null
                    ),
                    projectManagerName = projectManager.fullName
                ).block()!!

                if (projectManager.teamId == null) {
                    mongoTemplate.save(projectManager.copy(teamId = team.id)).block()
                }

                val teamMembers = (1..8).map {
                    teamMemberService.create(fakeTeamMember(team.id)).block()!!
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
                        projectLabelIds = labels.map { it.id!! }
                    ),
                    teamName = updatedTeam.name,
                    projectOwnerName = projectManager.fullName
                ).block()!!

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

                val tickets = (1..100).map { ticketIndex ->
                    val summary = "Fix ${faker.hacker.verb()} ${faker.hacker.noun()}"
                    ticketService.create(
                        Ticket(
                            id = null,
                            ticketIdentifier = "${updatedProject.key}-${ticketIndex}",
                            projectId = updatedProject.id,
                            summary = summary,
                            description = faker.lorem.words(),
                            reporter = projectManager,
                            assignee = teamMembers.random(),
                            reviewerIds = listOf(projectManager.id!!),
                            linkedTicketIds = listOf(),
                            linkedPullRequests = listOf(),
                            linkedWorkflowRuns = listOf(),
                            priority = Ticket.TicketPriority.entries.random(),
                            status = statuses.random(),
                            githubDescription = faker.lorem.words(),
                            projectBoardId = projectBoards.random().id,
                            createdAt = Instant.now(),
                            labels = listOf(labels.random(), labels.random())
                        ),
                        projectId = updatedProject.id.toString(),
                        reporterId = projectManager.id.toString(),
                        assigneeId = teamMembers.random().id.toString()
                    ).block()!!
                }

                mongoTemplate.save(
                    projectBoards.random().copy(ticketIds = tickets.mapNotNull { it.id })
                ).block()

                logger.info("Inserted team #${teamIndex + 1}: ${team.name}")
            }

            logger.info("Fake data insertion completed.")
        }
    }

    private fun fakeTeamMember(teamId: ObjectId?): TeamMember {
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
            avatarUrl = "https://i.pravatar.cc/150?u=${UUID.randomUUID()}"
        )
    }
}
