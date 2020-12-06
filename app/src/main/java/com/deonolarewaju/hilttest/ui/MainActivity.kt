package com.deonolarewaju.hilttest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.deonolarewaju.hilttest.R
import com.deonolarewaju.hilttest.databinding.ActivityMainBinding
import com.deonolarewaju.hilttest.model.BlogModel
import com.deonolarewaju.hilttest.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val TAG: String = "AppDebugger"

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()
        viewModel.setStateEvent(MainStateEvent.GetBlogEvents)

        recyclerViewMain.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )

    }


    private fun init() {

        setDataToRecyclerView(listOf())
        subscribeObservers()

    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is DataState.Success<List<BlogModel>> -> {
                    displayProgressBar(false)
                    setDataToRecyclerView(dataState.data)

                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(dataState.exception.message)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)

                }
            }
        })
    }

    private fun setDataToRecyclerView(userList: List<BlogModel>) {
        binding.recyclerViewMain.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = BlogAdapter(userList)
        }
    }

    private fun displayError(message: String?) {

        if (message != null) {
            Toast.makeText(this, "error: " + message, Toast.LENGTH_LONG)
            binding.textViewStart.text = message
        } else {
            Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG)
            binding.textViewStart.text = "Unknown error"
        }
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding.progressBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

}