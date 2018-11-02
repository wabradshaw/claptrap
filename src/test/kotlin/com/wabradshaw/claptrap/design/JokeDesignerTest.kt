package com.wabradshaw.claptrap.design

import com.wabradshaw.claptrap.repositories.datamuse.DataMuseRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataMuseRepositoryTest {

    val repo = DataMuseRepository()
    val designer = JokeDesigner(repo, repo, repo, repo);

    @Test
    fun tellJoke(){
        var result = designer.designJokeFromNucleus("battleaxe")

        println(result)
    }

}