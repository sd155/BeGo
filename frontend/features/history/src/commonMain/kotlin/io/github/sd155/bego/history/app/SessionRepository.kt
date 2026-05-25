package io.github.sd155.bego.history.app

import io.github.sd155.bego.tracker.api.RunSessionPoint

/**
 * Public write-only bridge used by app wiring to pass tracker session points into history.
 */
fun interface SessionPointConsumer {
    /**
     * Accepts a tracker-emitted session point for asynchronous persistence inside the history module.
     */
    fun consume(sessionPoint: RunSessionPoint)
}

/**
 * Stores tracker-emitted session points for later history processing.
 */
abstract class SessionRepository : SessionPointConsumer

/**
 * Generic repository failure returned when session-point persistence fails.
 */
internal object SessionRepositoryFailure
