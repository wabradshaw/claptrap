package com.wabradshaw.claptrap.generation.json

import com.wabradshaw.claptrap.generation.*
import java.util.function.Supplier

/**
 * A basic DTO class to make it easier to load Templates through JSON. Complicated by needing different classes for
 */
abstract class TemplateMappingDTO<T : Template>(private val id: String,
                                                private val script: String,
                                                private val constraints: List<ConstraintDTO>) {

    abstract fun supply(id: String, constraints: List<TemplateConstraint>, script: String): T

    fun toTemplate(): T {
        return supply(id, constraints.map { it.toConstraint()}, script)
    }
}

class SetupTemplateMappingDTO(private val id: String,
                              private val script: String,
                              private val constraints: List<ConstraintDTO>)
    : TemplateMappingDTO<SetupTemplate>(id, script, constraints) {

    override fun supply(id: String, constraints: List<TemplateConstraint>, script: String): SetupTemplate {
        return SetupTemplate(id, constraints, script);
    }
}

class PunchlineTemplateMappingDTO(private val id: String,
                                  private val script: String,
                                  private val constraints: List<ConstraintDTO>)
    : TemplateMappingDTO<PunchlineTemplate>(id, script, constraints) {

    override fun supply(id: String, constraints: List<TemplateConstraint>, script: String): PunchlineTemplate {
        return PunchlineTemplate(id, constraints, script);
    }
}

class ConstraintDTO(private val type: TemplateConstraintType, private val arg: String){
    fun toConstraint(): TemplateConstraint {
        return TemplateConstraint(type, arg)
    }
}