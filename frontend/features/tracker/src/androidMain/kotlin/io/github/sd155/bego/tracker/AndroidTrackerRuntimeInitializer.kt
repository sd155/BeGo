package io.github.sd155.bego.tracker

import android.content.Context
import io.github.sd155.bego.di.Inject
import io.github.sd155.bego.tracker.app.trackerModuleName
import io.github.sd155.bego.tracker.domain.Tracker
import io.github.sd155.logs.api.Logger

private var runtime: AndroidTrackerRuntime? = null

fun initializeAndroidTrackerRuntime(
    context: Context,
) {
    if (runtime == null) {
        runtime = AndroidTrackerRuntime(
            context = context,
            tracker = Inject.instance<Tracker>(),
            logger = Inject.instance<Logger>(tag = trackerModuleName),
        )
    }
}
