package com.wabradshaw.claptrap.generation.json

import com.wabradshaw.claptrap.generation.SetupConstraint
import com.wabradshaw.claptrap.generation.SetupConstraintType
import com.wabradshaw.claptrap.generation.SetupTemplate

/**
 * A basic DTO class to make it easier to load a SetupTemplate through JSON
 */
class SetupMappingDTO(private val id: String,
                      private val script: String,
                      private val constraints: List<SetupConstraintDTO>) {

    fun toTemplate(): SetupTemplate {
        return SetupTemplate(id, constraints.map { it.toConstraint()}, script)
    }
}

class SetupConstraintDTO(private val type: SetupConstraintType, private val arg: String){
    fun toConstraint(): SetupConstraint {
        return SetupConstraint(type, arg)
    }
}