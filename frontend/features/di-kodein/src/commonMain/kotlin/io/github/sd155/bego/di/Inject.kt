package io.github.sd155.bego.di

import org.kodein.di.DirectDI
import org.kodein.di.instance

/**
 * Singleton object for managing dependency injection using Kodein DI in the BeGo frontend.
 *
 * Provides a simple API to initialize and access dependencies throughout the application.
 */
object Inject {

    private var _di: DirectDI? = null

    /**
     * The [DirectDI] instance used for dependency resolution.
     *
     * @throws IllegalStateException if dependencies have not been initialized.
     */
    val di: DirectDI
        get() = requireNotNull(_di)

    /**
     * Initializes the dependency injection tree.
     *
     * @param tree The [DirectDI] tree to use for dependency resolution.
     */
    fun createDependencies(tree: DirectDI) {
        _di = tree
    }

    /**
     * Retrieves an instance of the specified type [T] from the DI container.
     *
     * @return An instance of type [T].
     * @throws IllegalStateException if dependencies have not been initialized.
     */
    inline fun <reified T> instance(): T {
        return di.instance()
    }
}