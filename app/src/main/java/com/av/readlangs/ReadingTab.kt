package com.av.readlangs

import com.av.readinlangs.App
import com.av.readinlangs.FileUtils
import java.io.File
import kotlin.concurrent.thread

class ReadingTab(val name: String) {
    companion object {
        val tabDirectoryPath = App.appContext.filesDir.absolutePath + "/tabs"
        fun listTabs(): List<String> {
            val tabsDirectory = File(tabDirectoryPath)
            if (!tabsDirectory.exists()) {
                tabsDirectory.mkdir()
            }
            val files = tabsDirectory.listFiles()

            val tabs = mutableListOf<String>()
            for (tab in files) {
                tabs.add(tab.name)
            }
            return tabs
        }
    }

    lateinit var path: String

    init {
        path = "$tabDirectoryPath/$name"
    }

    var text: String
        get() {
            var result = FileUtils.readFile("$path/text")
            if (result == null) {
                result = ""
            }
            return result
        }
        set(value) {
            thread {
                FileUtils.writeFile("$path/text", value)
            }
        }

    var scroll: Int
        get() {
            var savedValue = FileUtils.readFile("$path/scroll")
            if (savedValue != null) {
                val result = savedValue.toIntOrNull()
                if (result != null) {
                    return result
                }
            }
            return 0
        }
        set(value) {
            FileUtils.writeFile("$path/scroll", value.toString())
        }

    var sourceLanguage: String
        get() {
            var sl = FileUtils.readFile("$path/source-language")
            if (sl == null) {
                sl = App.sourceLanguage
            }
            return sl
        }
        set(value) {
            FileUtils.writeFile("$path/source-language", value)
        }

    var targetLanguage: String
        get() {
            var tl = FileUtils.readFile("$path/target-language")
            if (tl == null) {
                tl = App.targetLanguage
            }
            return tl
        }
        set(value) {
            FileUtils.writeFile("$path/target-language", value)
        }
}