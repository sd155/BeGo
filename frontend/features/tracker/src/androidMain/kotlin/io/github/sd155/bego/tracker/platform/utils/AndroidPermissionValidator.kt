package io.github.sd155.bego.tracker.platform.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import io.github.sd155.logs.api.Logger
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Helper for checking and requesting Android runtime location permissions.
 * Handles permission dialogs and result callbacks.
 */
@OptIn(ExperimentalAtomicApi::class)
internal class AndroidPermissionValidator(
    private val activityResultLauncher: ActivityResultLauncher<Array<String>>,
    private val logger: Logger? = null,
) {
    private val requestCoordinator = PermissionRequestCoordinator(logger = logger)

    fun onActivityCallback(results: Map<String, Boolean>) =
        requestCoordinator.onActivityCallback(results)

    /**
     * Checks if all given permissions are granted in the given context.
     */
    fun check(
        context: Context,
        permissions: Array<String>,
    ): Boolean {
        permissions.forEach { permission ->
            if (checkNotGranted(context, permission))
                return false
        }
        return true
    }

    /**
     * Checks and requests any missing permissions from the user.
     * Suspends until the user responds.
     */
    suspend fun checkAndRequest(
        context: Context,
        permissions: Array<String>,
    ): Boolean =
        permissions
            .filter { permission -> checkNotGranted(context, permission) }
            .toTypedArray()
            .let { notGrantedPermissions -> request(notGrantedPermissions) }

    private fun checkNotGranted(context: Context, permission: String): Boolean {
        val checkResult = ActivityCompat.checkSelfPermission(context, permission)
        val notGranted = checkResult != PackageManager.PERMISSION_GRANTED
        if (notGranted)
            logger?.info(event = "Permission `$permission` is not granted.")
        return notGranted
    }

    private suspend inline fun request(permissions: Array<String>): Boolean {
        return PermissionRequest(permissions, activityResultLauncher, logger)
            .let { request ->
                requestCoordinator.store(request)
                request.launch()
            }
    }
}

@OptIn(ExperimentalAtomicApi::class)
private class PermissionRequestCoordinator(
    private val logger: Logger? = null,
) {
    private val request = AtomicReference<PermissionRequest?>(value = null)

    fun store(permissionRequest: PermissionRequest) {
        request.store(permissionRequest)
    }

    fun onActivityCallback(results: Map<String, Boolean>) =
        request.load()?.onResponse(results)
            ?: run { logger?.warn(event = "Called onActivityCallback with no request!") }
}

private class PermissionRequest(
    val permissions: Array<String>,
    val launcher: ActivityResultLauncher<Array<String>>,
    val logger: Logger? = null,
) {
    var continuation: Continuation<Boolean>? = null

    suspend fun launch(): Boolean =
        suspendCoroutine { continuation ->
            this.continuation = continuation
            launcher.launch(permissions)
        }

    fun onResponse(results: Map<String, Boolean>) {
        this.continuation?.let { continuation ->
            permissions
                .filter { permission ->
                    val denied = results[permission] == false
                    if (denied)
                        logger?.info(event = "User denied permission `$permission`")
                    else
                        logger?.info(event = "User granted permission `$permission`")
                    denied
                }
                .also { deniedPermissions ->
                    this.continuation = null
                    continuation.resume(deniedPermissions.isEmpty())
                }
        }
            ?: run {
                logger?.warn(event = "Failed to handle permission response due to used continuation")
            }
    }
}
