package com.lateef.firebase_project

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.lateef.firebase_project.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    //view binding
    private lateinit var binding: ActivityLoginBinding

    //Action bar
    private lateinit var actionBar: ActionBar

    //progressDialog
    private lateinit var progressDialog: ProgressDialog

    //firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = " "
    private var password = " "


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionBar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        //configure progressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Logging In....")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click, open SignUp activity
        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }


        //handle click login
        binding.loginBtn.setOnClickListener {
            //before logging in, validate data
            validateData()

        }
    }

    private fun validateData() {
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.emailEt.error = "Invalid Email Format"
        }
        else if (TextUtils.isEmpty(password)){
            //no password entered
            binding.passwordEt.error = "Please enter password"
        }
        else{
            //data is validate being login
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        //show progressDialog
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()
                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Login as $email ", Toast.LENGTH_SHORT).show()
                //open profile activity
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()

            }
            .addOnFailureListener {e ->
                //login fail
                progressDialog.dismiss()
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

    private fun checkUser() {
        //if user is already logged in go to profile Activity
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            //user already logged in
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }
    }
}