package com.wabradshaw.claptrap.design

import com.wabradshaw.claptrap.repositories.custom.JsonRepository
import com.wabradshaw.claptrap.repositories.datamuse.DataMuseRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JokeDesignerTest {

    val mainRepository = JsonRepository()
    val nucleusRepository = JsonRepository(object{}.javaClass.getResourceAsStream("/nucleusDictionary.json"))
    val designer = JokeDesigner(dictionaryRepo = mainRepository,
                                primarySemanticRepo = nucleusRepository,
                                secondarySemanticRepo = mainRepository,
                                linguisticRepo = mainRepository,
                                nucleusRepository = nucleusRepository);

    @Test
    fun tellJoke(){
        var result = designer.designJokeFromNucleus("battleaxe")

        println(result)
    }

}