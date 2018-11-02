package com.wabradshaw.claptrap.structure

/**
 * A relationship links an original word, and a different semantic substitution word.
 */
enum class Relationship {

    /**
     * The two words mean practically the same thing. E.g. fire == flame.
     */
    SYNONYM,

    /**
     * The original word is a more specific type of the substitution. E.g. cabbage IS_A vegetable.
     */
    IS_A,

    /**
     * The substitution word is a more specific type of the original. E.g. vegetable INCLUDES cabbage.
     */
    INCLUDES,

    /**
     * The original word contains the substitution. E.g. flower HAS_A petal.
     */
    HAS_A,

    /**
     * The substitution word contains the original. E.g. petal PART_OF flower
     */
    PART_OF,

    /**
     * The original word is the opposite of the substitute. Only used for adj/adv. E.g. hot IS_NOT cold.
     */
    IS_NOT,

    /**
     * The original word resides inside the substitute. E.g. dog IN kennel.
     */
    IN,

    /**
     * The original word resides on top of the substitute. E.g. hair ON head.
     */
    ON,

    /**
     * The original word comes from the substitute. E.g. purr FROM cat.
     */
    FROM;
}