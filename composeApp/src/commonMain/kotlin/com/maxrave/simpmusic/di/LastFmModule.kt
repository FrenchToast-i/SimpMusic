package com.maxrave.simpmusic.di

import com.maxrave.data.service.lastfm.LastFmService
import org.koin.dsl.module

val lastFmModule =
    module {
        single {
            LastFmService(
                httpClient = get(),
                apiKey = com.maxrave.common.Config.LAST_FM_API_KEY,
                apiSecret = com.maxrave.common.Config.LAST_FM_API_SECRET,
            )
        }
    }
