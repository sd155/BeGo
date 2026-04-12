package io.github.sd155.bego.tracker.app

import androidx.compose.runtime.Composable
import io.github.sd155.bego.di.diTree
import io.github.sd155.bego.tracker.domain.LocationError
import io.github.sd155.bego.tracker.domain.PlatformReason
import io.github.sd155.bego.tracker.domain.Tracker
import io.github.sd155.bego.utils.Result
import io.github.sd155.logs.api.Logger
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class TrackerModuleTest {
    @Test
    fun moduleBindingSmokeTest() {
        val createdSources = mutableListOf<String>()
        lateinit var locationProviderLogger: Logger
        lateinit var hooksLogger: Logger
        val locationProvider = TestLocationProvider()
        val hooks = TestPlatformHooks()

        val diTree = diTree {
            importAll(
                trackerModule(
                    loggerBuilder = { source ->
                        createdSources += source
                        TestLogger(source = source)
                    },
                    locationProviderBuilder = { logger ->
                        locationProviderLogger = logger
                        locationProvider
                    },
                    platformHooksBuilder = { logger ->
                        hooksLogger = logger
                        hooks
                    },
                    platformBinding = { components ->
                        bindSingleton {
                            TrackerPlatformBindingProbe(
                                logger = components.logger,
                                tracker = components.tracker,
                            )
                        }
                    }
                )
            )
        }

        val resolvedLogger = diTree.instance<Logger>(tag = trackerModuleName)
        val resolvedLocationProvider = diTree.instance<LocationProvider>()
        val resolvedHooks = diTree.instance<PlatformHooks>()
        val resolvedTracker = diTree.instance<Tracker>()
        val resolvedProbe = diTree.instance<TrackerPlatformBindingProbe>()

        assertEquals(listOf("Tracker"), createdSources)
        assertSame(resolvedLogger, locationProviderLogger)
        assertSame(resolvedLogger, hooksLogger)
        assertSame(locationProvider, resolvedLocationProvider)
        assertSame(hooks, resolvedHooks)
        assertSame(resolvedLogger, resolvedProbe.logger)
        assertSame(resolvedTracker, resolvedProbe.tracker)
    }

    private data class TrackerPlatformBindingProbe(
        val logger: Logger,
        val tracker: Tracker,
    )

    private class TestLocationProvider : LocationProvider() {
        override suspend fun sub(onUpdate: (io.github.sd155.bego.tracker.domain.TrackPoint) -> Unit): Result<LocationError, Unit> {
            return Result.Success(Unit)
        }

        override fun unsub() = Unit
    }

    private class TestPlatformHooks : PlatformHooks() {
        @Composable
        override fun rememberLocationPrerequisites(): LocationPrerequisites = error("unused in DI test")

        @Composable
        override fun PlatformNotReadyView(reason: PlatformReason, onRetryInitialization: () -> Unit) = Unit
    }

    private class TestLogger(
        private val source: String,
    ) : Logger {
        override fun trace(event: String, diagnostics: List<Any>) = Unit
        override fun debug(event: String, diagnostics: List<Any>) = Unit
        override fun info(event: String, diagnostics: List<Any>) = Unit
        override fun warn(event: String, e: Throwable?, diagnostics: List<Any>) = Unit
        override fun error(event: String, e: Throwable?, diagnostics: List<Any>) = Unit
        override fun fatal(event: String, e: Throwable?, diagnostics: List<Any>) = Unit

        override fun toString(): String = "TestLogger(source=$source)"
    }
}
