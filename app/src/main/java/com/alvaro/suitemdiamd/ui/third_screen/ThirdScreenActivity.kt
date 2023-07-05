package com.alvaro.suitemdiamd.ui.third_screen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.alvaro.suitemdiamd.data.pagging.SetPreference
import com.alvaro.suitemdiamd.databinding.ActivityThirdScreenBinding
import com.alvaro.suitemdiamd.ui.VMFactory
import com.alvaro.suitemdiamd.ui.adapter.LoadingAdapter
import com.alvaro.suitemdiamd.ui.adapter.SelectedUserList

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class ThirdScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdScreenBinding
    private lateinit var viewModel: ThirdScreenViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel =   ViewModelProvider(this@ThirdScreenActivity, VMFactory(SetPreference.getInstance(dataStore), this@ThirdScreenActivity)
        )[ThirdScreenViewModel::class.java]

        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvSelectUser.layoutManager = layoutManager
        binding.rvSelectUser.addItemDecoration(itemDecoration)

        buttonAction()
        showList()

    }

    private fun buttonAction(){
        binding.apply {
            thirdScreenAppbar.setNavigationOnClickListener {
                onBackPressed()
                finish()
            }
        }
    }

    private fun showList(){
        val adapter = SelectedUserList{String ->
            viewModel.showSelectedUser(String)
            finish()
        }
        adapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.NotLoading && adapter.itemCount < 1) {
                binding.apply {
                    rvSelectUser.isVisible = false
                }
            } else {
                binding.apply {
                    rvSelectUser.isVisible = true
                }
            }
        }
        binding.rvSelectUser.adapter = adapter.withLoadStateFooter(
            footer = LoadingAdapter{adapter.retry()}
        )
        viewModel.users.observe(this){temp -> adapter.submitData(lifecycle,temp)}

    }
}