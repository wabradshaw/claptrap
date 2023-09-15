package com.wabradshaw.claptrap.generation

import com.wabradshaw.claptrap.NoJokeException
import com.wabradshaw.claptrap.structure.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * A set of tests for the JokeGenerator
 */
class JokeGeneratorTest {

    private val mockSubstituter = mock(JokeSubstituter::class.java)
    private val mockDeterminerManager = mock(DeterminerManager::class.java)

    private val normal = TemplateConstraint(TemplateConstraintType.NUCLEUS_FORM, "NORMAL")
    private val unique = TemplateConstraint(TemplateConstraintType.NUCLEUS_FORM, "UNIQUE")

    private val validSetup1 = SetupTemplate("a",emptyList(), "A $1 $2")
    private val validSetup2 = SetupTemplate("b",listOf(normal), "B $1 $2")
    private val invalidSetup = SetupTemplate("c", listOf(unique), "C $1 $2")

    private val validPunchline1 = PunchlineTemplate("z",emptyList(), "Z $3")
    private val validPunchline2 = PunchlineTemplate("y",listOf(normal), "Y $3")
    private val invalidPunchline = PunchlineTemplate("x", listOf(unique), "X $3")

    private val spec = JokeSpec("nucleus",
            Word("nucleus","nucleus", PartOfSpeech.NOUN, 1.0, Form.NORMAL),
            SemanticSubstitution(Word("a center","a center",PartOfSpeech.NOUN, 1.0),
                                 Word("nucleus","nucleus",PartOfSpeech.NOUN, 1.0),
                                 Relationship.NEAR_SYNONYM),
            SemanticSubstitution(Word("spew","spew",PartOfSpeech.NOUN, 1.0),
                                 Word("puke","puke",PartOfSpeech.NOUN, 1.0),
                                 Relationship.IS_A),
            LinguisticSubstitution(Word("puke","puke",PartOfSpeech.NOUN, 1.0),
                                   Word("nuc","nuc",PartOfSpeech.NOUN, 1.0),
                                   LinguisticSimilarity.NEAR_RHYME))

    /**
     * Sets up the default behaviour of the mocks
     */
    @BeforeEach
    fun setupMocks(){
        `when`(mockSubstituter.createJokeWord(spec)).thenReturn("puke-leus")
        `when`(mockDeterminerManager.chooseDefaultDeterminer("puke-leus")).thenReturn("a")
        `when`(mockDeterminerManager.removeDeterminer("a center")).thenReturn("center")
        `when`(mockDeterminerManager.removeDeterminer("spew")).thenReturn("spew")
        `when`(mockDeterminerManager.removeDeterminer("a puke-leus")).thenReturn("center")
    }

    /**
     * Tests generation for a standard joke where there is one possible setup and one possible punchline.
     */
    @Test
    fun testJoke(){
        val generator = JokeGenerator(listOf(validSetup1),
                                      listOf(validPunchline1),
                                      mockSubstituter,
                                      mockDeterminerManager)

        val result = generator.generateJoke(spec)
        assertEquals("A a center spew", result.setup)
        assertEquals("Z a puke-leus", result.punchline)
        assertEquals(spec, result.spec)
        assertEquals("a", result.setupTemplate)
        assertEquals("z", result.punchlineTemplate)
    }

    /**
     * Tests generation for a joke that doesn't have a possible setup will throw an exception.
     */
    @Test
    fun testNoSetup(){
        val generator = JokeGenerator(emptyList(),
                                      listOf(validPunchline1),
                                      mockSubstituter,
                                      mockDeterminerManager)

        assertThrows(NoJokeException::class.java, {generator.generateJoke(spec)})
    }

    /**
     * Tests generation for a joke that doesn't have a possible punchline will throw an exception.
     */
    @Test
    fun testNoPunchline(){
        val generator = JokeGenerator(listOf(validSetup1),
                                      emptyList(),
                                      mockSubstituter,
                                      mockDeterminerManager)

        assertThrows(NoJokeException::class.java, {generator.generateJoke(spec)})
    }

