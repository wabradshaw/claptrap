package com.wabradshaw.claptrap.repositories.datamuse

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.wabradshaw.claptrap.repositories.DictionaryRepository
import com.wabradshaw.claptrap.structure.Word
import java.net.URL

class DataMuseRepository : DictionaryRepository {

    override fun getWord(string: String): Word? {
        val url = URL("https://api.datamuse.com/words?sp=$string&qe=sp&md=frp&max=1")
        println(url.readText())


        val mapper = jacksonObjectMapper()
        val words = mapper.readValue<List<DataMuseWordDTO>>(url)
        val result = words.getOrNull(0)?.toWord()

        return when(result?.spelling){
            string -> result
            else -> null
        }
    }
}
