package org.gsm.jel.view.activity

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.gsm.jel.R
import org.gsm.jel.databinding.ActivityMainBinding
import org.gsm.jel.databinding.HeaderBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var headB: HeaderBinding
    private val loginViewModel : LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        setNavigationView()
        loginViewModel.initGoogleLogin(this)
    }

    private fun initViews() = with(binding) {
        lifecycleOwner = this@MainActivity
        activity = this@MainActivity

        //headerLayout 바인딩 연결
        val headV = navigationView.getHeaderView(0)
        headB = HeaderBinding.bind(headV)
        headB.activity = this@MainActivity
        headB.lifecycleOwner = this@MainActivity

    }

    //NavigationView 초기화
    private fun setNavigationView() = with(binding) {
        //네비게이션뷰 연결 아래 코드 없을시 메뉴속 아이템 클릭반응 없음
        navigationView.setNavigationItemSelectedListener(this@MainActivity)
        //메뉴바 그리기
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
    }

    //메뉴 버튼 열기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //메뉴속 아이템 클릭 이벤트
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(ContentValues.TAG, "onNavigationItemSelected: 불러오기")
        when (item.itemId) {
            R.id.addProfile -> {
                Log.d(ContentValues.TAG, "onNavigationItemSelected: addGit 클릭")
                Toast.makeText(this, "테스트", Toast.LENGTH_SHORT).show()
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://github.com/login/oauth/authorize?client_id=685ffb52e4dd768b3f66&redirect_uri=https://d6ui2fy5uj.execute-api.ap-northeast-2.amazonaws.com/api/auth&scope=user:email")
                )
                startActivity(intent)
            }

            R.id.logOut -> {
                loginViewModel.signOut()
                finish()
            }



        }
        return false
    }


}