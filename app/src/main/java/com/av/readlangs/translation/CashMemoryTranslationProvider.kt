package com.av.readinlangs

import java.io.File

class CashMemoryTranslationProvider : ITranslationProvider {
    constructor() {
        File(cashDirPath).mkdir()
    }

    companion object {
        val separator = "="
        val cashDirPath = App.appContext.cacheDir.absolutePath + "/" + "translated_cash"
    }

    override fun requestTranslation(word: String) {
        val cashItems = HashMap<String, String>()

        //get items from cash memory:
        val lines: List<String>? =
            FileUtils.readLines(cashDirPath + "/" + App.sourceLanguage + "-" + App.targetLanguage)

        //and store them in a dictionary:
        if (lines != null) {
            for (line in lines) {
                val key = line.split(separator)[0]
                val value = line.split(separator)[1]
                cashItems[key] = value
            }
        }

        //find the requested translation (if available):
        var translation:String? = null
        if (cashItems.containsKey(word)) {
            translation = cashItems[word]
        }

        //send it to the receiver:
        for(receiver in receivers){
            receiver.onTranslationReceived(word, translation)
        }
    }

    override fun registerForTranslationReceiving(receiver: ITranslationProvider.OnTranslationReceived) {
        receivers.add(receiver)
    }

    val receivers = mutableListOf<ITranslationProvider.OnTranslationReceived>()

    fun saveTranslationToCash(word: String, translation: String) {
        FileUtils.attachLine(
            cashDirPath + "/" + App.sourceLanguage + "-" + App.targetLanguage,
            word + separator + translation
        )
    }
}