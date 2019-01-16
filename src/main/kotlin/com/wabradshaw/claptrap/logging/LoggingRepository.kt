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
    fun logRating(tokenVal: String, joke: JokeRatingDTO){
        connect();

        transaction{
            JokeRatings.replace {
                it[token] = tokenVal
                it[setup] = joke.setup
                it[punchline] = joke.punchline
                it[vote] = joke.vote
                it[template] = joke.template
                it[nucleus] = joke.nucleus
                it[hasPrimary] = joke.nucleus.endsWith(joke.primarySetup ?: joke.nucleus)
                it[primarySetup] = joke.primarySetup ?: joke.nucleus
                it[secondarySetup] = joke.secondarySetup
                it[linguisticOriginal] = joke.linguisticOriginal
                it[linguisticReplacement] = joke.linguisticReplacement
                it[primaryRelationship] = joke.primaryRelationship ?: "nucleus"
                it[secondaryRelationship] = joke.secondaryRelationship
            }

        }
    }
}