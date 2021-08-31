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
import com.lateef.firebase_project.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    //view binding
    private lateinit var binding: ActivitySignUpBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //progressDialog
    private lateinit var progressDialog: ProgressDialog

    //firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = " "
    private var password = " "


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionBar
        //actionBar Enable backButton
        actionBar = supportActionBar!!
        actionBar.title = "SignUp"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //configure progressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Create Account....")
        progressDialog.setCanceledOnTouchOutside(false)


        //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()

        //handle click login
        binding.signupBtn.setOnClickListener {
            //validate data
            validateData()

        }

    }

    private fun validateData() {
        //get data
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
        else if (password.length < 6){
            //password length is less than 6
            binding.passwordEt.error = "password must be atleast 6 chracters long"
        }
        else{
            //data is valid, continue signUp
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        //show progressDialog
        progressDialog.show()

        //create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
               //SignUp successful
                progressDialog.dismiss()
                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Account Created with email $email ", Toast.LENGTH_SHORT).show()

                //open profile activity
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }
            .addOnFailureListener {e ->
                //sigup failed
                progressDialog.dismiss()
                Toast.makeText(this, "SignUp failed due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

    override fun onSupportNavigateUp(): Boolean {
        //go back to previous activity, when back button or actionBar is clicked
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}