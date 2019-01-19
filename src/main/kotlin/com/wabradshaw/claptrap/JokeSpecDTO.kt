package com.wabradshaw.claptrap

import com.wabradshaw.claptrap.structure.Joke

/**
 * A data transfer object representing information about a joke that may become visible to the user.
 */
class JokeSpecDTO() {
    lateinit var setup: String
    lateinit var punchline: String

    lateinit var template: String
    lateinit var nucleus: String

    var primarySetup: String? = null
    var primaryRelationship: String? = null

    lateinit var secondarySetup: String
    lateinit var secondaryRelationship: String

    lateinit var linguisticOriginal: String
    lateinit var linguisticReplacement: String

    /**
     * Secondary constructor that converts a Joke into this DTO object.
     */
    constructor(joke: Joke) : this(){
        setup = joke.setup
        punchline = joke.punchline
        template = joke.setupTemplate
        nucleus = joke.spec.nucleus

        primarySetup = joke.spec.primarySetup?.substitution?.spelling
        primaryRelationship = joke.spec.primarySetup?.relationship?.name

        secondarySetup = joke.spec.secondarySetup.substitution.spelling
        secondaryRelationship = joke.spec.secondarySetup.relationship.name

        linguisticOriginal = joke.spec.linguisticSub.original.spelling
        linguisticReplacement = joke.spec.linguisticSub.substitution.spelling
    }
}