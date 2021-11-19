package com.xoquin.vista_ej13

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.toxicbakery.bcrypt.Bcrypt

class ActivitySignin : AppCompatActivity() {
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val txtUserName: EditText = findViewById(R.id.txtUserNameSign)
        val txtUserPass: EditText = findViewById(R.id.txtUserPassSign)
        val txtUserPass2: EditText = findViewById(R.id.txtRepeatPass)
        val spnLoad: ProgressBar = findViewById(R.id.progressCircleSign)

        val btnCreateAccount: Button = findViewById(R.id.btnCreateAccount)
        btnCreateAccount.setOnClickListener {
            spnLoad.visibility = View.VISIBLE
            if(txtUserPass.text.toString() == txtUserPass2.text.toString()){
                val user = hashMapOf(
                    "username" to txtUserName.text.toString(),
                    "pass" to String(Bcrypt.hash(txtUserPass.text.toString(), 10))
                )

                val doc = db.collection("users").document(txtUserName.text.toString())
                doc.get().addOnSuccessListener {
                    if (it.exists()){
                        val toastError: Toast = Toast.makeText(applicationContext, getText(R.string.err_db), Toast.LENGTH_SHORT)
                        toastError.show()
                    }
                }
                db.collection("users").document(txtUserName.text.toString())
                    .set(user)
                    .addOnSuccessListener {
                        finish()
                    }
                    .addOnFailureListener {
                        val toastError: Toast = Toast.makeText(applicationContext, getText(R.string.err_db), Toast.LENGTH_SHORT)
                        toastError.show()
                    }
                    .addOnCompleteListener {
                        spnLoad.visibility = View.INVISIBLE
                    }

            }
            else{
                val toast = Toast.makeText(applicationContext, getText(R.string.pass_not_match), Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}