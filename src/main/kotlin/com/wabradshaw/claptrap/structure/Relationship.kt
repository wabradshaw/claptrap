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
     * The two words mean approximately the same thing. E.g. tree == bush.
     */
    NEAR_SYNONYM,

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
    FROM,

    /**
     * The substitution is a descriptive adjective that can be used to reference the original. E.g. scaly PROPERTY fish.
     */
    PROPERTY,

    /**
     * The substitute is a verb that the original can perform. This action should be in present 3rd singular form.
     * E.g. runs ACTION jogger.
     */
    ACTION,

    /**
     * The substitute is a verb that the original can frequently perform. This action should be in continuous form.
     * E.g. running ACTION_CONTINUOUS jogger.
     */
    ACTION_CONTINUOUS,

    /**
     * The substitute is a verb that can be performed on the original. This action should be in bare infinitive form.
     * E.g. write RECIPIENT_ACTION letter.
     */
    RECIPIENT_ACTION,

    /**
     * The substitute is a verb that could have been performed on the original. This action should be in past tense.
     * E.g. written RECIPIENT_ACTION_PAST letter.
     *
     * Note that actions which would have changed the nature of the original (rendering it unable to be described using
     * the original word) should not be used. For example, the substitute toasted would not be appropriate for the
     * original word bread.
     */
    RECIPIENT_ACTION_PAST;
}