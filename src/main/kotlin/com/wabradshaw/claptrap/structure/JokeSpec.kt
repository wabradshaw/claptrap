package com.wabradshaw.claptrap.structure

data class JokeSpec (val nucleus : String,
                     val nucleusWord : Word?,
                     val primarySetup : SemanticSubstitution?,
                     val secondarySetup : SemanticSubstitution,
                     val linguisticSub : LinguisticSubstitution)