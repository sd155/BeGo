package io.github.sd155.bego.history.api

interface HistoryRepository {
    suspend fun save(run: FinishedRun)

    suspend fun get(id: String): FinishedRun?

    /**
     * Returns completed runs ordered from newest to oldest.
     */
    suspend fun list(): List<FinishedRun>
}