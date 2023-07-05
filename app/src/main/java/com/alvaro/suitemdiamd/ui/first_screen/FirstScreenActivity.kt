package com.alvaro.suitemdiamd.ui.first_screen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alvaro.suitemdiamd.data.pagging.SetPreference
import com.alvaro.suitemdiamd.databinding.ActivityFirstScreenBinding
import com.alvaro.suitemdiamd.ui.VMFactory
import com.alvaro.suitemdiamd.ui.second_screen.SecondScreenActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class FirstScreenActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFirstScreenBinding
    private lateinit var viewModel: FirstScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = ViewModelProvider(this@FirstScreenActivity,VMFactory(SetPreference.getInstance(dataStore),this@FirstScreenActivity)
        )[FirstScreenViewModel::class.java]

        buttonAction()
    }

    private fun buttonAction(){
        binding.apply {
            btnCheck.setOnClickListener {
                val inputPalindrome = etPalindrome.text.toString()
                viewModel.isPalindrome(inputPalindrome)
                lifecycleScope.launchWhenStarted {
                    viewModel.isPalindrome.collect { reversed ->
                        if(reversed)
                            showDialog("Hasil", "ini adalah Palindrome")
                        else
                            showDialog("Hasil", "ini bukan Palindrome")
                    }
                }
            }
            btnNext.setOnClickListener {
                val insertName = etName.text.toString()
                viewModel.saveInsertName(insertName)
                val intent = Intent(this@FirstScreenActivity,SecondScreenActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showDialog (title: String, message: String){
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Okay"){button, _ -> button.dismiss()
            }
            .setCancelable(false)
            .show()
    }
}
