package mick.studio.itsfuntorun.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber.i

var lastId = 1L

internal fun getId(): Long {
    return lastId++
}

object RunManager : RunStore {
    val runs = ArrayList<RunModel>()
//
//    override fun findAll(): List<RunModel> {
//        return runs
//    }
//
//    override fun create(run: RunModel) {
//        runs.add(run)
//        logAll()
//    }
//
//    override fun update(run: RunModel) {
//        val runExists: RunModel? = runs.find { p -> p.id == run.id }
//        if (runExists != null) {
//            runExists.runInKms = run.runInKms
//            runExists.runInTime = run.runInTime
//            runExists.image = run.image
//            logAll()
//        }
//    }

    fun logAll() {
        runs.forEach{ i("${it}") }
    }

    override fun findAll(runsList: MutableLiveData<List<RunModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, runsList: MutableLiveData<List<RunModel>>) {
        TODO("Not yet implemented")
    }

    override fun findById(userid: String, runid: String, donation: MutableLiveData<RunModel>) {
        TODO("Not yet implemented")
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, run: RunModel) {
        TODO("Not yet implemented")
    }

    override fun delete(userid: String, runid: String) {
        TODO("Not yet implemented")
    }

    override fun updateRun(userid: String, runid: String, run: RunModel) {
        TODO("Not yet implemented")
    }
}