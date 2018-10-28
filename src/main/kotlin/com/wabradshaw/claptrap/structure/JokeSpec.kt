package com.wabradshaw.claptrap.structure

data class JokeSpec (val nucleus : Word,
                     val primarySetup : SemanticSubstitution?,
                     val secondarySetup : SemanticSubstitution,
                     val linguisticSub : LinguisticSubstitution)