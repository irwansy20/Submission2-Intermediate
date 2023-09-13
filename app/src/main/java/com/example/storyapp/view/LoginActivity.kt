package com.example.storyapp.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.response.LoginResponse
import com.example.storyapp.retrofit.ApiConfig
import com.example.storyapp.stories.ListStoryActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var preferences: SharedPreferences
    private var isChecked = false

    companion object{
        val PREFS_NAME = "user_pref"
        val NAME = "name"
        val ID ="userId"
        val TOKEN = "token"
        val CHECK_BOX = "isChecked"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        isChecked = preferences.getBoolean(CHECK_BOX, false)
        checkLogin(isChecked)

        playAnimation()
        getApiLogin()
    }

    //animation
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(500)
        val edtEmail = ObjectAnimator.ofFloat(binding.edtEmail, View.ALPHA, 1f).setDuration(500)
        val edtPass = ObjectAnimator.ofFloat(binding.edtPassword, View.ALPHA, 1f).setDuration(500)
        val keepMe = ObjectAnimator.ofFloat(binding.check, View.ALPHA, 1f).setDuration(500)
        val btnLog = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val textSign = ObjectAnimator.ofFloat(binding.tvRegis, View.ALPHA, 1f).setDuration(500)
        val btnSign = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(btnSign, textSign)
        }

        AnimatorSet().apply {
            playSequentially(title, edtEmail, edtPass, keepMe, btnLog, together)
            start()
        }
    }

    private fun checkLogin(login: Boolean){
        if (login){
            val intent = Intent(this@LoginActivity, ListStoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun validate(userId: String, name: String, token: String){
        val editor = preferences.edit()
        editor.putString(NAME, name)
        editor.putString(ID, userId)
        editor.putString(TOKEN, token)
        editor.putBoolean(CHECK_BOX, binding.check.isChecked)
        editor.apply()
    }

    private fun getApiLogin() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.edtEmail.error = "Masukkan email"
                }
                password.isEmpty() -> {
                    binding.edtPassword.error = "Masukkan password"
                }
                else -> {
                    showLoading(true)
                    val client = ApiConfig.getApiService().getLogin(email,password)
                    client.enqueue(object : Callback<LoginResponse> {
                        override fun onResponse(
                            call: Call<LoginResponse>,
                            response: Response<LoginResponse>
                        ) {
                            showLoading(false)
                            if (response.isSuccessful) {
                                response.body()?.loginResult?.apply {
                                    validate(userId, name, token)
                                    AlertDialog.Builder(this@LoginActivity).apply {
                                        setTitle("Yeah!")
                                        setMessage(resources.getString(R.string.message_login))
                                        setPositiveButton(resources.getString(R.string.message_btn)) { _, _ ->
                                            val intent = Intent(this@LoginActivity, ListStoryActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                        create()
                                        show()
                                    }
                            }
                            }else{
                                showLoading(false)
                                Toast.makeText(this@LoginActivity, resources.getString(R.string.toast_not), Toast.LENGTH_SHORT). show()
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            Toast.makeText(this@LoginActivity, resources.getString(R.string.toast_error), Toast.LENGTH_SHORT). show()
                        }

                    })
                }
            }
        }
        binding.btnSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_language, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.bahasa -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            else -> false
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}