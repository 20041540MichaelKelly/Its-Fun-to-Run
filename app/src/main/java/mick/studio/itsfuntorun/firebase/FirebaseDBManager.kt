package mick.studio.itsfuntorun.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import mick.studio.itsfuntorun.models.RunModel
import mick.studio.itsfuntorun.models.RunStore
import mick.studio.itsfuntorun.models.users.UserModel
import mick.studio.itsfuntorun.models.users.UserStore
import timber.log.Timber

object FirebaseDBManager: RunStore, UserStore{
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(runsList: MutableLiveData<List<RunModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, runsList: MutableLiveData<List<RunModel>>) {

        database.child("user-runs").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Its Fun To Run error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<RunModel>()
                    val children = snapshot.children
                    children.forEach {
                        val run = it.getValue(RunModel::class.java)
                        localList.add(run!!)
                    }
                    database.child("user-runs").child(userid)
                        .removeEventListener(this)

                    runsList.value = localList
                }
            })
    }


    override fun findById(userid: String, runid: String, run: MutableLiveData<RunModel>) {
        database.child("user-runs").child(userid)
            .child(runid).get().addOnSuccessListener {
                run.value = it.getValue(RunModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, run: RunModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("runs").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        run.runid = key
        run.uid = uid

        val runValues = run.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/runs/$key"] = runValues
        childAdd["/user-runs/$uid/$key"] = runValues

        database.updateChildren(childAdd)
        Timber.i("completed childAdd : $childAdd")


    }


    override fun delete(userid: String, runid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/runs/$runid"] = null
        childDelete["/user-runs/$userid/$runid"] = null

        database.updateChildren(childDelete)
    }

    override fun updateRun(userid: String, runid: String, run: RunModel) {

        val runValues = run.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["runs/$runid"] = runValues
        childUpdate["user-runs/$userid/$runid"] = runValues

        database.updateChildren(childUpdate)
    }

    override fun findUser(userid: String, user: MutableLiveData<RunModel>) {
        database.child("user-runs").child(userid)
            .get().addOnSuccessListener {
                user.value = it.getValue(RunModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun createUser( user: UserModel) {
        Timber.i("Firebase DB Reference : ${database}")

        val key = database.child("user-info").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }

        val userValues = user.toMap()

        val uid = user.uid

        val childAdd = HashMap<String, Any>()
        childAdd["/user-info/$key"] = userValues
        childAdd["/user-runs/$uid/$key"] = userValues

        FirebaseDBManager.database.updateChildren(childAdd)
        Timber.i("completed childAdd : $childAdd")
    }

    override fun findAllUsers(userid: String, usersList: MutableLiveData<List<UserModel>>) {

        database.child("user-info")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Its Fun To Run error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<UserModel>()
                    val children = snapshot.children
                    children.forEach {
                        val user = it.getValue(UserModel::class.java)
                        localList.add(user!!)
                    }
                    database.child("user-info")
                        .removeEventListener(this)

                    usersList.value = localList
                }
            })
    }

}