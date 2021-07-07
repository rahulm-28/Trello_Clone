package com.example.trello.models

import android.os.Parcel
import android.os.Parcelable

data class Board(
    val name: String = "",
    val image: String = "",
    val createdBy: String = "",
    val assignedTo: ArrayList<String> = ArrayList(),
    var documentId: String = "",
    var taskList: ArrayList<Task> = ArrayList()
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!,
        source.readString()!!,
        source.createStringArrayList()!!,
        source.readString()!!,
        source.createTypedArrayList(Task.CREATOR)!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(image)
        writeString(createdBy)
        writeStringList(assignedTo)
        writeString(documentId)
        writeTypedList(taskList)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Board> = object : Parcelable.Creator<Board> {
            override fun createFromParcel(source: Parcel): Board = Board(source)
            override fun newArray(size: Int): Array<Board?> = arrayOfNulls(size)
        }
    }
}

//data class Board(
//    val name: String? = "",
//    val image: String? = "",
//    val createdBy: String? = "",
//    val assignedTo: ArrayList<String>? = ArrayList(),
//    var documentId: String? = "",
//    var taskList: ArrayList<Task> = ArrayList()
//        ): Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.createStringArrayList(),
//        parcel.readString(),
//        parcel.createTypedArrayList(Task.CREATOR)!!
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) = with(parcel) {
//        parcel.writeString(name)
//        parcel.writeString(image)
//        parcel.writeString(createdBy)
//        parcel.writeStringList(assignedTo)
//        parcel.writeString(documentId)
//        parcel.writeTypedList(taskList)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Board> {
//        override fun createFromParcel(parcel: Parcel): Board {
//            return Board(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Board?> {
//            return arrayOfNulls(size)
//        }
//    }
//}