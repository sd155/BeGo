package io.github.sd155.bego.history.platform.internal

import android.content.Context
import io.github.sd155.bego.history.api.FinishedRun
import io.github.sd155.bego.history.api.HistoryRepository
import java.io.File
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

internal class AndroidHistoryRepository(
    context: Context,
) : HistoryRepository {
    private val _fileName = "history.json"
    private val _json = Json {
        ignoreUnknownKeys = true
    }
    private val _serializer = ListSerializer(FinishedRun.serializer())
    private val _mutex = Mutex()
    private val _storageFile = File(context.applicationContext.filesDir, _fileName)

    override suspend fun save(run: FinishedRun) {
        _mutex.withLock {
            val savedRuns = readAll()
                .filterNot { savedRun -> savedRun.id == run.id }
                .plus(run)
                .sortedByDescending(FinishedRun::finishedAtEpochMs)
            writeAll(savedRuns)
        }
    }

    override suspend fun get(id: String): FinishedRun? =
        _mutex.withLock {
            readAll().firstOrNull { run -> run.id == id }
        }

    override suspend fun list(): List<FinishedRun> =
        _mutex.withLock {
            readAll()
        }

    private fun readAll(): List<FinishedRun> {
        if (!_storageFile.exists()) return emptyList()
        val payload = _storageFile.readText()
        if (payload.isBlank()) return emptyList()
        return _json.decodeFromString(_serializer, payload)
            .sortedByDescending(FinishedRun::finishedAtEpochMs)
    }

    private fun writeAll(runs: List<FinishedRun>) {
        _storageFile.writeText(_json.encodeToString(_serializer, runs))
    }
}
