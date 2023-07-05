package com.alvaro.suitemdiamd.ui.second_screen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.alvaro.suitemdiamd.data.pagging.SetPreference
import com.alvaro.suitemdiamd.databinding.ActivitySecondScreenBinding
import com.alvaro.suitemdiamd.ui.VMFactory
import com.alvaro.suitemdiamd.ui.third_screen.ThirdScreenActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class SecondScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondScreenBinding
    private lateinit var viewModel: SecondScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this@SecondScreenActivity,
            VMFactory(SetPreference.getInstance(dataStore),this@SecondScreenActivity)
        )[SecondScreenViewModel::class.java]

        val userName = viewModel.getUserName()
        userName.observe(this){
            binding.apply {
                tvInputName.text = it.insertUserName
                tvSelectedUser.text = it.selectedUserName
            }
        }
        buttonAction()

    }

    private fun buttonAction(){
        binding.apply {
            secondScreenAppbar.setNavigationOnClickListener {
                onBackPressed()
            }
            btnChooseUser.setOnClickListener {
                val intent = Intent(this@SecondScreenActivity, ThirdScreenActivity::class.java)
                startActivity(intent)
            }
        }
    }
}