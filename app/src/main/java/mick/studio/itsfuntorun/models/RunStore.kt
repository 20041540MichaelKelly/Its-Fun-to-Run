package mick.studio.itsfuntorun.models

interface RunStore {
    fun findAll(): List<RunModel>
    fun create(run: RunModel)
    fun update(run: RunModel)
}