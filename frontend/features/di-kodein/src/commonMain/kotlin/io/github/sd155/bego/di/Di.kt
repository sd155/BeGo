package io.github.sd155.bego.di

import org.kodein.di.DI
import org.kodein.di.DirectDI
import org.kodein.di.bind
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.singleton

/**
 * Android-facing contract for framework-created entry points that need access to the shared root DI tree.
 */
interface DiTreeHolder {
    val diTree: DiTree
}

/**
 * Thin wrapper over the underlying DI engine.
 *
 * Feature modules depend on these abstractions instead of importing Kodein types directly.
 */
class DiTree @PublishedApi internal constructor(
    private val _delegate: DirectDI,
) {
    @PublishedApi
    internal val delegate: DirectDI
        get() = _delegate

    inline fun <reified T> instance(tag: Any? = null): T {
        return delegate.instance(tag)
    }
}

class DiModule internal constructor(
    internal val delegate: DI.Module,
)

class DiModuleBuilder internal constructor() {
    @PublishedApi
    internal val registrations = mutableListOf<DI.Builder.() -> Unit>()

    internal fun build(name: String): DiModule =
        DiModule(
            DI.Module(name = name) {
                registrations.forEach { registration -> registration(this) }
            }
        )

    inline fun <reified T : Any> bindSingleton(
        tag: Any? = null,
        noinline provider: DiTree.() -> T,
    ) {
        registrations += {
            bind<T>(tag = tag) with singleton { provider(DiTree(this)) }
        }
    }
}

class DiTreeBuilder internal constructor() {
    private val _modules = mutableListOf<DiModule>()

    fun importAll(vararg modules: DiModule) {
        _modules += modules
    }

    internal fun build(): DiTree =
        DiTree(
            DI {
                importAll(*_modules.map { module -> module.delegate }.toTypedArray())
            }.direct
        )
}

fun diModule(
    name: String,
    init: DiModuleBuilder.() -> Unit,
): DiModule = DiModuleBuilder()
    .apply(init)
    .build(name)

fun diTree(
    init: DiTreeBuilder.() -> Unit,
): DiTree = DiTreeBuilder()
    .apply(init)
    .build()
