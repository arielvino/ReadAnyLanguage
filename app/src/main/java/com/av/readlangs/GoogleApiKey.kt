package com.av.readlangs

import com.av.readinlangs.App
import com.av.readinlangs.FileUtils
import java.io.File

object GoogleApiKey {
    private val path = App.appContext.filesDir.absolutePath + "/google-api-key"

    var key: String?
        get() {
            return FileUtils.readFile(path)
        }
        set(value) {
            if (value == null) {
                File(path).delete()
            } else {
                FileUtils.writeFile(path, value)
            }
        }
}