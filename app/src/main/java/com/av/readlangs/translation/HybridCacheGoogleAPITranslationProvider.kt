package com.av.readinlangs

class HybridCacheGoogleAPITranslationProvider() : ITranslationProvider {

    val cacheTranslator = CacheMemoryTranslationProvider()
    val googleAPITranslator = GoogleAPITranslationProvider()

    init {
        //when cache provider callback:
        cacheTranslator.registerForTranslationReceiving(object :
            ITranslationProvider.OnTranslationReceived {
            override fun onTranslationReceived(origin: String, translation: String?) {

                //if translation isn't exist in cache - call googleAPI:
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
                //save translation to cache memory:
                if (translation != null) {
                    cacheTranslator.saveTranslationToCache(origin, translation)
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
        cacheTranslator.requestTranslation(word)
    }

    override fun registerForTranslationReceiving(receiver: ITranslationProvider.OnTranslationReceived) {
        receivers.add(receiver)
    }
}