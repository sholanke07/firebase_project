package com.lateef.firebase_project

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.lateef.firebase_project.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    //view binding
    private lateinit var binding: ActivityProfileBinding

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
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionBar
        //actionBar Enable backButton
        actionBar = supportActionBar!!
        actionBar.title = "Profile"

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle logOut
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
    }

    private fun checkUser() {
        // check user logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null){
            // user not null, user is logged in, get user info
            val email = firebaseUser.email
            //set text to view
            binding.emailTv.text = email

        }
        else{
            //user is null, user is not logged in, go to loginActivity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}