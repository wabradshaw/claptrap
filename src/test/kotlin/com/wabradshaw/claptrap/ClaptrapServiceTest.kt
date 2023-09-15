package com.wabradshaw.claptrap

import com.wabradshaw.claptrap.design.JokeDesigner
import com.wabradshaw.claptrap.repositories.custom.JsonRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClapTrapServiceTest {

    @Test
    fun tellRandomJoke(){
        val mainRepository = JsonRepository()
        val nucleusRepository = JsonRepository(object{}.javaClass.getResourceAsStream("/nucleusDictionary.json"))

        val jokeDesigner = JokeDesigner(dictionaryRepo = mainRepository,
                primarySemanticRepo = nucleusRepository,
                secondarySemanticRepo = mainRepository,
                linguisticRepo = mainRepository,
                nucleusRepository = nucleusRepository)

        var result = ClaptrapService(jokeDesigner).tellJoke()

        println(result.spec)
        println()
        println(result.setup)
        println(result.punchline)
    }

    @Test
    fun tellSpecificJoke(){
        val mainRepository = JsonRepository()
        val nucleusRepository = JsonRepository(object{}.javaClass.getResourceAsStream("/nucleusDictionary.json"))

        val jokeDesigner = JokeDesigner(dictionaryRepo = mainRepository,
                primarySemanticRepo = nucleusRepository,
                secondarySemanticRepo = mainRepository,
                linguisticRepo = mainRepository,
                nucleusRepository = nucleusRepository)

        var result = ClaptrapService(jokeDesigner).tellJoke("catapult")

        println(result.spec)
        println()
        println(result.setup)
        println(result.punchline)
    }

    @Test
    fun tellJokes(){
        val mainRepository = JsonRepository()
        val nucleusRepository = JsonRepository(object{}.javaClass.getResourceAsStream("/nucleusDictionary.json"))

        val jokeDesigner = JokeDesigner(dictionaryRepo = mainRepository,
                                        primarySemanticRepo = nucleusRepository,
                                        secondarySemanticRepo = mainRepository,
                                        linguisticRepo = mainRepository,
                                        nucleusRepository = nucleusRepository)

        val topics = listOf("catapult", "pirate", "captain", "slowboat", "winter", "summer", "spring", "damage", "hammersmith")

        for(topic in topics) {
            var result = ClaptrapService(jokeDesigner).tellJoke(topic)

            println()
            println(result.setup)
            println(result.punchline)
        }
    }
}