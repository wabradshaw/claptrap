package com.wabradshaw.claptrap

import com.wabradshaw.claptrap.design.JokeDesigner
import com.wabradshaw.claptrap.repositories.custom.JsonRepository
import com.wabradshaw.claptrap.repositories.datamuse.DataMuseRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClapTrapServiceTest {

    @Test
    fun tellJoke(){
        val dataMuseRepo = DataMuseRepository()
        val customRepo = JsonRepository(this.javaClass.getResourceAsStream("/customDictionary.json"))
        val jokeDesigner = JokeDesigner(customRepo, customRepo, customRepo, customRepo)

        var result = ClaptrapService(jokeDesigner).tellJoke("catapult")

        println(result.spec)
        println()
        println(result.setup)
        println(result.punchline)
    }
}