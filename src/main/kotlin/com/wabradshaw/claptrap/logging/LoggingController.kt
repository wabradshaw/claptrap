package com.wabradshaw.claptrap.logging

import org.springframework.web.bind.annotation.*

/*
 * A RESTful controller for logging ratings in the system
 */
@RestController
@RequestMapping(value= ["/service"])
class LoggingController(val service: LoggingService) {

    @PostMapping("/rate/{token}")
    @CrossOrigin
    fun rateJoke(@PathVariable(value = "token") token: String,
                 @RequestBody() body: JokeRatingDTO) {
        service.logRating(token, body)
    }

    @PostMapping("/rate/relation/{token}")
    @CrossOrigin
    fun rateRelationship(@PathVariable(value = "token") token: String,
                         @RequestBody() body: RelationRatingDTO) {
        service.logRelation(token, body)
    }

    @PostMapping("/suggest/{token}")
    @CrossOrigin
    fun rateRelationship(@PathVariable(value = "token") token: String,
                         @RequestBody() body: JokeImprovementDTO) {
        service.logSuggestion(token, body.oldJoke, body.newJoke)
    }
}