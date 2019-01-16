package com.wabradshaw.claptrap.logging

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