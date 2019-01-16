package com.wabradshaw.claptrap.logging

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.replace
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

/**
 * A repository that connects to the logging database to handle user feedback on jokes.
 */
@Repository
class LoggingRepository(val db: DatabaseConfiguration) {

    /**
     * Connect to the database. Called by every method here.
     */
    private fun connect() = Database.connect(url=db.url, user=db.user, password=db.password, driver=db.driver)

    /**
     * Inserts the specified joke rating into the database
     * @param tokenVal   The string token representing the user's current session. Used to handle multiple votes on the
     *                   same joke.
     * @param jokeRating The rating for the joke. Includes both the user's vote, and the info on the joke.
     */
    fun logRating(tokenVal: String, jokeRating: JokeRatingDTO){
        connect();

        val joke = jokeRating.joke

        transaction{
            JokeRatings.replace {
                it[token] = tokenVal
                it[vote] = jokeRating.vote

                it[setup] = joke.setup
                it[punchline] = joke.punchline
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