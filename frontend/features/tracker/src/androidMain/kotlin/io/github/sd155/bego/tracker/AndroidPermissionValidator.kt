package io.github.sd155.bego.tracker

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import io.github.sd155.logs.api.Logger
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AndroidPermissionValidator(
    private val logger: Logger? = null,
) {
    private var _activityResultLauncher: ActivityResultLauncher<Array<String>>? = null
    private var _request: PermissionRequest? = null

    fun setup(activity: ComponentActivity) {
        _activityResultLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            ::onActivityCallback
        )
    }

    private fun onActivityCallback(results: Map<String, Boolean>) =
        _request?.onResponse(results)
            ?: run { logger?.warn(event = "Called onActivityCallback with no request!") }

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

    suspend fun checkAndRequest(
        activity: ComponentActivity,
        permissions: Array<String>,
    ): Boolean =
        permissions
            .filter { permission -> checkNotGranted(activity, permission) }
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
        val launcher = _activityResultLauncher
            ?: run {
                logger?.error(event = "Permissions request failed due to unset ActivityResultLauncher!")
                return false
            }
        return PermissionRequest(permissions, launcher, logger)
            .let { request ->
                _request = request
                request.launch()
            }
    }
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