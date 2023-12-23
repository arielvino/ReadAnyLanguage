package com.av.readinlangs

import java.io.File

object FileUtils {
    fun writeFile(path:String, text:String){
        val file = File(path)
        file.createNewFile()

        file.writeText(text)
    }

    fun readFile(path: String): String? {
        val file = File(path)

        return if (file.exists()) {
            file.readText()
        } else {
            null
        }
    }

    fun readLines(path: String): List<String>? {
        val file = File(path)

        return if (file.exists()) {
            file.readLines()
        } else {
            null
        }
    }

    fun writeLines(path: String, lines: List<String>) {
        var text = ""

        //attach all lines:
        for (line in lines) {
            text += line
            text += "\r\n"
        }

        //remove the last linebreak:
        text = text.substring(0, text.lastIndexOf("\r\n"))

        //write to file
        val file = File(path)
        file.createNewFile()
        file.writeText(text)
    }

    fun attachLine(path: String, line: String) {
        val file = File(path)
        file.createNewFile()

        //get existed lines:
        val lines: MutableList<String> = file.readLines().toMutableList()

        //add the new line:
        lines.add(line)

        //write to file:
        writeLines(path, lines)
    }

    fun <T> binarySearch(
        items: List<T>,
        comparator: (a: T, b: T) -> Int,
        start: Int,
        end: Int,
        wanted: T
    ): Pair<Boolean, Int> {
        val check: Int = (start + end) / 2

        //found:
        if (comparator(wanted, items[check]) == 0) {
            return Pair(true, check)
        }

        //todo
        return Pair(false, -1)///////////////////////////
    }
}