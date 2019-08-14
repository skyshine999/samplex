package com.pro.simpsons

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.pro.simpsons.databinding.BaseActivityBinding
import com.pro.simpsons.details.DetailsFragment
import com.pro.simpsons.home.HomeFragment

class BaseActivity : AppCompatActivity() {

    var binding: BaseActivityBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.base_activity)

        if (verifyAvailableNetwork()) {
            if (resources.getBoolean(R.bool.isTablet)) {
                var homeFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.first_container, homeFragment).commit()

                var detailsFragment = DetailsFragment()
                supportFragmentManager.beginTransaction().replace(R.id.second_container, detailsFragment).commit()
            } else {
                var homeFragment: HomeFragment = HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit()
            }
        }


    }
    // this is also one way if internet requires password authentication
//   fun isInternetAvailable():Boolean {
//        try {
//            var address = InetAddress.getByName("https://duckduckgo.com/");
//            return !address.equals("");
//        } catch (e:UnknownHostException) {
//            // Log error
//        }
//        return false;
//    }

    fun verifyAvailableNetwork(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

}