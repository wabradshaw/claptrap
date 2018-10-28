package com.wabradshaw.claptrap

data class JokeSpec (val nucleus : Word,
                     val primarySetup : SemanticSubstitution,
                     val secondarySetup : SemanticSubstitution,
                     val linguisticSub : LinguisticSubstitution)