package com.example.trello.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.trello.R
import com.example.trello.firebase.FirestoreClass
import com.example.trello.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in.et_email
import kotlinx.android.synthetic.main.activity_sign_in.et_password
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignInActivity : BaseActivity() {
    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_sign_in)

        setupActionBar()

        btn_sign_in.setOnClickListener {
            signInRegisteredUser()
        }

        doFullScreen()

    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(toolbar_sign_in_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_sign_in_activity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function for Sign-In using the registered user using the email and password.
     */
    private fun signInRegisteredUser() {
        // Here we get the text from editText and trim the space
        val email: String = et_email.text.toString().trim { it <= ' ' }
        val password: String = et_password.text.toString().trim { it <= ' ' }

        if (validateForm(email, password)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))

            // Sign-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Calling the FirestoreClass signInUser function to get the data of user from database.
                        FirestoreClass().loadUserData(this@SignInActivity)
                    } else {
                        Toast.makeText(
                            this@SignInActivity,
                            task.exception!!.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    /**
     * A function to validate the entries of a user.
     */
    private fun validateForm(email: String, password: String): Boolean {
        return if (TextUtils.isEmpty(email)) {
            showErrorSnackBar("Please enter email.")
            false
        } else if (TextUtils.isEmpty(password)) {
            showErrorSnackBar("Please enter password.")
            false
        } else {
            true
        }
    }

    /**
     * A function to get the user details from the firestore database after authentication.
     */
    fun signInSuccess(user: User) {

        hideProgressDialog()

        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        this.finish()
    }

    @SuppressLint("NewApi")
    private fun doFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
//            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
        }
    }
}

//class SignInActivity : BaseActivity() {
//
//    private lateinit var auth: FirebaseAuth
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_in)
//
//        auth = Firebase.auth
//
//        setUpActionBar()
//
//        btn_sign_in.setOnClickListener {
//            signInRegisteredUser()
//        }
//
//        doFullScreen()
//    }
//
//    private fun signInRegisteredUser() {
//        val email: String = et_email.text.toString().trim { it <= ' ' }
//        val password: String = et_password.text.toString().trim { it <= ' ' }
//
//        if (validateForm(email, password)) {
//            showProgressDialog(resources.getString(R.string.please_wait))
//
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this) { task ->
//                    hideProgressDialog()
//                    if (task.isSuccessful) {
//                        FirestoreClass().loadUserData(this@SignInActivity)
//                    } else {
//                        Log.w("Sign In", "signInWithEmail:failure", task.exception)
//                        Toast.makeText(baseContext, "Authentication failed.",
//                            Toast.LENGTH_SHORT).show()
//                    }
//                }
//        }
//    }
//
//    private fun validateForm(email: String, password: String): Boolean {
//        return when {
//            TextUtils.isEmpty(email) -> {
//                showErrorSnackBar("Please enter email")
//                false
//            }
//            TextUtils.isEmpty(password) -> {
//                showErrorSnackBar("Please enter password")
//                false
//            }
//
//            else -> {
//                true
//            }
//        }
//    }
//
//    private fun setUpActionBar() {
//        setSupportActionBar(toolbar_sign_in_activity)
//
//        val actionBar = supportActionBar
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
//        }
//
//        toolbar_sign_in_activity.setNavigationOnClickListener {
//            onBackPressed()
//        }
//    }
//
//
//    fun signInSuccess(user: User?) {
//        hideProgressDialog()
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()
//    }
//
//}