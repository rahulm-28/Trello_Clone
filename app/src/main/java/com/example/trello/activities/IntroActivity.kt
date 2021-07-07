package com.example.trello.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.example.trello.R
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_intro)

        doFullScreen()

        // This is used to hide the status bar and make the splash screen as a full screen activity.
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )

        // This is used to get the file from the assets folder and set it to the title textView.
//        val typeface: Typeface =
//            Typeface.createFromAsset(assets, "carbon bl.ttf")
//        tv_app_name_intro.typeface = typeface

        btn_sign_in_intro.setOnClickListener {

            // Launch the sign in screen.
            startActivity(Intent(this@IntroActivity, SignInActivity::class.java))
        }

        btn_sign_up_intro.setOnClickListener {

            // Launch the sign up screen.
            startActivity(Intent(this@IntroActivity, SignUpActivity::class.java))
        }
    }

    @SuppressLint("NewApi")
    private fun doFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
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
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
    }
}

//class IntroActivity : BaseActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_intro)
//        doFullScreen()
//        btn_sign_up_intro.setOnClickListener {
//            goToSignUpPage()
//        }
//        btn_sign_in_intro.setOnClickListener {
//            goToSignInPage()
//        }
//    }
//
//
//    private fun goToSignUpPage() {
//        startActivity(Intent(
//            this@IntroActivity, SignUpActivity::class.java
//        ))
//    }
//
//    private fun goToSignInPage() {
//        startActivity(Intent(
//            this@IntroActivity, SignInActivity::class.java
//        ))
//    }
//}

