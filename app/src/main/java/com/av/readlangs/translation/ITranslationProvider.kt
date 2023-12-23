package com.av.readinlangs

interface ITranslationProvider {

    fun requestTranslation(word: String)
    fun registerForTranslationReceiving(receiver: OnTranslationReceived)

    interface OnTranslationReceived {
        fun onTranslationReceived(origin: String, translation: String?)
    }
}