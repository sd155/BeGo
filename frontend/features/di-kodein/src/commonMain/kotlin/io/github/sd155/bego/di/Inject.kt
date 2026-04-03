package io.github.sd155.bego.di

/**
 * Singleton object for managing dependency injection using Kodein DI in the BeGo frontend.
 *
 * Provides a simple API to initialize and access dependencies throughout the application.
 */
object Inject {

    private var _di: DiTree? = null

    /**
     * The [DiTree] instance used for dependency resolution.
     *
     * This property provides access to the configured dependency injection tree.
     * It should only be accessed after dependencies have been initialized.
     *
     * @throws IllegalStateException if dependencies have not been initialized via [createDependencies].
     */
    val di: DiTree
        get() = requireNotNull(_di) {
            "Dependencies are not initialized. Call Inject.createDependencies(...) during app startup."
        }

    /**
     * Initializes the dependency injection tree.
     *
     * This method should be called once during application startup to set up the
     * dependency injection system. It configures the DI tree that will be used
     * for resolving dependencies throughout the application lifecycle.
     *
     * @param tree The [DiTree] to use for dependency resolution.
     * @throws IllegalStateException if dependencies have already been initialized.
     */
    fun createDependencies(tree: DiTree) {
        check(_di == null) {
            "Dependencies are already initialized."
        }
        _di = tree
    }

    /**
     * Retrieves an instance of the specified type [T] from the DI container.
     *
     * This method provides type-safe access to dependencies registered in the DI container.
     * The returned instance is managed by the DI container according to its binding configuration
     * (singleton, factory, etc.).
     *
     * @param tag Optional tag to distinguish between multiple bindings of the same type.
     * @return An instance of type [T].
     * @throws IllegalStateException if dependencies have not been initialized.
     * @throws org.kodein.di.DI.NotFoundException if no binding exists for the requested type and tag.
     */
    inline fun <reified T> instance(tag: Any? = null): T {
        return di.instance(tag)
    }
}
