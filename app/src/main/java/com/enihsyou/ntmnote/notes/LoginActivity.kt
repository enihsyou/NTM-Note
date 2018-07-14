package com.enihsyou.ntmnote.notes

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.enihsyou.ntmnote.R
import com.enihsyou.ntmnote.data.User
import com.enihsyou.ntmnote.data.source.UserDataSource
import com.enihsyou.ntmnote.data.source.remote.UserRepository
import com.enihsyou.ntmnote.data.source.remote.WebApi
import com.enihsyou.ntmnote.utils.AppExecutors
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private var running: Boolean = false

    private lateinit var dataSource: UserDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Set up the login form.
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        username_sign_in_button.setOnClickListener { attemptLogin() }
        username_sign_up_button.setOnClickListener { attemptSignUp() }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.100:8999")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(WebApi::class.java)
        dataSource = UserRepository(api, AppExecutors())
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (running) return

        // Reset errors.
        username.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val usernameStr = username.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(usernameStr)) {
            username.error = getString(R.string.error_field_required)
            focusView = username
            cancel = true
        } else if (!isUsernameValid(usernameStr)) {
            username.error = getString(R.string.error_invalid_username)
            focusView = username
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            dataSource.login(usernameStr, passwordStr, object : UserDataSource.LoginCallback {
                override fun onUserLogin(user: User?) {
                    showProgress(false)
                    if (user != null) {
                        val preferences = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                        val editor = preferences.edit()
                        editor.putString("username", usernameStr)
                        editor.putString("password", passwordStr)
                        editor.apply()

                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        password.error = getString(R.string.error_incorrect_password)
                        password.requestFocus()
                    }
                }
            })
        }
    }

    private fun attemptSignUp() {
        if (running) return

        // Reset errors.
        username.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val usernameStr = username.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(usernameStr)) {
            username.error = getString(R.string.error_field_required)
            focusView = username
            cancel = true
        } else if (!isUsernameValid(usernameStr)) {
            username.error = getString(R.string.error_invalid_username)
            focusView = username
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true)
            dataSource.register(usernameStr, passwordStr, object : UserDataSource.RegisterCallback {
                override fun onOperationDone(status: Boolean) {
                    showProgress(false)
                    if (status) {
                        Toast.makeText(applicationContext, "注册成功", Toast.LENGTH_SHORT).show()

                        username.text.clear()
                        password.text.clear()
                        username.requestFocus()
                    } else {
                        username.error = getString(R.string.error_invalid_username)
                        password.requestFocus()
                    }
                }
            })
        }
    }

    private fun isUsernameValid(username: String): Boolean {
        return username.length > 4
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    companion object {

        /**
         * Id to identity READ_CONTACTS permission request.
         */
        private val REQUEST_READ_CONTACTS = 0

        /**
         * A dummy authentication store containing known user names and passwords.
         * TODO: remove after connecting to a real authentication system.
         */
        private val DUMMY_CREDENTIALS = arrayOf("foo@example.com:hello", "bar@example.com:world")
    }
}
