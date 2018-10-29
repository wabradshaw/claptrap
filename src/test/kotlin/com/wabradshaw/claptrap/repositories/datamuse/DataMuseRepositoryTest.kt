package com.wabradshaw.claptrap.repositories.datamuse

import com.wabradshaw.claptrap.repositories.datamuse.DataMuseRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataMuseRepositoryTest {

    val repo: DataMuseRepository = DataMuseRepository()

    @Test
    fun testGetWord_known(){
        var result = repo.getWord("that");
        println(result);
    }
}