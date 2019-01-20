package com.wabradshaw.claptrap.logging

import com.wabradshaw.claptrap.JokeDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LoggingService {

    @Autowired
    lateinit var loggingRepository: LoggingRepository;

    fun logRating(token : String, joke : JokeRatingDTO){
        loggingRepository.logRating(token, joke)
    }

    fun logRelation(token : String, relation : RelationRatingDTO){
        loggingRepository.logRelation(token, relation)
    }

    fun logSuggestion(token : String, oldJoke : JokeDTO, newJoke : JokeDTO) {
        loggingRepository.logSuggestion(token, oldJoke, newJoke)
    }

}