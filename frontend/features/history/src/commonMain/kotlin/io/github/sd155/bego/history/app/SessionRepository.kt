package io.github.sd155.bego.history.app

import io.github.sd155.bego.tracker.api.RunSessionPoint
import io.github.sd155.bego.utils.Result

/**
 * Stores tracker-emitted session points for later history processing.
 */
abstract class SessionRepository {
    internal abstract suspend fun save(sessionPoint: RunSessionPoint): Result<SessionRepositoryFailure, Unit>
}

/**
 * Generic repository failure returned when session-point persistence fails.
 */
internal object SessionRepositoryFailure
