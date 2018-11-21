package com.wabradshaw.claptrap.generation.json

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.wabradshaw.claptrap.generation.SetupTemplate
import java.io.InputStream

/**
 * A class used to deserialise JSON lists representing SetupTemplates.
 */
class SetupTemplateLoader(){

    /**
     * Loads the SetupTemplates stored in the supplied input stream. If no input stream is provided, the default
     * templates will be loaded.
     *
     * @param jsonSource The location where JSON representing the SetupTemplates is stored.
     */
    fun load(jsonSource: InputStream = object{}.javaClass.getResourceAsStream("/templates.json")): List<SetupTemplate>{
        return jacksonObjectMapper().readValue<List<SetupMappingDTO>>(jsonSource).map{it.toTemplate()}
    }

}