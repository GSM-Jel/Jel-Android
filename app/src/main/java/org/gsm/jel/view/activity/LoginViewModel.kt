package org.gsm.jel.view.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.gsm.jel.R


class LoginViewModel : ViewModel() {

    lateinit var googleSignInClient : GoogleSignInClient


     fun initGoogleLogin(activity : AppCompatActivity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("521593304736-a1blbiblhbeaonol2iq25kmgpds64cbg.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun signOut(){
        googleSignInClient.signOut()
        googleSignInClient.revokeAccess()
    }

}