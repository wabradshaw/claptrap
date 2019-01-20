package com.wabradshaw.claptrap.logging

import com.wabradshaw.claptrap.logging.JokeRatings.primaryKey
import com.wabradshaw.claptrap.logging.RelationshipRatings.primaryKey
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object JokeRatings : Table() {
    val token : Column<String> = varchar("token", length=32).primaryKey(0) // A token representing a user's session
    val setup : Column<String> = varchar("setup", length=128).primaryKey(1) // The full joke setup
    val punchline : Column<String> = varchar("punchline", length=32).primaryKey(2) // The full joke punchline
    val vote : Column<Int> = integer("vote") // How they voted, with 1 for good, -1 for bad
    val template : Column<String> = varchar("template", length=12) // The id of the setup template
    val nucleus : Column<String> = varchar("nucleus", length=32) // The word used as the nucleus
    val hasPrimary : Column<Boolean> = bool("hasPrimary") // Whether or not there was a separate primary setup
    val primarySetup : Column<String> = varchar("primarySetup", length=64) // The link to the nucleus word
    val secondarySetup : Column<String> = varchar("secondarySetup", length=64) // The link to the substitution
    val linguisticOriginal : Column<String> = varchar("linguisticOriginal", length=16) // The substring that was replaced
    val linguisticReplacement : Column<String> = varchar("linguisticReplacement", length=16) // The linguistic replacement
    val primaryRelationship : Column<String> = varchar("primaryRelationship", length=16) // How the primary setup links to the nucleus
    val secondaryRelationship : Column<String> = varchar("secondaryRelationship", length=16) // How the secondary setup links to the linguistic replacement
    init {
        index(true, token, setup, punchline)
    }
}

object RelationshipRatings : Table() {
    val token : Column<String> = varchar("token", length=32).primaryKey(0) // A token representing a user's session
    val original : Column<String> = varchar("original", length=64).primaryKey(1) // The primary word in the relationship
    val substitution : Column<String> = varchar("substitution", length=64).primaryKey(2) // The secondary word in the relationship
    val relationship : Column<String> = varchar("relationship", length=16).primaryKey(3) // How the words were linked
    val position : Column<String> = varchar("position", length=16).primaryKey(4) // Where in the joke it was used
    val wrong : Column<Boolean> = JokeRatings.bool("wrong") // Whether or not the user has marked this as an issue
    init {
        index(true, token, original, substitution, relationship, position)
    }
}

object JokeSuggestions : Table() {
    val token : Column<String> = varchar("token", length=32) // A token representing a user's session
    val nucleus : Column<String> = varchar("nucleus", length=32) // The word used as the nucleus in both
    val linguisticOriginal : Column<String> = varchar("linguisticOriginal", length=16) // The substring that was replaced in both
    val linguisticReplacement : Column<String> = varchar("linguisticReplacement", length=16) // The linguistic replacement in both
    val oldSetup : Column<String> = varchar("oldSetup", length=128) // The full joke setup in the original joke
    val oldPunchline : Column<String> = varchar("oldPunchline", length=32) // The full joke punchline in the original joke
    val oldTemplate : Column<String> = varchar("oldTemplate", length=12) // The id of the setup template in the original joke
    val oldPrimarySetup : Column<String> = varchar("oldPrimarySetup", length=64) // The link to the nucleus word in the original joke
    val oldSecondarySetup : Column<String> = varchar("oldSecondarySetup", length=64) // The link to the substitution in the original joke
    val oldPrimaryRelationship : Column<String> = varchar("oldPrimaryRelationship", length=16) // How the primary setup links to the nucleus in the original joke
    val oldSecondaryRelationship : Column<String> = varchar("oldSecondaryRelationship", length=16) // How the secondary setup links to the linguistic replacement in the original joke
    val newSetup : Column<String> = varchar("newSetup", length=128) // The full joke setup in the suggested joke
    val newPunchline : Column<String> = varchar("newPunchline", length=32) // The full joke punchline in the suggested joke
    val newTemplate : Column<String> = varchar("newTemplate", length=12) // The id of the setup template in the suggested joke
    val newPrimarySetup : Column<String> = varchar("newPrimarySetup", length=64) // The link to the nucleus word in the suggested joke
    val newSecondarySetup : Column<String> = varchar("newSecondarySetup", length=64) // The link to the substitution in the suggested joke
    val newPrimaryRelationship : Column<String> = varchar("newPrimaryRelationship", length=16) // How the primary setup links to the nucleus in the suggested joke
    val newSecondaryRelationship : Column<String> = varchar("newSecondaryRelationship", length=16) // How the secondary setup links to the linguistic replacement in the suggested joke
}