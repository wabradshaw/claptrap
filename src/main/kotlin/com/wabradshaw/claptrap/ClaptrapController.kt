package com.wabradshaw.claptrap

import com.wabradshaw.claptrap.design.JokeDesigner
import com.wabradshaw.claptrap.repositories.custom.JsonRepository
import com.wabradshaw.claptrap.structure.Joke
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/*
 * A RESTful controller for the Claptrap system
 */
@RestController
class ClaptrapController {

    private val mainRepository = JsonRepository()
    private val nucleusRepository = JsonRepository(object{}.javaClass.getResourceAsStream("/nucleusDictionary.json"))

    val jokeDesigner = JokeDesigner(dictionaryRepo = mainRepository,
            primarySemanticRepo = nucleusRepository,
            secondarySemanticRepo = mainRepository,
            linguisticRepo = mainRepository,
            nucleusRepository = nucleusRepository)
    val jokeService: ClaptrapService = ClaptrapService(jokeDesigner)

    @GetMapping("/joke")
    fun getJoke(): ResponseEntity<Joke> {
        return ResponseEntity.ok(jokeService.tellJoke())
    }
}