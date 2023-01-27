
package com.example.archedny_app_friend.core.domain.utils.validation

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot


object DataManeger {
    inline fun <reified T> handledata(value: QuerySnapshot?): ResultState<MutableList<T>> {
        val data = mutableListOf<T>()
        if (value != null) {
            value.documents.forEach {
                data.add(it.toObject(T::class.java)!!)
            }
            if (data.isEmpty()) {
                return ResultState.IsError(msg = "No data Found")
            } else {
                return ResultState.IsSucsses(data = data)
            }
        } else {
            return ResultState.IsError(msg = "No data Found")
        }
    }

    inline fun <reified T> handleSingledata(it: Task<DocumentSnapshot>): ResultState<T> {
        if (it.result != null) {
            Log.d("mylog", "${it.result.toObject(T::class.java)!!}")
            return ResultState.IsSucsses(data = it.result.toObject(T::class.java)!!)
        } else {
            return ResultState.IsError(msg = "No data Found")
        }
    }

}


//private fun showToast()
//{
//    val toast= Toast(requireActivity())
//    toast.duration= Toast.LENGTH_SHORT
//    toast.setGravity(Gravity.CENTER,0,0)
//    toast.view= LayoutInflater.from(requireActivity()).inflate(R.layout.layout_succes,null,false)
//    toast.show()
//}