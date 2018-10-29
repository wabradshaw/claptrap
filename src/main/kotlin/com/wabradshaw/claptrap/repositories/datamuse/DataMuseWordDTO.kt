package com.wabradshaw.claptrap.repositories.datamuse

import com.fasterxml.jackson.annotation.JsonInclude
import com.wabradshaw.claptrap.structure.PartOfSpeech
import com.wabradshaw.claptrap.structure.Word

/**
 * JSON transfer object to convert DataMuse responses into Word objects.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class DataMuseWordDTO(val word:String, val score:Int, val tags: List<String>){

    private val pos = when (tags.get(1)){
        "n" -> PartOfSpeech.NOUN
        "v" -> PartOfSpeech.VERB
        "adj" -> PartOfSpeech.ADJECTIVE
        else -> PartOfSpeech.NOUN
    }
    private val pronunciation: String = tags.filter{x -> x.startsWith("pron:")}.get(0).split(":").get(1)
    private val frequency: Double = tags.filter{x -> x.startsWith("f:")}.get(0).split(":").get(1).toDouble()

    /**
     * Convert this object into a standard Word object.
     */
    fun toWord(): Word {
        return Word(word, pronunciation, pos, frequency)
    }
}