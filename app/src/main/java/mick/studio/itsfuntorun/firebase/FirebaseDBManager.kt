package mick.studio.itsfuntorun.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.models.RunStore
import timber.log.Timber

object FirebaseDBManager: RunStore {
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

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
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("run-data").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        run.uid = key
        val runValues = run.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/run-data/$key"] = runValues
        childAdd["/user-run-data/$uid/$key"] = runValues

        database.updateChildren(childAdd)
    }


    override fun delete(userid: String, runid: String) {
        TODO("Not yet implemented")
    }

    override fun update(userid: String, runid: String, run: RunModel) {
        TODO("Not yet implemented")
    }

}