package com.wabradshaw.claptrap.logging

import com.wabradshaw.claptrap.structure.Joke
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.replace
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class LoggingRepository(val db: DatabaseConfiguration) {

    /**
     * Connect to the database. Called by every method here.
     */
    private fun connect() = Database.connect(url=db.url, user=db.user, password=db.password, driver=db.driver)

    /**
     * Inserts the specified trip into the database.
     * @param startTimeVal The dateTime when the trip started.
     * @param endTimeVal The dateTime when the user will move on from the trip. Null if that isn't known yet.
     * @param groupVal The name of the overall group of trips this trip is part of.
     * @param nameVal The name of the location for the trip.
     * @param countryVal The name of the country the trip is in.
     * @param timezoneVal The timezone offset for the location.
     */
    fun logRating(tokenVal: String, joke: Joke, voteVal : Vote){
        connect();

        transaction{
            JokeRatings.replace {
                it[token] = tokenVal
                it[setup] = joke.setup
                it[punchline] = joke.punchline

                it[vote] = when(voteVal){
                    Vote.GOOD -> 1
                    Vote.NEUTRAL -> 0
                    Vote.BAD -> -1
                }

                it[template] = joke.setupTemplate
                it[nucleus] = joke.spec.nucleus
                it[hasPrimary] = joke.spec.nucleus.endsWith(joke.spec.primarySetup?.substitution?.spelling ?: joke.spec.nucleus)
                it[primarySetup] = joke.spec.primarySetup?.substitution?.spelling ?: joke.spec.nucleus
                it[secondarySetup] = joke.spec.secondarySetup.substitution.spelling
                it[linguisticOriginal] = joke.spec.linguisticSub.original.spelling
                it[linguisticReplacement] = joke.spec.linguisticSub.substitution.spelling
                it[primaryRelationship] = joke.spec.primarySetup?.relationship?.name ?: "nucleus"
                it[secondaryRelationship] = joke.spec.secondarySetup.relationship.name
            }

        }
    }
}