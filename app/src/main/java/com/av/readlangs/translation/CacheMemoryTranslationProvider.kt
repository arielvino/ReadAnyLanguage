package com.av.readinlangs

import java.io.File

class CacheMemoryTranslationProvider : ITranslationProvider {
    constructor() {
        File(cacheDirPath).mkdir()
    }

    companion object {
        val separator = "="
        val cacheDirPath = App.appContext.cacheDir.absolutePath + "/" + "translated_cache"
    }

    override fun requestTranslation(word: String) {
        val cacheItems = HashMap<String, String>()

        //get items from cache memory:
        val lines: List<String>? =
            FileUtils.readLines(cacheDirPath + "/" + App.sourceLanguage + "-" + App.targetLanguage)

        //and store them in a dictionary:
        if (lines != null) {
            for (line in lines) {
                try{
                    val key = line.split(separator)[0]
                    val value = line.split(separator)[1]
                    cacheItems[key] = value
                }
                catch (e:IndexOutOfBoundsException){
                    continue
                }
            }
        }

        //find the requested translation (if available):
        var translation:String? = null
        if (cacheItems.containsKey(word)) {
            translation = cacheItems[word]
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

    fun saveTranslationToCache(word: String, translation: String) {
        if(word.contains(separator)||word.contains("\n")){
            return
        }
        FileUtils.attachLine(
            cacheDirPath + "/" + App.sourceLanguage + "-" + App.targetLanguage,
            word + separator + translation
        )
    }
}