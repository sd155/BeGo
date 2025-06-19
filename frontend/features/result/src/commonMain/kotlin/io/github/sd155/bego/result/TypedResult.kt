package io.github.sd155.bego.result

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed class Result<out F, out S> private constructor() {
    data class Success<T>(val value: T) : Result<Nothing, T>()
    data class Failure<T>(val error: T) : Result<T, Nothing>()

    fun withSuccess(block: (S) -> Unit): Result<F, S> {
        if (this is Success)
            block(value)
        return this
    }
}

@OptIn(ExperimentalContracts::class)
suspend fun <F, S, R> Result<F, S>.fold(
    onSuccess: suspend (value: S) -> R,
    onFailure: suspend (error: F) -> R
): R {
    contract {
        callsInPlace(onSuccess, InvocationKind.AT_MOST_ONCE)
        callsInPlace(onFailure, InvocationKind.AT_MOST_ONCE)
    }
    return when (this) {
        is Result.Success -> onSuccess(value)
        is Result.Failure -> onFailure(error)
    }
}

fun <T> T.asSuccess() = Result.Success(this)

fun <T> T.asFailure() = Result.Failure(this)

@OptIn(ExperimentalContracts::class)
suspend fun <F, S, R> Result<F, S>.next(block: suspend (S) -> Result<F, R>): Result<F, R> {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return  when(this) {
        is Result.Success -> block(value)
        is Result.Failure -> error.asFailure()
    }
}
