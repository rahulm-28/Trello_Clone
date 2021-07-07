package com.example.trello.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {

    // Firebase Constants
    // This  is used for the collection name for USERS.
    const val USERS: String = "users"

    // This  is used for the collection name for USERS.
    const val BOARDS: String = "boards"

    // Firebase database field names
    const val IMAGE: String = "image"
    const val NAME: String = "name"
    const val MOBILE: String = "mobile"
    const val ASSIGNED_TO: String = "assignedTo"
    const val DOCUMENT_ID: String = "documentId"
    const val TASK_LIST: String = "taskList"
    const val ID: String = "id"
    const val EMAIL: String = "email"

    const val BOARD_DETAIL: String = "board_detail"

    // START
    const val TASK_LIST_ITEM_POSITION: String = "task_list_item_position"
    const val CARD_LIST_ITEM_POSITION: String = "card_list_item_position"
    // END

    //A unique code for asking the Read Storage Permission using this we will be check and identify in the method onRequestPermissionsResult
    const val READ_STORAGE_PERMISSION_CODE = 1
    // A unique code of image selection from Phone Storage.
    const val PICK_IMAGE_REQUEST_CODE = 2

    const val BOARD_MEMBERS_LIST: String = "board_members_list"

    const val SELECT: String = "Select"
    const val UN_SELECT: String = "UnSelect"

    const val TRELLO_PREFERENCES: String = "TrelloPrefs"

    const val FCM_TOKEN:String = "fcmToken"
    const val FCM_TOKEN_UPDATED:String = "fcmTokenUpdated"

    const val FCM_BASE_URL:String = "https://fcm.googleapis.com/fcm/send"
    const val FCM_AUTHORIZATION:String = "authorization"
    const val FCM_KEY:String = "key"
    const val FCM_SERVER_KEY:String = "AAAAUC8YR0Q:APA91bHSd6i3zmNohc3bbt778gSuDF8jCJly3sJNWItzGUjdCdWHEtWLBbf0eky13MG5yRU3uHGdMDJ8YgowU_zyQsLBltnT7S8Ww4pzZ50IhzBDEfxN5xS7tEsQl8_5dvY2oM3VV6Yn"
    const val FCM_KEY_TITLE:String = "title"
    const val FCM_KEY_MESSAGE:String = "message"
    const val FCM_KEY_DATA:String = "data"
    const val FCM_KEY_TO:String = "to"

    /**
     * A function for user profile image selection from phone storage.
     */
    fun showImageChooser(activity: Activity) {
        // An intent for launching the image selection of phone storage.
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        // Launches the image selection of phone storage using the constant code.
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    /**
     * A function to get the extension of selected image.
     */
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}

//object Constants {
//
//    const val USERS: String = "users"
//    const val IMAGE: String = "image"
//    const val NAME: String = "name"
//    const val MOBILE: String = "mobile"
//
//    const val ASSIGNED_TO: String = "assignedTo"
//
//    const val BOARDS: String = "boards"
//
//    const val DOCUMENT_ID: String = "documentId"
//
//    const val TASK_LIST: String = "taskList"
//
//    const val READ_STORAGE_PERMISSION_CODE = 1
//    const val PICK_IMAGE_REQUEST_CODE = 2
//
//    const val BOARD_DETAIL: String = "board_detail"
//
//    const val ID: String = "id"
//
//    const val EMAIL: String = "email"
//
//    const val TASK_LIST_ITEM_POSITION: String = "task_list_item_position"
//    const val CARD_LIST_ITEM_POSITION: String = "card_list_item_position"
//
//    fun showImageChooser(activity: Activity) {
//        val galleryIntent = Intent(
//            Intent.ACTION_PICK,
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//        )
//        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
//    }
//
//    fun getFileTypeExtension(activity:Activity, uri: Uri?): String? {
//        return MimeTypeMap.getSingleton()
//            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
//    }
//
//    fun getFileExtension(activity: Activity, uri: Uri?): String? {
//        return MimeTypeMap.getSingleton()
//            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
//    }
//}

