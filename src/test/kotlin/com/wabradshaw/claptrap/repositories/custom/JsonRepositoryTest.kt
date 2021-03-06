package com.wabradshaw.claptrap.repositories.custom

import com.wabradshaw.claptrap.structure.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * A set of tests for the JsonRepository
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonRepositoryTest {

    private val cat = Word("cat", "cat", PartOfSpeech.NOUN, 100.0)
    private val bat = Word("bat", "bat", PartOfSpeech.NOUN, 100.0)
    private val hat = Word("hat", "hat", PartOfSpeech.NOUN, 100.0)
    private val rat = Word("rat", "rat", PartOfSpeech.NOUN, 100.0)
    private val twat = Word("twat", "twat", PartOfSpeech.NOUN, 100.0)
    private val capital_cat = Word("CAT", "CAT", PartOfSpeech.NOUN, 100.0)

    private val unknown = Word("zzz", "zzz", PartOfSpeech.UNKNOWN, 100.0)
    private val quark = Word("quark", "quark", PartOfSpeech.NOUN, 100.0)

    private val tail = Word("a tail", "a tail", PartOfSpeech.UNKNOWN, 100.0)
    private val whiskers = Word("whiskers", "whiskers", PartOfSpeech.UNKNOWN, 100.0)
    private val cave = Word("a cave", "a cave", PartOfSpeech.UNKNOWN, 100.0)
    private val head = Word("a head", "a head", PartOfSpeech.UNKNOWN, 100.0)
    private val cattery = Word("a cattery", "a cattery", PartOfSpeech.UNKNOWN, 100.0)
    private val feline = Word("a feline", "a feline", PartOfSpeech.UNKNOWN, 100.0)
    private val mammal = Word("a mammal", "a mammal", PartOfSpeech.UNKNOWN, 100.0)
    private val lion = Word("a lion", "a lion", PartOfSpeech.UNKNOWN, 100.0)
    private val fox = Word("a fox", "a fox", PartOfSpeech.UNKNOWN, 100.0)
    private val meows = Word("meows", "meows", PartOfSpeech.UNKNOWN, 100.0)
    private val meowing = Word("a meowing", "a meowing", PartOfSpeech.UNKNOWN, 100.0)
    private val feed = Word("feed", "feed", PartOfSpeech.UNKNOWN, 100.0)
    private val fed = Word("a fed", "a fed", PartOfSpeech.UNKNOWN, 100.0)


    /**
     * Tests that getWord can return a word that is in't the dictionary.
     */
    @Test
    fun testGetWord_doesntExist(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"))
        val result = repo.getWord("ZZZ")
        assertEquals(null, result)
    }

    /**
     * Tests that getWord can return a word that is the only word in the dictionary.
     */
    @Test
    fun testGetWord_exists_onlyWord(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"))
        val result = repo.getWord("cat")
        assertEquals(cat, result)
    }

    /**
     * Tests that getWord ignores case when looking for words.
     */
    @Test
    fun testGetWord_caseInsensitive(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"))
        val result = repo.getWord("Cat")
        assertEquals(cat, result)
    }

    /**
     * Tests the getLinguisticSubs method when the word doesn't exist in the dictionary.
     */
    @Test
    fun testGetLinguisticSubs_unknown(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"))
        val result = repo.getLinguisticSubs(unknown)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getLinguisticSubs method when the word has one other word in its linguistic group
     */
    @Test
    fun testGetLinguisticSubs_onlyKnown(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catBat.json"))
        val result = repo.getLinguisticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(LinguisticSubstitution(bat, cat, LinguisticSimilarity.RHYME), result[0])
    }

    /**
     * Tests the getLinguisticSubs method when the word has several other words in its linguistic group
     */
    @Test
    fun testGetLinguisticSubs_multiple(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/ats.json"))
        val result = repo.getLinguisticSubs(cat)
        assertEquals(3, result.size)
        assertEquals(LinguisticSubstitution(bat, cat, LinguisticSimilarity.RHYME), result[0])
        assertEquals(LinguisticSubstitution(hat, cat, LinguisticSimilarity.RHYME), result[1])
        assertEquals(LinguisticSubstitution(rat, cat, LinguisticSimilarity.RHYME), result[2])
    }

    /**
     * Tests the getLinguisticSubs method when the word has one other word in its linguistic group, and there are words
     * not in the same group.
     */
    @Test
    fun testGetLinguisticSubs_ignoreOtherGroups(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catBatOwl.json"))
        val result = repo.getLinguisticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(LinguisticSubstitution(bat, cat, LinguisticSimilarity.RHYME), result[0])
    }

    /**
     * Tests the getLinguisticSubs method will ignore case when looking for substitutions
     */
    @Test
    fun testGetLinguisticSubs_caseInsensitive(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catBat.json"))
        val result = repo.getLinguisticSubs(capital_cat)
        assertEquals(1, result.size)
        assertEquals(LinguisticSubstitution(bat, capital_cat, LinguisticSimilarity.RHYME), result[0])
    }

    /**
     * Tests the getSemanticSubs method on a word that isn't in the dictionary.
     */
    @Test
    fun testGetSemanticSubs_unknown(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"))
        val result = repo.getSemanticSubs(unknown)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getSemanticSubs method on a word that contains has substitutions.
     */
    @Test
    fun testGetSemanticSubs_has_exist(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"),
                                  validRelationships = listOf(Relationship.HAS_A))
        val result = repo.getSemanticSubs(cat)
        assertEquals(2, result.size)
        assertEquals(SemanticSubstitution(tail, cat, Relationship.HAS_A), result[0])
        assertEquals(SemanticSubstitution(whiskers, cat, Relationship.HAS_A), result[1])
    }

    /**
     * Tests the getSemanticSubs method on a word that doesn't contain any has substitutions.
     */
    @Test
    fun testGetSemanticSubs_has_none(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/quark.json"),
                                  validRelationships = listOf(Relationship.HAS_A))
        val result = repo.getSemanticSubs(quark)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getSemanticSubs method on a word that contains in substitutions.
     */
    @Test
    fun testGetSemanticSubs_in_exist(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/in.json"),
                validRelationships = listOf(Relationship.IN))
        val result = repo.getSemanticSubs(bat)
        assertEquals(1, result.size)
        assertEquals(SemanticSubstitution(cave, bat, Relationship.IN), result[0])
    }

    /**
     * Tests the getSemanticSubs method on a word that doesn't contain any in substitutions.
     */
    @Test
    fun testGetSemanticSubs_in_none(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/quark.json"),
                validRelationships = listOf(Relationship.IN))
        val result = repo.getSemanticSubs(quark)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getSemanticSubs method on a word that contains on substitutions.
     */
    @Test
    fun testGetSemanticSubs_on_exist(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/on.json"),
                validRelationships = listOf(Relationship.ON))
        val result = repo.getSemanticSubs(hat)
        assertEquals(1, result.size)
        assertEquals(SemanticSubstitution(head, hat, Relationship.ON), result[0])
    }

    /**
     * Tests the getSemanticSubs method on a word that doesn't contain any on substitutions.
     */
    @Test
    fun testGetSemanticSubs_on_none(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/quark.json"),
                validRelationships = listOf(Relationship.ON))
        val result = repo.getSemanticSubs(quark)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getSemanticSubs method on a word that contains from substitutions.
     */
    @Test
    fun testGetSemanticSubs_from_exist(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/from.json"),
                validRelationships = listOf(Relationship.FROM))
        val result = repo.getSemanticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(SemanticSubstitution(cattery, cat, Relationship.FROM), result[0])
    }

    /**
     * Tests the getSemanticSubs method on a word that doesn't contain any from substitutions.
     */
    @Test
    fun testGetSemanticSubs_from_none(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/quark.json"),
                validRelationships = listOf(Relationship.FROM))
        val result = repo.getSemanticSubs(quark)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getSemanticSubs method on a word that contains synonym substitutions.
     */
    @Test
    fun testGetSemanticSubs_synonym_exist(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"),
                validRelationships = listOf(Relationship.SYNONYM))
        val result = repo.getSemanticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(SemanticSubstitution(feline, cat, Relationship.SYNONYM), result[0])
    }

    /**
     * Tests the getSemanticSubs method on a word that doesn't contain any synonym substitutions.
     */
    @Test
    fun testGetSemanticSubs_synonym_none(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/quark.json"),
                validRelationships = listOf(Relationship.SYNONYM))
        val result = repo.getSemanticSubs(quark)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getSemanticSubs method on a word that contains type substitutions.
     */
    @Test
    fun testGetSemanticSubs_type_exist(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"),
                validRelationships = listOf(Relationship.IS_A))
        val result = repo.getSemanticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(SemanticSubstitution(mammal, cat, Relationship.IS_A), result[0])
    }

    /**
     * Tests the getSemanticSubs method on a word that doesn't contain any type substitutions.
     */
    @Test
    fun testGetSemanticSubs_type_none(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/quark.json"),
                validRelationships = listOf(Relationship.IS_A))
        val result = repo.getSemanticSubs(quark)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getSemanticSubs method on a word that contains supertype substitutions.
     */
    @Test
    fun testGetSemanticSubs_supertype_exist(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"),
                validRelationships = listOf(Relationship.INCLUDES))
        val result = repo.getSemanticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(SemanticSubstitution(lion, cat, Relationship.INCLUDES), result[0])
    }

    /**
     * Tests the getSemanticSubs method on a word that doesn't contain any supertype substitutions.
     */
    @Test
    fun testGetSemanticSubs_supertype_none(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/quark.json"),
                validRelationships = listOf(Relationship.INCLUDES))
        val result = repo.getSemanticSubs(quark)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getSemanticSubs method on a word that contains nearly is substitutions.
     */
    @Test
    fun testGetSemanticSubs_nearly_exist(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"),
                validRelationships = listOf(Relationship.NEAR_SYNONYM))
        val result = repo.getSemanticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(SemanticSubstitution(fox, cat, Relationship.NEAR_SYNONYM), result[0])
    }

    /**
     * Tests the getSemanticSubs method on a word that doesn't contain any nearly is substitutions.
     */
    @Test
    fun testGetSemanticSubs_nearly_none(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/quark.json"),
                validRelationships = listOf(Relationship.NEAR_SYNONYM))
        val result = repo.getSemanticSubs(quark)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }


    /**
     * Tests the getSemanticSubs method on a word that contains property substitutions.
     */
    @Test
    fun testGetSemanticSubs_property_exist(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"),
                validRelationships = listOf(Relationship.PROPERTY))
        val result = repo.getSemanticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(SemanticSubstitution(feline, cat, Relationship.PROPERTY), result[0])
    }

    /**
     * Tests the getSemanticSubs method on a word that doesn't contain any property substitutions.
     */
    @Test
    fun testGetSemanticSubs_property_none(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/quark.json"),
                validRelationships = listOf(Relationship.PROPERTY))
        val result = repo.getSemanticSubs(quark)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getSemanticSubs method on a word that contains acts substitutions.
     */
    @Test
    fun testGetSemanticSubs_acts_exist(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"),
                validRelationships = listOf(Relationship.ACTION))
        val result = repo.getSemanticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(SemanticSubstitution(meows, cat, Relationship.ACTION), result[0])
    }

    /**
     * Tests the getSemanticSubs method on a word that doesn't contain any acts substitutions.
     */
    @Test
    fun testGetSemanticSubs_acts_none(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/quark.json"),
                validRelationships = listOf(Relationship.ACTION))
        val result = repo.getSemanticSubs(quark)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getSemanticSubs method on a word that contains acts_continuous substitutions.
     */
    @Test
    fun testGetSemanticSubs_acts_continuous_exist(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"),
                validRelationships = listOf(Relationship.ACTION_CONTINUOUS))
        val result = repo.getSemanticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(SemanticSubstitution(meowing, cat, Relationship.ACTION_CONTINUOUS), result[0])
    }

    /**
     * Tests the getSemanticSubs method on a word that doesn't contain any acts_continuous substitutions.
     */
    @Test
    fun testGetSemanticSubs_acts_continuous_none(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/quark.json"),
                validRelationships = listOf(Relationship.ACTION_CONTINUOUS))
        val result = repo.getSemanticSubs(quark)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getSemanticSubs method on a word that contains recipient substitutions.
     */
    @Test
    fun testGetSemanticSubs_recipient_exist(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"),
                validRelationships = listOf(Relationship.RECIPIENT_ACTION))
        val result = repo.getSemanticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(SemanticSubstitution(feed, cat, Relationship.RECIPIENT_ACTION), result[0])
    }

    /**
     * Tests the getSemanticSubs method on a word that doesn't contain any recipient substitutions.
     */
    @Test
    fun testGetSemanticSubs_recipient_none(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/quark.json"),
                validRelationships = listOf(Relationship.RECIPIENT_ACTION))
        val result = repo.getSemanticSubs(quark)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getSemanticSubs method on a word that contains recipient_past substitutions.
     */
    @Test
    fun testGetSemanticSubs_recipient_past_exist(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"),
                validRelationships = listOf(Relationship.RECIPIENT_ACTION_PAST))
        val result = repo.getSemanticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(SemanticSubstitution(fed, cat, Relationship.RECIPIENT_ACTION_PAST), result[0])
    }

    /**
     * Tests the getSemanticSubs method on a word that doesn't contain any recipient_past substitutions.
     */
    @Test
    fun testGetSemanticSubs_recipient_past_none(){
        val repo = JsonRepository(this.javaClass.getResourceAsStream("/dictionaries/quark.json"),
                validRelationships = listOf(Relationship.RECIPIENT_ACTION_PAST))
        val result = repo.getSemanticSubs(quark)
        assertEquals(emptyList<LinguisticSubstitution>(), result)
    }

    /**
     * Tests the getSemanticSubs method will ignore case.
     */
    @Test
    fun testGetSemanticSubs_caseInsensitive(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catOnly.json"),
                validRelationships = listOf(Relationship.HAS_A))
        val result = repo.getSemanticSubs(capital_cat)
        assertEquals(2, result.size)
        assertEquals(SemanticSubstitution(tail, capital_cat, Relationship.HAS_A), result[0])
        assertEquals(SemanticSubstitution(whiskers, capital_cat, Relationship.HAS_A), result[1])
    }

    /**
     * Tests that an adult word can be returned by getWords if showAdult is true.
     */
    @Test
    fun testShowAdult_true_getWords(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catBatOwlTwat.json"),
                                  showAdult = true)
        val result = repo.getWord("twat")
        assertEquals(twat, result)
    }

    /**
     * Tests that an adult word won't be returned by getWords if showAdult is false.
     */
    @Test
    fun testShowAdult_false_getWords(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catBatOwlTwat.json"),
                showAdult = false)
        val result = repo.getWord("twat")
        assertEquals(null, result)
    }

    /**
     * Tests that an adult word can be returned by getLinguisticSubs if showAdult is true.
     */
    @Test
    fun testShowAdult_true_getLinguisticSubs(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catBatOwlTwat.json"),
                showAdult = true)
        val result = repo.getLinguisticSubs(cat)
        assertEquals(2, result.size)
        assertEquals(LinguisticSubstitution(bat, cat, LinguisticSimilarity.RHYME), result[0])
        assertEquals(LinguisticSubstitution(twat, cat, LinguisticSimilarity.RHYME), result[1])
    }

    /**
     * Tests that an adult word won't be returned by getLinguisticSubs if showAdult is false.
     */
    @Test
    fun testShowAdult_false_getLinguisticSubs(){
        val repo = JsonRepository(jsonSource = this.javaClass.getResourceAsStream("/dictionaries/catBatOwlTwat.json"),
                showAdult = false)
        val result = repo.getLinguisticSubs(cat)
        assertEquals(1, result.size)
        assertEquals(LinguisticSubstitution(bat, cat, LinguisticSimilarity.RHYME), result[0])
    }

}