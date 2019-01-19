package com.wabradshaw.claptrap.logging

import org.springframework.web.bind.annotation.*

/*
 * A RESTful controller for logging ratings in the system
 */
@RestController
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
}