    /**
     * Tests generation for a standard joke where there are multiple possible valid setups.
     */
    @Test
    fun testMultipleSetupsJoke(){
        val generator = JokeGenerator(listOf(validSetup1, validSetup2),
                listOf(validPunchline1),
                mockSubstituter,
                mockDeterminerManager)

        val results = HashSet<String>()
        for(i in 1..100){
            val result = generator.generateJoke(spec)
            results.add(result.setup)
            assertEquals(result.setupTemplate, result.setup.substring(0,1).lowercase())
        }
        assertEquals(2, results.size)
        assertTrue(results.contains("A a center spew"))
        assertTrue(results.contains("B a center spew"))
    }

    /**
     * Tests generation for a standard joke where there are multiple possible valid punchlines.
     */
    @Test
    fun testMultiplePunchlinesJoke(){
        val generator = JokeGenerator(listOf(validSetup1),
                listOf(validPunchline1, validPunchline2),
                mockSubstituter,
                mockDeterminerManager)

        val results = HashSet<String>()
        for(i in 1..100){
            val result = generator.generateJoke(spec)
            results.add(result.punchline)
            assertEquals(result.punchlineTemplate, result.punchline.substring(0,1).lowercase())
        }
        assertEquals(2, results.size)
        assertTrue(results.contains("Z a puke-leus"))
        assertTrue(results.contains("Y a puke-leus"))
    }

    /**
     * Tests generation for a standard joke will ignore a setup that can't apply to the spec.
     */
    @Test
    fun testInvalidSetup(){
        val generator = JokeGenerator(listOf(validSetup1, invalidSetup, validSetup2),
                listOf(validPunchline1),
                mockSubstituter,
                mockDeterminerManager)

        val results = HashSet<String>()
        for(i in 1..100){
            val result = generator.generateJoke(spec)
            results.add(result.setup)
            assertEquals(result.setupTemplate, result.setup.substring(0,1).lowercase())
        }
        assertEquals(2, results.size)
        assertTrue(results.contains("A a center spew"))
        assertTrue(results.contains("B a center spew"))
    }

    /**
     * Tests generation for a standard joke will ignore a punchline that can't apply to the spec.
     */
    @Test
    fun testInvalidPunchline(){
        val generator = JokeGenerator(listOf(validSetup1),
                listOf(validPunchline1, invalidPunchline, validPunchline2),
                mockSubstituter,
                mockDeterminerManager)

        val results = HashSet<String>()
        for(i in 1..100){
            val result = generator.generateJoke(spec)
            results.add(result.punchline)
            assertEquals(result.punchlineTemplate, result.punchline.substring(0,1).lowercase())
        }
        assertEquals(2, results.size)
        assertTrue(results.contains("Z a puke-leus"))
        assertTrue(results.contains("Y a puke-leus"))
    }
    /**
     * Tests generation for a standard joke where there is one possible setup and one possible punchline.
     */
    @Test
    fun testNoNucleusObject(){

        val nuclessSpec = JokeSpec("nucleus",
                null,
                null,
                SemanticSubstitution(Word("spew","spew",PartOfSpeech.NOUN, 1.0),
                        Word("puke","puke",PartOfSpeech.NOUN, 1.0),
                        Relationship.IS_A),
                LinguisticSubstitution(Word("puke","puke",PartOfSpeech.NOUN, 1.0),
                        Word("nuc","nuc",PartOfSpeech.NOUN, 1.0),
                        LinguisticSimilarity.NEAR_RHYME))

        `when`(mockSubstituter.createJokeWord(nuclessSpec)).thenReturn("puke-leus")
        `when`(mockDeterminerManager.removeDeterminer("nucleus")).thenReturn("nucleus")

        val generator = JokeGenerator(listOf(validSetup1),
                listOf(validPunchline1),
                mockSubstituter,
                mockDeterminerManager)

        val result = generator.generateJoke(nuclessSpec)
        assertEquals("A nucleus spew", result.setup)
        assertEquals("Z a puke-leus", result.punchline)
        assertEquals(nuclessSpec, result.spec)
        assertEquals("a", result.setupTemplate)
        assertEquals("z", result.punchlineTemplate)
    }
}