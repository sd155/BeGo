package io.github.sd155.bego.history.app

import io.github.sd155.bego.di.diTree
import io.github.sd155.bego.tracker.api.RunSessionPoint
import io.github.sd155.logs.api.Logger
import kotlin.test.Test
import kotlin.test.assertSame

class HistoryModuleTest {
    @Test
    fun moduleBindsSessionPointConsumerToRepositoryInstanceTest() {
        val repository = TestSessionRepository()
        val diTree = diTree {
            importAll(
                historyModule(
                    loggerBuilder = { TestLogger() },
                    repositoryBuilder = { repository },
                )
            )
        }

        val resolvedRepository = diTree.instance<SessionRepository>()
        val resolvedSaver = diTree.instance<SessionPointConsumer>()

        assertSame(repository, resolvedRepository)
        assertSame(repository, resolvedSaver)
    }

    private class TestSessionRepository : SessionRepository() {
        override fun consume(sessionPoint: RunSessionPoint) = Unit
    }

    private class TestLogger : Logger {
        override fun trace(event: String, diagnostics: List<Any>) = Unit
        override fun debug(event: String, diagnostics: List<Any>) = Unit
        override fun info(event: String, diagnostics: List<Any>) = Unit
        override fun warn(event: String, e: Throwable?, diagnostics: List<Any>) = Unit
        override fun error(event: String, e: Throwable?, diagnostics: List<Any>) = Unit
        override fun fatal(event: String, e: Throwable?, diagnostics: List<Any>) = Unit
    }
}
