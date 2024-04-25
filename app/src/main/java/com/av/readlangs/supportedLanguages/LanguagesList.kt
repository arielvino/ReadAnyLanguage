package com.av.readlangs.supportedLanguages

import android.content.Context
import com.av.readinlangs.App

object LanguagesList : ISupportedLanguagesList {
    override fun getSupportedLanguages(): List<LanguageItem> {
        val content = App.appContext.assets.open("supported_languages").use {
            it.reader().readLines()
        }
        val languagesList: MutableList<LanguageItem> = mutableListOf()

        for (line in content) {
            languagesList.add(
                LanguageItem(
                    line.substring(0, line.lastIndexOf(" ")),
                    line.substring(line.lastIndexOf(" ") + 1)
                )
            )
        }

        return languagesList
    }

    private fun Context.readAssetFile(fileName: String): String {
        return assets.open(fileName).use {
            it.reader().readText()
        }
    }
}