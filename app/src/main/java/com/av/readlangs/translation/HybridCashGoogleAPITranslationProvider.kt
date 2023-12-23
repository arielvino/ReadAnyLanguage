package com.av.readinlangs

class HybridCashGoogleAPITranslationProvider(val apiKey: String) : ITranslationProvider {

    val cashTranslator = CashMemoryTranslationProvider()
    val googleAPITranslator = GoogleAPITranslationProvider(apiKey)

    init {
        //when cash provider callback:
        cashTranslator.registerForTranslationReceiving(object :
            ITranslationProvider.OnTranslationReceived {
            override fun onTranslationReceived(origin: String, translation: String?) {

                //if translation isn't exist in cash - call googleAPI:
                if (translation == null) {
                    googleAPITranslator.requestTranslation(origin)
                }

                //forward the result:
                else {
                    for (receiver in receivers) {
                        receiver.onTranslationReceived(origin, translation)
                    }
                }
            }
        })

        //when google provider callback:
        googleAPITranslator.registerForTranslationReceiving(object :
            ITranslationProvider.OnTranslationReceived {
            override fun onTranslationReceived(origin: String, translation: String?) {
                //save translation to cash memory:
                if (translation != null) {
                    cashTranslator.saveTranslationToCash(origin, translation)
                }

                //forward the result:
                for (receiver in receivers) {
                    receiver.onTranslationReceived(origin, translation)
                }
            }
        })
    }

    val receivers = mutableListOf<ITranslationProvider.OnTranslationReceived>()
    override fun requestTranslation(word: String) {
        cashTranslator.requestTranslation(word)
    }

    override fun registerForTranslationReceiving(receiver: ITranslationProvider.OnTranslationReceived) {
        receivers.add(receiver)
    }
}