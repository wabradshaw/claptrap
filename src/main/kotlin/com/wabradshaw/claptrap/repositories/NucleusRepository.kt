package com.wabradshaw.claptrap.repositories

import com.wabradshaw.claptrap.structure.Relationship
import com.wabradshaw.claptrap.structure.Word
import com.wabradshaw.claptrap.structure.SemanticSubstitution

/**
 * A NucleusRepository is an interface for connectors to different dictionaries that contain words that are suitable
 * to joke about. This means that part of the word could be substituted for something else as the nucleus for a
 * punchline.
 */
interface NucleusRepository {

    /**
     * Gets a random nucleus from the dictionary.
     *
     * @return A random joke nucleus.
     */
    fun getNucleus(): Word

}