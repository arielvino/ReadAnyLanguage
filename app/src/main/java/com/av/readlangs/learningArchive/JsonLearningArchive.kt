package com.av.readlangs.learningArchive

import com.av.readinlangs.App
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

object JsonLearningArchive : ILearningArchive {
    private fun archiveFilePath() :String {
        return App.appContext.filesDir.absolutePath + "/" + "Archives" + "/" + App.sourceLanguage + "-" + App.targetLanguage + ".json"
    }

    override fun saveWord(wordItem: WordItem) {
        val list = getWords()
        val mutableList: MutableList<WordItem> = list?.toMutableList() ?: mutableListOf()

        //check if the word already exist:
        var exist = false
        for (word in mutableList) {
            if (word.word.contentEquals(wordItem.word)) {
                exist = true
            }
        }

        if (!exist) {
            mutableList.add(wordItem)
            saveListToJson(mutableList)
        }
    }

    override fun editWord(word: String, translation: String, explanation: String?) {
        deleteWord(word)
        saveWord(WordItem(word, translation, explanation))
    }

    override fun deleteWord(word: String) {
        val list = getWords()

        if (list != null) {
            val mutableList = list.toMutableList()

            //remove word:
            mutableList.removeIf {
                it.word.contentEquals(word)
            }

            //save new list:
            saveListToJson(mutableList)
        }
    }

    override fun getWords(): List<WordItem>? {
        return retrieveListFromJson()
    }

    private fun saveListToJson(list: List<WordItem>) {
        val jsonArray = JSONArray()

        for (obj in list) {
            val jsonObject = JSONObject()
            jsonObject.put("word", obj.word)
            jsonObject.put("translation", obj.translation)
            if (obj.explanation != null) {
                jsonObject.put("explanation", obj.explanation)
            }
            jsonArray.put(jsonObject)
        }

        // Save JSON to a file:
        val file = File(archiveFilePath())
        file.parentFile.mkdirs()
        file.writeText(jsonArray.toString())
    }

    private fun retrieveListFromJson(): List<WordItem>? {
        val file = File(archiveFilePath())

        if (!file.exists()) {
            return null
        }

        val json = file.readText()
        val jsonArray = JSONArray(json)

        val resultList = mutableListOf<WordItem>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val word = jsonObject.getString("word")
            val translation = jsonObject.getString("translation")
            var explanation: String? = null
            if (jsonObject.has("explanation")) {
                explanation = jsonObject.getString("explanation")
            }
            resultList.add(WordItem(word, translation, explanation))
        }

        return resultList
    }
}