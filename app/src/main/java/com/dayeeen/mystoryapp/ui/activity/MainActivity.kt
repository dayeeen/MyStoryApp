package com.dayeeen.mystoryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dayeeen.mystoryapp.R
import com.dayeeen.mystoryapp.databinding.ActivityMainBinding
import com.dayeeen.mystoryapp.ui.adapters.ListStoryAdapter
import com.dayeeen.mystoryapp.viewmodel.MainViewModel
import com.dayeeen.mystoryapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mvm by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set Toolbar sebagai ActionBar
        setSupportActionBar(binding.topAppBar)

        // Cek apakah user sudah login atau belum
        isLogin()

        // Tampilkan list story
        showStories()

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, UploadStoryActivity::class.java))
        }

    }


    private fun isLogin() {
        mvm.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, StartActivity::class.java))
                finish()
            }
        }
    }

    private fun showStories() {
        val adapter = ListStoryAdapter()
        binding.rvStory.adapter = adapter
        binding.rvStory.layoutManager = LinearLayoutManager(this)

        mvm.stories.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.maps_option -> {
                // Handle maps logic
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            R.id.logout_option -> {
                // Handle logout logic
                mvm.logout()
                Toast.makeText(this, "Logout Success", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

