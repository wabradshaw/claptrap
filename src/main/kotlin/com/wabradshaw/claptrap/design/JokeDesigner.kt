package com.wabradshaw.claptrap.design

import com.wabradshaw.claptrap.structure.JokeSpec

/**
 * The JokeDesigner is used to design the specification for jokes. It's method generate JokeSpecs
 * that can be turned into actual written jokes.
 */
class JokeDesigner(){

    /**
     * Generates the joke specification for a joke where the user has supplied the principle word in the
     * joke punchline. This nucleus word will be altered to create a funnier alternative.
     *
     * Please note that this is a joke telling AI. The joke will probably be rubbish.
     *
     * @param nucleus The word that should be the focus for the joke. It will appear in the body.
     */
    fun designJokeFromNucleus(nucleus: String): JokeSpec {
        return TODO()
    }
}