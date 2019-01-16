package com.wabradshaw.claptrap.logging

import com.wabradshaw.claptrap.structure.Joke
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LoggingService {

    @Autowired
    lateinit var loggingRepository: LoggingRepository;

    fun logRating(token : String, joke : Joke, vote: Vote){
        when(vote) {
            Vote.NEUTRAL -> TODO() //Delete rating
            else -> loggingRepository.logRating(token, joke, vote)
        }
    }

}