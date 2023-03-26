package mick.studio.itsfuntorun.models

import timber.log.Timber.i

var lastId = 1L

internal fun getId(): Long {
    return lastId++
}

object RunManager : RunStore {
    val runs = ArrayList<RunModel>()

    override fun findAll(): List<RunModel> {
        return runs
    }

    override fun create(run: RunModel) {
        runs.add(run)
        logAll()
    }

    override fun update(run: RunModel) {
        val runExists: RunModel? = runs.find { p -> p.id == run.id }
        if (runExists != null) {
            runExists.runInKms = run.runInKms
            runExists.runInTime = run.runInTime
            runExists.image = run.image
            logAll()
        }
    }

    fun logAll() {
        runs.forEach{ i("${it}") }
    }
}