package com.wabradshaw.claptrap.generation.json

import com.wabradshaw.claptrap.generation.SetupConstraintType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * A set of tests for the SetupTemplateLoader
 */
class SetupTemplateLoaderTest {

    /**
     * Tests the template loader loading an empty array.
     */
    @Test
    fun testEmptyTemplates(){
        val result = SetupTemplateLoader().load(object{}.javaClass.getResourceAsStream("/setup-templates/empty.json"))
        assertEquals(0, result.size)
    }

    /**
     * Tests the template loader loading a single template which doesn't have any constraints.
     */
    @Test
    fun testSingleNoConstraints(){
        val result = SetupTemplateLoader().load(object{}.javaClass.getResourceAsStream("/setup-templates/singleNoConstraints.json"))
        assertEquals(1, result.size)
        assertEquals("has01", result[0].id)
        assertEquals("What do you call a $1 with $2?", result[0].script)
        assertEquals(0, result[0].constraints.size)
    }

    /**
     * Tests the template loader loading a single template which has one constraint.
     */
    @Test
    fun testSingleOneConstraint(){
        val result = SetupTemplateLoader().load(object{}.javaClass.getResourceAsStream("/setup-templates/singleOneConstraint.json"))
        assertEquals(1, result.size)
        assertEquals("has01", result[0].id)
        assertEquals("What do you call a $1 with $2?", result[0].script)
        assertEquals(1, result[0].constraints.size)
        assertEquals(SetupConstraintType.SECONDARY_RELATIONSHIP, result[0].constraints[0].constraint)
        assertEquals("HAS_A", result[0].constraints[0].arg)
    }

    /**
     * Tests the template loader loading a single template which has multiple constraints.
     */
    @Test
    fun testSingleMultipleConstraints(){
        val result = SetupTemplateLoader().load(object{}.javaClass.getResourceAsStream("/setup-templates/singleMultipleConstraints.json"))
        assertEquals(1, result.size)
        assertEquals("has01", result[0].id)
        assertEquals("What do you call a $1 with $2?", result[0].script)
        assertEquals(3, result[0].constraints.size)
    }

    /**
     * Tests the template loader loading multiple templates.
     */
    @Test
    fun testMultiple(){
        val result = SetupTemplateLoader().load(object{}.javaClass.getResourceAsStream("/setup-templates/multiple.json"))
        assertEquals(3, result.size)
    }
}