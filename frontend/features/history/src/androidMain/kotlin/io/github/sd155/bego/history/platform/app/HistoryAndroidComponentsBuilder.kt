package io.github.sd155.bego.history.platform.app

import android.content.Context
import io.github.sd155.bego.history.api.HistoryRepository
import io.github.sd155.bego.history.platform.internal.AndroidHistoryRepository

class HistoryAndroidComponentsBuilder {

    fun createRepository(
        applicationContext: Context,
    ): HistoryRepository =
        AndroidHistoryRepository(applicationContext)
}
