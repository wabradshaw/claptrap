package com.wabradshaw.claptrap.logging

import com.wabradshaw.claptrap.ClaptrapService
import com.wabradshaw.claptrap.design.JokeDesigner
import com.wabradshaw.claptrap.repositories.custom.JsonRepository
import com.wabradshaw.claptrap.structure.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/*
 * A RESTful controller for logging ratings in the system
 */
@RestController
class LoggingController(val service: LoggingService) {

    @PostMapping("/rate/{token}")
    @CrossOrigin
    fun rateJoke(@PathVariable(value = "token") token: String) {
        val vote = "1";

        val spec = JokeSpec("catapult",
                null,
                null,
                SemanticSubstitution(Word("a brim", "brim", PartOfSpeech.NOUN, 1.0),
                        Word("hat", "hat", PartOfSpeech.NOUN, 1.0),
                        Relationship.HAS_A),
                LinguisticSubstitution(Word("hat", "hat", PartOfSpeech.NOUN, 1.0),
                        Word("cat", "cat", PartOfSpeech.NOUN, 1.0),
                        LinguisticSimilarity.RHYME)
        )
        val body = Joke("What do you call a catauplt with a brim", "A hat-apult!", spec, "HAS01")


        service.logRating(token, body, Vote.GOOD)
    }
}