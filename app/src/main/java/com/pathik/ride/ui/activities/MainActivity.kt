package com.pathik.ride.ui.activities

import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.mikhaellopez.circularimageview.CircularImageView
import com.pathik.ride.R
import com.pathik.ride.databinding.ActivityMainBinding
import com.pathik.ride.utils.UserPref
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var headerView: View

    private lateinit var ivProfile: CircularImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView


    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavHostFragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        headerView = binding.navView.getHeaderView(0)
        ivProfile = headerView.findViewById(R.id.ivProfile)
        tvName = headerView.findViewById(R.id.tvName)
        tvEmail = headerView.findViewById(R.id.tvEmail)

//        window.apply {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                statusBarColor = Color.TRANSPARENT
//            }
//        }

//        Util.setTransparentWindow(window)

        setupDrawerLayout()
    }

    override fun onResume() {
        super.onResume()
        tvName.text = UserPref.getString(UserPref.KEY_NAME)
        tvEmail.text = UserPref.getString(UserPref.KEY_EMAIL)
        if (UserPref.getString(UserPref.KEY_USER_PROFILE_URL).isNotEmpty()) {
            Picasso.get().load(UserPref.getString(UserPref.KEY_USER_PROFILE_URL))
                .placeholder(R.drawable.ic_user_place_holder)
                .error(R.drawable.ic_user_place_holder)
                .into(ivProfile)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

    public fun openDrawer(){
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    private fun setupDrawerLayout() {
        binding.navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}
