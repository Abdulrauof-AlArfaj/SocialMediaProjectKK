package com.example.socialmediaprojectkk

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socialmediaprojectkk.databinding.ActivityLoginBinding
import com.example.socialmediaprojectkk.databinding.ActivitySignupBinding

class LoginActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var context: Context
                super.onCreate(savedInstanceState)
        var binding:ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //set action to sign up text
        context=this@LoginActivity
        binding.apply {
            signupBtn.setOnClickListener {
                var intent= Intent(context,SignupActivity::class.java)
                context.startActivity(intent)

            }//end of on clicklistener for signup
        }


    }
}