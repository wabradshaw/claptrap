package com.wabradshaw.claptrap.structure

/**
 * An enum representing different usage forms. This form changes how a word (of that particular POS) is used in a
 * sentence. For example, a normal noun could be "a cat", while a unique noun could be "the sun".
 */
enum class Form {

    /* The typical form for the POS. E.g. "a cat", "an apple" */
    NORMAL,

    /* A plural noun. E.g. "cabbages", "oxen" */
    PLURAL,

    /* A word that isn't typically countable. This means it can't be described as 'a'. E.g. "management", "tennis" */
    UNCOUNT,

    /* A proper noun for a thing or place. E.g. "London", "Microsoft" */
    PROPER_NOUN,

    /* The name of a person. E.g. "Nikola Tesla", "Batman" */
    PERSON,

    /* A particular known instance. E.g. "The British Isles", "The sun" */
    UNIQUE



}
