package com.wabradshaw.claptrap

/**
 * Data class representing a dictionary word.
 *
 * @property spelling How the word is spelled out
 * @property pronunciation How the word is pronounced, in two letter ARPABET form (https://en.wikipedia.org/wiki/ARPABET)
 * @property partOfSpeech How the word is used in a sentence
 * @property popularity How often the word appears, per million average words
 */
data class Word(val spelling: String,
                val pronunciation: String,
                val partOfSpeech: PartOfSpeech,
                val popularity: Double)