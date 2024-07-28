package com.av.readinlangs

import com.av.readlangs.GoogleApiKey
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class GoogleAPITranslationProvider() : ITranslationProvider {

    override fun requestTranslation(word: String) {
        Thread {
            var translation: String? = null

            val googleURL = "https://translation.googleapis.com/language/translate/v2?"

            //build the query:
            val query =
                googleURL + "key=" + GoogleApiKey.key + "&source=" + App.sourceLanguage + "&target=" + App.targetLanguage + "&q=" + word

            val url = URL(query)
            val connection = url.openConnection() as HttpURLConnection

            try {
                // Set up the request
                connection.requestMethod = "GET"

                // Get the response code
                val responseCode = connection.responseCode

                // Read the response
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    var line: String?
                    val response = StringBuilder()

                    while (reader.readLine().also { line = it } != null) {
                        response.append(line)
                    }

                    reader.close()

                    //retrieve the translation from the result:

                    try {
                        val jsonObject = JSONObject(response.toString())
                        val data = jsonObject.getJSONObject("data")
                        val translations = data.getJSONArray("translations")
                        val translationObject = translations.getJSONObject(0)
                        translation = translationObject.getString("translatedText")
                    } catch (e: JSONException) {
                    }
                } else {
                    println("Error: ${connection.responseMessage}")
                }
            }
            catch (e:IOException){
                e.printStackTrace()
            } finally {
                connection.disconnect()

                //send result to receiver(s):
                for (receiver in receivers) {
                    receiver.onTranslationReceived(word, translation)
                }
            }
        }.start()
    }

    override fun registerForTranslationReceiving(receiver: ITranslationProvider.OnTranslationReceived) {
        receivers.add(receiver)
    }

    val receivers = mutableListOf<ITranslationProvider.OnTranslationReceived>()
}