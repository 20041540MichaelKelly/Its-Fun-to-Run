package mick.studio.itsfuntorun.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface RunStore {
    fun findAll(runsList:
                MutableLiveData<List<RunModel>>
    )
    fun findAll(userid:String,
                runsList:
                MutableLiveData<List<RunModel>>)
    fun findById(userid:String, runid: String,
                 run: MutableLiveData<RunModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, run: RunModel)
    fun delete(userid:String, runid: String)
    fun updateRun(userid:String, runid: String, run: RunModel)
}
