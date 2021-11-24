package com.xoquin.vista_ej13

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.toxicbakery.bcrypt.Bcrypt
import com.xoquin.vista_ej13.utils.UserSingleton

class ActivityLogin : AppCompatActivity() {
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val txtUserName: EditText = findViewById(R.id.txtUserName)
        val txtUserPass: EditText = findViewById(R.id.txtUserPass)
        val spnLoad: ProgressBar = findViewById(R.id.progressCircle)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            spnLoad.visibility = View.VISIBLE
            db.collection("users")
                .whereEqualTo("username", txtUserName.text.toString())
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents){
                        val pass: String = document.getString("pass") as String
                        Log.i("login", document.get("username").toString())
                        Log.i("login", pass)
                        if(Bcrypt.verify(txtUserPass.text.toString(), pass.toByteArray())) {
                            UserSingleton.username = txtUserName.text.toString()
                            val intent = Intent(this, ActivityBuscar::class.java)
                            startActivity(intent)
                        }
                        else{
                            val toastError: Toast = Toast.makeText(applicationContext, getText(R.string.err_password), Toast.LENGTH_SHORT)
                            toastError.show()
                        }
                    }

                    if(documents.isEmpty){
                        val toastError: Toast = Toast.makeText(applicationContext, getText(R.string.err_user), Toast.LENGTH_SHORT)
                        toastError.show()
                    }
                }
                .addOnFailureListener {
                    Log.i("login", "user not found")
                    val toastError: Toast = Toast.makeText(applicationContext, getText(R.string.err_db), Toast.LENGTH_SHORT)
                    toastError.show()
                }
                .addOnCompleteListener {
                    spnLoad.visibility = View.INVISIBLE

                }
        }

        val btnSignin: Button = findViewById(R.id.btnSignin)
        btnSignin.setOnClickListener {
            val intent = Intent(this, ActivitySignin::class.java)
            startActivity(intent)
        }
    }
}