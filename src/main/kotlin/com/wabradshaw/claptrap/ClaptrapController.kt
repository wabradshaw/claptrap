package com.wabradshaw.claptrap

import com.wabradshaw.claptrap.design.JokeDesigner
import com.wabradshaw.claptrap.repositories.custom.JsonRepository
import com.wabradshaw.claptrap.structure.Relationship
import org.jetbrains.exposed.sql.not
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/*
 * A RESTful controller for the Claptrap system
 */
@RestController
class ClaptrapController {

    private val maxLetters = 30

    private val mainRepo = JsonRepository(showAdult = false)
    private val nucleusRepo = JsonRepository(object{}.javaClass.getResourceAsStream("/nucleusDictionary.json"),
                                                  showAdult = false)

    val jokeDesigner = JokeDesigner(dictionaryRepo = mainRepo,
            primarySemanticRepo = nucleusRepo,
            secondarySemanticRepo = mainRepo,
            linguisticRepo = mainRepo,
            nucleusRepository = nucleusRepo)
    val jokeService: ClaptrapService = ClaptrapService(jokeDesigner)

    @GetMapping("/joke")
    @CrossOrigin
    fun getJoke(): ResponseEntity<JokeDTO> {
        val joke = jokeService.tellJoke()
        return ResponseEntity.ok(JokeDTO(joke))
    }

    @GetMapping("/joke/custom")
    @CrossOrigin
    fun regenerateJoke(@RequestParam("nucleus") nucleus: String,
                       @RequestParam("linguisticOriginal") linguisticOriginal: String,
                       @RequestParam("linguisticSubstitute") linguisticSubstitute: String,
                       @RequestParam("primarySetup") primarySetup: String,
                       @RequestParam("primaryRelationship") primaryRelationship: Relationship,
                       @RequestParam("secondarySetup") secondarySetup: String,
                       @RequestParam("secondaryRelationship") secondaryRelationship: Relationship): ResponseEntity<JokeDTO> {

        return if(tooLong(nucleus) || tooLong(linguisticSubstitute) || tooLong(primarySetup) || tooLong(secondarySetup)) {
            ResponseEntity.badRequest().build()
        } else if (!nucleus.contains(linguisticOriginal)){
            ResponseEntity.badRequest().build()
        } else {
            val joke = jokeService.tellJoke() //Todo: change to regenerateJoke
            ResponseEntity.ok(JokeDTO(joke))
        }
    }

    private fun tooLong(string: String) : Boolean{
        return string.length > maxLetters
    }
}