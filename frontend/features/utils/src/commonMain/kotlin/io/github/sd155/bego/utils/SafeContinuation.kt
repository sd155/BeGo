package io.github.sd155.bego.utils

import kotlin.Result
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resumeWithException

/**
 * A thread-safe, atomic wrapper for a single [Continuation] instance.
 *
 * This utility is designed to safely manage the lifecycle of a coroutine continuation in asynchronous or callback-based code,
 * preventing double-resume bugs and memory leaks. It ensures that only one continuation is stored at a time, and provides
 * hooks for handling cases where a previous continuation was not resumed or when a continuation is resumed more than once.
 *
 * Typical usage is to store a continuation when starting an async operation, then resume it from a callback or event.
 * If a new continuation is stored before the previous one is resumed, the previous one is resumed with an error.
 *
 * @param T The type of the result value.
 * @param onAlreadyResumed Called if [resumeWith] is invoked when no continuation is stored (already resumed or never set).
 * @param onNotResumed Called if a previous continuation is replaced or closed without being resumed. By default, resumes with [IllegalStateException].
 *
 * Example:
 * ```kotlin
 * val safe = SafeContinuation<Result<String, Unit>>()
 * safe.store(continuation)
 * // ... later, from a callback:
 * safe.resume(Result.success(Unit))
 * // or, to clean up:
 * safe.close()
 * ```
 *
 * @see Continuation
 */
@OptIn(ExperimentalAtomicApi::class)
class SafeContinuation<T>(
    private val onAlreadyResumed: () -> Unit = {},
    private val onNotResumed: (Continuation<T>) -> Unit = { it.resumeWithException(IllegalStateException("Pending continuation is not resumed!")) },
) : Continuation<T> {
    private val _continuation = AtomicReference<Continuation<T>?>(value = null)

    /**
     * The [CoroutineContext] of the currently stored continuation, or throws if none is set.
     *
     * @throws IllegalStateException if no continuation is set or it has already been resumed.
     */
    override val context: CoroutineContext
        get() = _continuation.load()?.context
            ?: throw IllegalStateException("Continuation not set, or already resumed!")

    /**
     * Store a new [Continuation]. If a previous continuation was stored and not resumed, it will be resumed with an error
     * (by calling [onNotResumed]).
     *
     * @param continuation The continuation to store.
     * @return The previous continuation, if any (already handled by [onNotResumed]).
     */
    fun store(continuation: Continuation<T>) =
        _continuation.exchange(newValue = continuation)
            ?.let(onNotResumed)

    /**
     * Resume and clear the stored continuation with the given [result].
     * If no continuation is stored, calls [onAlreadyResumed].
     *
     * @param result The result to resume the continuation with.
     */
    override fun resumeWith(result: Result<T>) =
        _continuation.exchange(newValue = null)
            ?.resumeWith(result)
            ?: onAlreadyResumed()

    /**
     * Close the SafeContinuation, resuming any stored continuation with an error (by calling [onNotResumed]).
     * This is useful for cleanup if the continuation will never be resumed (e.g., on cancellation or shutdown).
     */
    fun close() =
        _continuation.load()
            ?.let(onNotResumed)
}