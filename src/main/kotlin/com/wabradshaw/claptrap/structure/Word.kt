package com.wabradshaw.claptrap.structure

/**
 * Data class representing a dictionary word.
 *
 * @property spelling How the word is spelled out
 * @property pronunciation How the word is pronounced, in two letter ARPABET form (https://en.wikipedia.org/wiki/ARPABET)
 * @property partOfSpeech How the word is used in a sentence
 * @property frequency How often the word appears, per million average words
 * @property form The way the sentence should be structured around the word. More specific than POS.
 */
data class Word(val spelling: String,
                val pronunciation: String,
                val partOfSpeech: PartOfSpeech,
                val frequency: Double,
                val form: Form = Form.NORMAL)