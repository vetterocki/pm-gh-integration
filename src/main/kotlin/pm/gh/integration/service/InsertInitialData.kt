//package pm.gh.integration.service
//
//import org.springframework.boot.CommandLineRunner
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.data.mongodb.core.ReactiveMongoTemplate
//import pm.gh.integration.model.Project
//import pm.gh.integration.model.ProjectBoard
//import pm.gh.integration.model.ProjectLabel
//import pm.gh.integration.model.Team
//import pm.gh.integration.model.TeamMember
//import pm.gh.integration.model.Ticket
//import pm.gh.integration.model.TicketStatus
//import reactor.core.publisher.Flux
//import io.github.serpro69.kfaker.Faker as KotlinFaker
//
//@Configuration
//class InsertInitialData(private val reactiveMongoTemplate: ReactiveMongoTemplate) {
//
//    private val faker = KotlinFaker()
//
//    @Bean
//    fun initData(): CommandLineRunner = CommandLineRunner {
//        // Drop collections before inserting new data
//        val collectionsToDrop = listOf(
//            Project.COLLECTION_NAME,
//            ProjectBoard.COLLECTION_NAME,
//            ProjectLabel.COLLECTION_NAME,
//            Team.COLLECTION_NAME,
//            TeamMember.COLLECTION_NAME,
//            Ticket.COLLECTION_NAME,
//            TicketStatus.COLLECTION_NAME
//        )
//
//        Flux.fromIterable(collectionsToDrop)
//            .flatMap { reactiveMongoTemplate.dropCollection(it) }
//            .subscribe()
//
//        val qaTeam = Team(
//            id = null,
//            name = "QA",
//            projectManager = null,
//            projects = null,
//            teamMembers = null
//        ).let { reactiveMongoTemplate.insert(it).block()!! }
//
//        val devopsTeam = Team(
//            id = null,
//            name = "DEVOPS",
//            projectManager = null,
//            projects = null,
//            teamMembers = null
//        ).let { reactiveMongoTemplate.insert(it).block()!! }
//
//
//        val qaMembers = List(4) {
//            TeamMember(
//                id = null,
//                firstName = faker.name.firstName(),
//                lastName = faker.name.lastName(),
//                email = faker.internet.safeEmail(),
//                team = qaTeam,
//                position = "QA"
//            )
//        }.let { reactiveMongoTemplate.insertAll(it).collectList().block()!! }
//
//        val devopsMembers = List(4) {
//            TeamMember(
//                id = null,
//                firstName = faker.name.firstName(),
//                lastName = faker.name.lastName(),
//                email = faker.internet.safeEmail(),
//                team = devopsTeam,
//                position = "DEVOPS"
//            )
//        }.let { reactiveMongoTemplate.insertAll(it).collectList().block()!! }
//
//        val qaProjectManager = TeamMember(
//            id = null,
//            firstName = faker.name.firstName(),
//            lastName = faker.name.lastName(),
//            email = faker.internet.safeEmail(),
//            team = qaTeam,
//            position = "Project Manager QA"
//        ).let { reactiveMongoTemplate.insert(it).block()!! }
//
//        val devopsProjectManager = TeamMember(
//            id = null,
//            firstName = faker.name.firstName(),
//            lastName = faker.name.lastName(),
//            email = faker.internet.safeEmail(),
//            team = qaTeam,
//            position = "Project Manager DEVOPS"
//        ).let { reactiveMongoTemplate.insert(it).block()!! }
//
//        val devopsProject = Project(
//            id = null,
//            name = "DEVOPS",
//            projectBoards = null,
//            team = devopsTeam,
//            projectOwner = devopsProjectManager,
//            projectLabels = null
//        ).let { reactiveMongoTemplate.save(it).block()!! }
//
//        val qaProject = Project(
//            id = null,
//            name = "QA",
//            projectBoards = null,
//            team = qaTeam,
//            projectOwner = qaProjectManager,
//            projectLabels = null
//        ).let { reactiveMongoTemplate.save(it).block()!! }
//
//        qaTeam.copy(teamMembers = qaMembers, projectManager = qaProjectManager, projects = listOf(qaProject))
//            .let { reactiveMongoTemplate.save(it).block()!! }
//        devopsTeam.copy(
//            teamMembers = devopsMembers,
//            projectManager = devopsProjectManager,
//            projects = listOf(devopsProject)
//        )
//            .let { reactiveMongoTemplate.save(it).block()!! }
//
//        val devopsProjectLabels = listOf(
//            ProjectLabel(
//                id = null,
//                name = "INFRA"
//            ),
//            ProjectLabel(
//                id = null,
//                name = "CORE"
//            ),
//        ).let { reactiveMongoTemplate.insertAll(it).collectList() }
//            .map { labels -> devopsProject.copy(projectLabels = labels).let { reactiveMongoTemplate.save(it) } }
//            .block()!!
//
//        val qaProjectLabels = listOf(
//            ProjectLabel(
//                id = null,
//                name = "INTEGRATION"
//            ),
//            ProjectLabel(
//                id = null,
//                name = "UNIT"
//            ),
//        ).let { reactiveMongoTemplate.insertAll(it).collectList() }
//            .map { labels -> devopsProject.copy(projectLabels = labels).let { reactiveMongoTemplate.save(it) } }
//            .block()!!
//
//
//    }
//}
