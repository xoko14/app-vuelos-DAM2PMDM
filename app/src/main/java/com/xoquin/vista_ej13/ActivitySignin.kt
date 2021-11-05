package com.xoquin.vista_ej13

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.toxicbakery.bcrypt.Bcrypt

class ActivitySignin : AppCompatActivity() {
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
            if(txtUserPass.text == txtUserPass2.text){
                val user = hashMapOf(
                    "username" to txtUserName.text.toString(),
                    "pass" to String(Bcrypt.hash(txtUserPass.text.toString(), 10))
                )

            }
            else{
                val toast = Toast.makeText(applicationContext, getText(R.string.pass_not_match), Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}