package com.majed.currencyapp.anagrams

class AnagramsChecker(private val str1: String, private val str2: String) {

    fun check(): String {
        val isAnagram = str1.toCharArray().sorted().toCharArray()
            .contentEquals(str2.toCharArray().sorted().toCharArray())

        return if (isAnagram)
            "Strings are an anagrams of each other"
        else
            "Strings are not Anagrams of each other"
    }
}