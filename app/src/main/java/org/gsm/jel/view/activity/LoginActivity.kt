package org.gsm.jel.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import org.gsm.jel.BuildConfig
import org.gsm.jel.R
import org.gsm.jel.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private lateinit var requestActivity : ActivityResultLauncher<Intent>
    private lateinit var googleSignInClient: GoogleSignInClient

    private val TAG = "LoginActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.activity = this
        initGoogleLogin()
        resultActivity()
        binding.loginGoogle.setOnClickListener { signIn() }
    }

    private fun initGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun resultActivity(){
        requestActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if(it.resultCode == RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Toast.makeText(this, "구글 로그인 실패", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onActivityResult: $e")
                }
            }
        }
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        requestActivity.launch(signInIntent)
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    if (useRegex(auth.currentUser?.email.toString())) {
                        Log.d(TAG, "firebaseAuthWithGoogle: 성공")
                        loginSuccess()
                    } else {
                        Toast.makeText(this, "학교 계정을 사용해주세요", Toast.LENGTH_SHORT).show()
                        auth.currentUser?.delete()
                        googleSignInClient.signOut()
                        googleSignInClient.revokeAccess()
                    }

                } else {
                    Log.d(TAG, "firebaseAuthWithGoogle: ${auth.currentUser?.email}")
                    Log.d(TAG, "firebaseAuthWithGoogle: 실패")
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "구글 로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
    }


    //이메일 정규식 필터
    fun useRegex(input: String): Boolean {
        val regex = Regex(
            pattern = "^[a-zA-Z][0-9][0-9][0-9][0-9][0-9]+@gsm\\.hs\\.kr\$",
            options = setOf(RegexOption.IGNORE_CASE)
        )
        return regex.matches(input)
    }


    private fun loginSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}