package com.example.loginsignupusingfirebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    private var backBtn:ImageView?=null
    private var nameField:EditText?=null
    private var emailField:EditText?=null
    private var passField:EditText?=null
    private var signUpBtn:Button?=null
    private var progressBar:ProgressBar?=null
    private val context:Context=this
    private var Auth:FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()
        assignComponents()
        actions()
    }
    private fun assignComponents(){
        backBtn=findViewById(R.id.backBtn)
        nameField=findViewById(R.id.nameField)
        emailField=findViewById(R.id.emailField)
        passField=findViewById(R.id.passwordField)
        signUpBtn=findViewById(R.id.signUpButton)
        progressBar=findViewById(R.id.signInButton)
        Auth=FirebaseAuth.getInstance()
    }
    private fun actions(){
        BackAction()
        SignUpAct()
    }
    private fun BackAction(){
        backBtn?.setOnClickListener {
            startActivity(Intent(context,LoginActivity::class.java))
            overridePendingTransition(0,0)
        }
    }
    private fun SignUpAct(){
        signUpBtn?.setOnClickListener {
            if(nameField?.text.toString().length == 0 ){
                nameField?.error = "Name is required"
                nameField?.requestFocus()
            }
            if (emailField?.text.toString().length== 0){
                emailField?.error = "Email is required"
                emailField?.requestFocus()
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(emailField?.text.toString()).matches()){
                emailField?.error = "Invalid Email"
                emailField?.requestFocus()
            }
            if(passField?.text.toString().length <6){
                passField?.error = "Password has to be 6 chars or more !"
                passField?.requestFocus()
            }
            if (nameField?.text.toString().length > 0 && emailField?.text.toString().length > 0 && Patterns.EMAIL_ADDRESS.matcher(emailField?.text.toString()).matches() && passField?.text.toString().length >0){
                signUpBtn?.visibility= View.GONE
                progressBar?.visibility=View.VISIBLE
                Auth?.createUserWithEmailAndPassword(emailField?.text.toString(),passField?.text.toString())?.addOnCompleteListener {
                    if (it.isSuccessful){
                        val user:User= User(
                            nameField?.text.toString(),
                            emailField?.text.toString(),
                            passField?.text.toString()
                        )
                        FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                            .setValue(user)
//                            .addOnCompleteListener {
//                                if (it.isSuccessful){
//                                    Toast.makeText(context,"Successfully",Toast.LENGTH_SHORT).show()
//                                    progressBar?.visibility=View.GONE
//                                    signUpBtn?.visibility=View.VISIBLE
//                                    val i =Intent(context,LoginActivity::class.java)
//                                    startActivity(i)
//                                    overridePendingTransition(0,0)
//                                }else{
//                                    Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show()
//                                    progressBar?.visibility=View.GONE
//                                    signUpBtn?.visibility=View.VISIBLE
//                                }
//                            }
                        signUpBtn?.visibility= View.VISIBLE
                        progressBar?.visibility=View.GONE
                        this.onBackPressed()
                    }
                }
            }
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(context,LoginActivity::class.java))
        overridePendingTransition(0,0)
    }
}