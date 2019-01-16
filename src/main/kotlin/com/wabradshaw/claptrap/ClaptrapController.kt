package com.wabradshaw.claptrap

import com.wabradshaw.claptrap.design.JokeDesigner
import com.wabradshaw.claptrap.logging.Vote
import com.wabradshaw.claptrap.repositories.custom.JsonRepository
import com.wabradshaw.claptrap.structure.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/*
 * A RESTful controller for the Claptrap system
 */
@RestController
class ClaptrapController {

    private val adultMainRepo = JsonRepository()
    private val adultNucleusRepo = JsonRepository(object{}.javaClass.getResourceAsStream("/nucleusDictionary.json"))

    val adultJokeDesigner = JokeDesigner(dictionaryRepo = adultMainRepo,
            primarySemanticRepo = adultNucleusRepo,
            secondarySemanticRepo = adultMainRepo,
            linguisticRepo = adultMainRepo,
            nucleusRepository = adultNucleusRepo)
    val adultJokeService: ClaptrapService = ClaptrapService(adultJokeDesigner)

    private val childMainRepo = JsonRepository(showAdult = false)
    private val childNucleusRepo = JsonRepository(object{}.javaClass.getResourceAsStream("/nucleusDictionary.json"),
                                                  showAdult = false)

    val childJokeDesigner = JokeDesigner(dictionaryRepo = childMainRepo,
            primarySemanticRepo = childNucleusRepo,
            secondarySemanticRepo = childMainRepo,
            linguisticRepo = childMainRepo,
            nucleusRepository = childNucleusRepo)
    val childJokeService: ClaptrapService = ClaptrapService(childJokeDesigner)

    @GetMapping("/joke")
    @CrossOrigin
    fun getJoke(@RequestParam(value="sweary") adult: Boolean): ResponseEntity<Joke> {
        val joke = when (adult) {
            true -> adultJokeService.tellJoke()
            false -> childJokeService.tellJoke()
        }
        return ResponseEntity.ok(joke)
    }
}