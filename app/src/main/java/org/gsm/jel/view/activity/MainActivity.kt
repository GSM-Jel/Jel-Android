package org.gsm.jel.view.activity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import org.gsm.jel.R
import org.gsm.jel.databinding.ActivityMainBinding
import org.gsm.jel.databinding.HeaderBinding


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var headB: HeaderBinding
    private val loginViewModel: LoginViewModel by viewModels()
    lateinit var navController : NavController


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

        //메뉴바 그리기
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(org.gsm.jel.R.drawable.ic_baseline_menu_24)

        //navController and appBarConfiguration 초기화
        navController = Navigation.findNavController(this@MainActivity, org.gsm.jel.R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this@MainActivity,navController,drawerLayout)
        NavigationUI.setupWithNavController(navigationView,navController)

        //네비게이션뷰 연결 아래 코드 없을시 메뉴속 아이템 클릭반응 없음
        navigationView.setNavigationItemSelectedListener(this@MainActivity)
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
        when (item.itemId) {

            R.id.addProfile -> {
                Log.d(TAG, "onNavigationItemSelected: addProfile")
                navController.navigate(R.id.addProfileFragment)
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            }

            R.id.logOut -> {
                loginViewModel.signOut()
                Toast.makeText(this@MainActivity, "로그아웃 하셨습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        return true
    }


}