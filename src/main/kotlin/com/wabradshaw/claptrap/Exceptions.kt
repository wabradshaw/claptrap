package com.wabradshaw.claptrap

open class NoJokeException(message: String): Exception(message)

class NoSubstitutionException(word: String):
        NoJokeException("Could not create a joke as there is no substitution available for '" + word + "'.")