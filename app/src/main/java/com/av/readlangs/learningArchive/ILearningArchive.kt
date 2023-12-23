package com.av.readlangs.learningArchive

interface ILearningArchive {
    fun saveWord(wordItem: WordItem)
    fun editWord(word: String, translation: String, explanation:String?)
    fun deleteWord(word:String)
    fun getWords():List<WordItem>?

}