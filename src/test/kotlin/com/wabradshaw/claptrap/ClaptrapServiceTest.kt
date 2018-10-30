package com.wabradshaw.claptrap

import com.wabradshaw.claptrap.design.JokeDesigner
import com.wabradshaw.claptrap.repositories.datamuse.DataMuseRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataMuseRepositoryTest {

    @Test
    fun tellJoke(){
        val repo = DataMuseRepository()
        val jokeDesigner = JokeDesigner(repo, repo, repo)

        var result = ClaptrapService(jokeDesigner).tellJoke("catapult")

        println(result.spec)
        println()
        println(result.setup)
        println(result.punchline)
    }
}