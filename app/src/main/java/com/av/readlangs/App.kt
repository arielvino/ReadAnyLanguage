package com.av.readinlangs

import android.app.Application
import android.content.Context
import com.av.readlangs.MyKey
import com.av.readlangs.learningArchive.ILearningArchive
import com.av.readlangs.learningArchive.JsonLearningArchive

class App : Application() {
    companion object {
        lateinit var appContext: Context
        var sourceLanguage = "de"
        var targetLanguage = "en"
        lateinit var translator: ITranslationProvider
        lateinit var archive: ILearningArchive
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        translator = HybridCashGoogleAPITranslationProvider(MyKey.key)
        archive = JsonLearningArchive
    }
}