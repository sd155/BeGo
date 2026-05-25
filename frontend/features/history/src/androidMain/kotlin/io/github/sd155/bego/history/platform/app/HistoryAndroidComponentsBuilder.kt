package io.github.sd155.bego.history.platform.app

import android.content.Context
import io.github.sd155.bego.history.app.SessionRepository
import io.github.sd155.bego.history.platform.internal.AndroidSessionRepository
import io.github.sd155.logs.api.Logger

/**
 * Android-specific wiring helper for the history module.
 */
class HistoryAndroidComponentsBuilder {

    /**
     * Creates the Android implementation of [SessionRepository].
     */
    fun createRepository(
        applicationContext: Context,
        logger: Logger,
    ): SessionRepository =
        AndroidSessionRepository(
            applicationContext = applicationContext,
            logger = logger,
        )
}
