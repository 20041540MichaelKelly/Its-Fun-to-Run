package mick.studio.itsfuntorun.models.friends

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import mick.studio.itsfuntorun.models.RunModel

interface FriendsStore {
    fun addFriend(firebaseUser: MutableLiveData<FirebaseUser>,friend: FriendsModel)

    fun findAllFriends(userid: String, friendsList: MutableLiveData<List<FriendsModel>>)
}