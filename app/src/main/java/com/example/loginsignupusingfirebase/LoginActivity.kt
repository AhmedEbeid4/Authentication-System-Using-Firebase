package com.example.loginsignupusingfirebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private var signUpTextView:TextView?=null
    private var forgetPass:TextView?=null
    private var emailField:EditText?=null
    private var PassField:EditText?=null
    private var signUpBtn:Button?=null
    private var progressBar: ProgressBar?=null
    private val context:Context=this
    private var Auth:FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        assignComponents()
        actions()
    }
    private fun assignComponents(){
        signUpTextView=findViewById(R.id.signUpTextView)
        forgetPass=findViewById(R.id.forgetPasswordTextView)
        emailField=findViewById(R.id.emailField)
        PassField=findViewById(R.id.passwordField)
        signUpBtn=findViewById(R.id.signInButton)
        progressBar=findViewById(R.id.progressBar)
        Auth=FirebaseAuth.getInstance()
    }
    private fun actions(){
        signUpAc()
        LoginAc()
        emailFieldOnTextChanged()
        passFieldOnTextChanged()
    }
    private fun signUpAc(){
        signUpTextView?.setOnClickListener {
            startActivity(Intent(context,SignUpActivity::class.java))
            overridePendingTransition(0,0)
        }
    }
    private fun emailFieldOnTextChanged(){
        emailField?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                signUpBtn?.isEnabled = emailField?.text.toString().length>0 && PassField?.text.toString().length>0
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }
    private fun passFieldOnTextChanged(){
        PassField?.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                signUpBtn?.isEnabled = emailField?.text.toString().length>0 && PassField?.text.toString().length>0
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }
    private fun LoginAc(){

        signUpBtn?.setOnClickListener {
            var y=true
            if (emailField?.text.toString().length==0 ){
                emailField?.error = "Email Field is Empty"
                emailField?.requestFocus()
                y=false
            }
            if (PassField?.text.toString().length==0 ){
                PassField?.error = "Password Field is Empty"
                PassField?.requestFocus()
                y=false
            }
            if (PassField?.text.toString().length<6){
                PassField?.error = "Make Sure of The Password Please."
                PassField?.requestFocus()
                y=false
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(emailField?.text.toString()).matches()){
                emailField?.error = "Make Sure of The Email Please"
                emailField?.requestFocus()
                y=false
            }
           if (y){

               Handler().postDelayed({

                   signUpBtn?.visibility=View.GONE
                   progressBar?.visibility=View.VISIBLE
                   val email=emailField?.text.toString()
                   val pass=PassField?.text.toString()
                   Auth?.signInWithEmailAndPassword(email,pass)
                       ?.addOnCompleteListener {
                           if(it.isSuccessful){
                                startActivity(Intent(context,HomeActivity::class.java))
                           }else{
                               Toast.makeText(context,"Failed To Login",Toast.LENGTH_SHORT).show()
                           }
                       }
               },1000)

               progressBar?.visibility=View.GONE
               signUpBtn?.visibility=View.VISIBLE
           }
        }
    }

    override fun onBackPressed() {

    }
}