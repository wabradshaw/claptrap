package com.wabradshaw.claptrap.generation.json

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.wabradshaw.claptrap.generation.PunchlineTemplate
import com.wabradshaw.claptrap.generation.SetupTemplate
import com.wabradshaw.claptrap.generation.Template
import java.io.InputStream

//This is ugly. Not happy with the duplication of code. Tried
/**
 * The type of TemplateLoader for SetupTemplates.
 */
class SetupTemplateLoader(){
    /**
     * Loads the Templates stored in the supplied input stream. If no input stream is provided, the default
     * templates will be loaded.
     *
     * @param jsonSource The location where JSON representing the SetupTemplates is stored.
     */
    fun load(jsonSource: InputStream = object{}.javaClass.getResourceAsStream("/setupTemplates.json"))
            : List<SetupTemplate>{
        return jacksonObjectMapper().readValue<List<SetupTemplateMappingDTO>>(jsonSource).map{it.toTemplate()}
    }
}

/**
 * The type of TemplateLoader for PunchlineTemplates.
 */
class PunchlineTemplateLoader(){
    /**
     * Loads the Templates stored in the supplied input stream. If no input stream is provided, the default
     * templates will be loaded.
     *
     * @param jsonSource The location where JSON representing the SetupTemplates is stored.
     */
    fun load(jsonSource: InputStream = object{}.javaClass.getResourceAsStream("/punchlineTemplates.json"))
            : List<PunchlineTemplate>{
        return jacksonObjectMapper().readValue<List<PunchlineTemplateMappingDTO>>(jsonSource).map{it.toTemplate()}
    }
}