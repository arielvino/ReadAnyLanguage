package com.av.readinlangs

import android.app.Application
import android.content.Context
import com.av.readlangs.learningArchive.ILearningArchive
import com.av.readlangs.learningArchive.JsonLearningArchive
import com.av.readlangs.supportedLanguages.ISupportedLanguagesList
import com.av.readlangs.supportedLanguages.LanguagesList

class App : Application() {
    companion object {
        lateinit var appContext: Context
        var sourceLanguage: String
            get() {
                var src: String? =
                    FileUtils.readFile(appContext.filesDir.absolutePath + "/source-language")//read existing value

                //if value doesn't exist:
                if (src == null) {
                    //write default value:
                    src = "de"
                    sourceLanguage = src
                }
                return src
            }
            set(value) {
                FileUtils.writeFile(appContext.filesDir.absolutePath + "/source-language", value)
            }

        var targetLanguage: String
            get() {
                var target: String? =
                    FileUtils.readFile(appContext.filesDir.absolutePath + "/target-language")//read existing value

                //if value doesn't exist:
                if (target == null) {
                    //write default value:
                    target = "en"
                    targetLanguage = target
                }
                return target
            }
            set(value) {
                FileUtils.writeFile(appContext.filesDir.absolutePath + "/target-language", value)
            }

        lateinit var translator: ITranslationProvider
        lateinit var archive: ILearningArchive
        lateinit var supportedLanguagesProvider: ISupportedLanguagesList
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this

        translator = HybridCacheGoogleAPITranslationProvider()

        archive = JsonLearningArchive
        supportedLanguagesProvider = LanguagesList
    }
}