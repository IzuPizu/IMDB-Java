import CheckerUtils.Companion.checkEquals
import CheckerUtils.Companion.loadActors
import CheckerUtils.Companion.loadProductions
import CheckerUtils.Companion.loadRequests
import CheckerUtils.Companion.loadUsers
import java.time.format.DateTimeFormatterBuilder
/*
 *   FIXME: Pentru a functiona checker-ul aceste clase trebuie sa existe si sa aiba functiile si membrii necesari
 */
import org.example.Actor
import org.example.IMDB
import org.example.Request
import org.example.Movie
import org.example.Production
import org.example.Series
import org.example.Admin
import org.example.Contributor
import org.example.Regular
import org.example.User

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.lang.reflect.Modifier

class JsonDataLoadChecker {

    @Test
    fun loadTest() {
        IMDB.getInstance().run()
        val imdb = IMDB.getInstance()
        val fields = imdb.javaClass.declaredFields
        for (field in fields) {
            if (!Modifier.isPrivate(field.modifiers)) {
                throw AssertionError("All fields in IMDB should be private")
            } else {
                field.isAccessible = true
            }
            when (field.name) {
                "users" -> {
                    Assertions.assertEquals(
                        field.type,
                        MutableList::class.java,
                        "users field should be of type List<User>"
                    )
                    when (val fieldValue = field.get(imdb)) {
                        null -> throw AssertionError("users field should not be null")
                        is List<*> -> {
                            @Suppress("unchecked_cast")
                            val users = fieldValue as List<User<*>>
                            val testUsers = loadUsers()
                            Assertions.assertEquals(users.size, testUsers.size)
                            users.zip(testUsers).forEach { (user, testUser): Pair<User<*>, User<*>> ->
                                when (user) {
                                    is Regular<*> -> Assertions.assertTrue(
                                        checkEquals(user.javaClass, user, testUser),
                                        "Regular $user is not equal to $testUser"
                                    )
                                    is Contributor<*> -> Assertions.assertTrue(
                                        checkEquals(user.javaClass, user, testUser),
                                        "Contributor $user is not equal to $testUser"
                                    )
                                    is Admin<*> -> Assertions.assertTrue(
                                        checkEquals(user.javaClass, user, testUser),
                                        "Admin $user is not equal to $testUser"
                                    )
                                }
                            }
                        }
                    }
                }

                "actors" -> {
                    Assertions.assertEquals(
                        field.type,
                        MutableList::class.java,
                        "actors field should be of type List<Actor>"
                    )
                    when (val fieldValue = field.get(imdb)) {
                        null -> throw AssertionError("actors field should not be null")
                        is List<*> -> {
                            @Suppress("unchecked_cast")
                            val actors = fieldValue as List<Actor>
                            val testActors = loadActors()
                            Assertions.assertEquals(actors.size, testActors.size)
                            actors.zip(testActors).forEach { (actor, testActor): Pair<Actor, Actor> ->
                                Assertions.assertTrue(
                                    checkEquals(actor.javaClass, actor, testActor),
                                    "Actor $actor is not equal to $testActor"
                                )
                            }

                            //test VERS
//                            actors.zip(testActors).forEachIndexed { index, (actor, testActor) ->
//                                val areActorsEqual = checkEquals(actor.javaClass, actor, testActor)
//                                println("Comparison result for actor at index $index: $areActorsEqual")
//                                println("Actor performances: ${actor.performances.joinToString(", ")}")
//                                println("Test actor performances: ${testActor.performances.joinToString(", ")}")
//
//                                // Convert performances to sets for unordered equality check
//                                val actorPerformancesSet = actor.performances.toSet()
//                                val testActorPerformancesSet = testActor.performances.toSet()
//
//                                Assertions.assertTrue(
//                                    actorPerformancesSet == testActorPerformancesSet,
//                                    "Actor $actor is not equal to $testActor"
//                                )
//                            }


                        }
                    }
                }

                //ORIGINAL VERS

                "productions" -> {
                    Assertions.assertEquals(
                        field.type,
                        MutableList::class.java,
                        "productions field should be of type List<Production>"
                    )
                    when (val fieldValue = field.get(imdb)) {
                        null -> throw AssertionError("productions field should not be null")
                        is List<*> -> {
                            @Suppress("unchecked_cast")
                            val productions = fieldValue as List<Production>
                            val testProductions = loadProductions()
                            Assertions.assertEquals(productions.size, testProductions.size)
                            productions.zip(testProductions).forEach { (production, testProduction): Pair<Production, Production> ->
                                when (production) {
                                    is Movie -> Assertions.assertTrue(
                                        checkEquals(
                                            production.javaClass,
                                            production,
                                            testProduction
                                        ), "Movie $production is not equal to $testProduction"
                                    )
                                    is Series -> Assertions.assertTrue(
                                        checkEquals(
                                            production.javaClass,
                                            production,
                                            testProduction
                                        ), "Series $production is not equal to $testProduction"
                                    )
                                } }

                        }
                    }
                }

                //test vers
//                "productions" -> {
//                    Assertions.assertEquals(
//                        field.type,
//                        MutableList::class.java,
//                        "productions field should be of type List<Production>"
//                    )
//                    when (val fieldValue = field.get(imdb)) {
//                        null -> throw AssertionError("productions field should not be null")
//                        is List<*> -> {
//                            @Suppress("unchecked_cast")
//                            val productions = fieldValue as List<Production>
//                            val testProductions = loadProductions()
//                            Assertions.assertEquals(productions.size, testProductions.size)
//
//                            productions.zip(testProductions).forEach { (production, testProduction): Pair<Production, Production> ->
//                                when (production) {
//                                    is Movie -> {
//                                        println("Comparing movies:")
//                                        println(production) // Add any other necessary prints
//                                        println(testProduction) // Add any other necessary prints
//                                        Assertions.assertTrue(
//                                            checkEquals(
//                                                production.javaClass,
//                                                production,
//                                                testProduction
//                                            ), "Movie $production is not equal to $testProduction"
//                                        )
//                                    }
//                                    is Series -> {
//                                        println("Comparing series:")
//                                        println(production) // Add any other necessary prints
//                                        println(testProduction) // Add any other necessary prints
//                                        Assertions.assertTrue(
//                                            checkEquals(
//                                                production.javaClass,
//                                                production,
//                                                testProduction
//                                            ), "Series $production is not equal to $testProduction"
//                                        )
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }


//                "productions" -> {
//                    Assertions.assertEquals(
//                        field.type,
//                        MutableList::class.java,
//                        "productions field should be of type List<Production>"
//                    )
//                    when (val fieldValue = field.get(imdb)) {
//                        null -> throw AssertionError("productions field should not be null")
//                        is List<*> -> {
//                            @Suppress("unchecked_cast")
//                            val productions = fieldValue as List<Production>
//                            val testProductions = loadProductions()
//                            Assertions.assertEquals(productions.size, testProductions.size)
//                            productions.zip(testProductions).forEachIndexed { index, (production, testProduction) ->
//                                println("Checking production at index $index:")
//                                println("Production:")
//                                println("  Title: ${production.title}")
//                                println("  Type: ${production.type}")
//                                println("  Directors: ${production.directors}")
//                                println("  Actors: ${production.actors}")
//                                println("  Genres: ${production.genres}")
//                                println("  Ratings:")
//                                production.ratings.forEachIndexed { ratingIndex, rating ->
//                                    println("    Rating $ratingIndex:")
//                                    println("      Username: ${rating.username}")
//                                    println("      Rating: ${rating.rating}")
//                                    println("      Comment: ${rating.comment}")
//                                }
//                                println("  Plot: ${production.plot}")
//                                println("  Average Rating: ${production.averageRating}")
//                                if (production is Movie) {
//                                    println("  Duration: ${production.duration}")
//                                    println("  Release Year: ${production.releaseYear}")
//                                }
//                                if (production is Series) {
//                                    println("  Release Year: ${production.releaseYear}")
//                                    println("  NumSeasons: ${production.numSeasons}")
//                                    println("  NumSeasons: ${production.seasons}")
//                                }
//
//
//                                println("Test Production:")
//                                println("  Title: ${testProduction.title}")
//                                println("  Type: ${testProduction.type}")
//                                println("  Directors: ${testProduction.directors}")
//                                println("  Actors: ${testProduction.actors}")
//                                println("  Genres: ${testProduction.genres}")
//                                println("  Ratings:")
//                                testProduction.ratings.forEachIndexed { ratingIndex, rating ->
//                                    println("    Rating $ratingIndex:")
//                                    println("      Username: ${rating.username}")
//                                    println("      Rating: ${rating.rating}")
//                                    println("      Comment: ${rating.comment}")
//                                }
//                                println("  Plot: ${testProduction.plot}")
//                                println("  Average Rating: ${testProduction.averageRating}")
//                                if (testProduction is Movie) {
//                                    println("  Duration: ${testProduction.duration}")
//                                    println("  Release Year: ${testProduction.releaseYear}")
//                                }
//                                if (testProduction is Series) {
//                                    println("  Release Year: ${testProduction.releaseYear}")
//                                    println("  NumSeasons: ${testProduction.numSeasons}")
//                                    println("  NumSeasons: ${testProduction.seasons}")
//                                }
//
//
//                                when (production) {
//                                    is Movie -> {
//                                        val areEqual = checkEquals(production.javaClass, production, testProduction)
//                                        println("Are Equal: $areEqual")
//                                        Assertions.assertTrue(areEqual, "Movie $production is not equal to $testProduction")
//                                    }
//                                    is Series -> {
//                                        val areEqual = checkEquals(production.javaClass, production, testProduction)
//                                        println("Are Equal: $areEqual")
//                                        Assertions.assertTrue(areEqual, "Series $production is not equal to $testProduction")
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }



                "requests" -> {
                    Assertions.assertEquals(
                        field.type,
                        MutableList::class.java,
                        "requests field should be of type List<Request>"
                    )
                    when (val fieldValue = field.get(imdb)) {
                        null -> throw AssertionError("requests field should not be null")
                        is List<*> -> {
                            @Suppress("unchecked_cast")
                            val requests = fieldValue as List<Request>
                            val testRequests = loadRequests()
                            Assertions.assertEquals(requests.size, testRequests.size)
                            requests.zip(testRequests).forEach { (request, testRequest): Pair<Request, Request> ->
                                Assertions.assertTrue(
                                    checkEquals(request.javaClass, request, testRequest),
                                    "Request $request is not equal to $testRequest"
                                )
                            }

                            //TEST VERS

//                            for ((index, pair) in requests.zip(testRequests).withIndex()) {
//                                val (request, testRequest) = pair
//                                println("Comparing requests at index $index:")
//                                println("Test Request type: ${testRequest.displayInfo()}")
//                                println("Request type: ${request.displayInfo()}")
//
//                                Assertions.assertTrue(
//                                    checkEquals(request.javaClass, request, testRequest),
//                                    "Request $request is not equal to $testRequest"
//                                )
//                            }
                        }
                    }

                }
            }
        }
    }
}