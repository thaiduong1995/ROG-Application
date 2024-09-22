package com.duong.rog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.duong.rog.data.reponse.ProgressBarState
import com.duong.rog.databinding.ActivityMainBinding
import com.duong.rog.extension.getDotsSpanAnimator
import com.duong.rog.vm.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel by viewModels<SharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        initUi()
        initListener()
    }

    private fun initData() {
        this.lifecycleScope.launch {
            this@MainActivity.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    sharedViewModel.progressBarState.collect {
                        observeProgressBarState(it)
                    }
                }
            }
        }
    }

    private fun observeProgressBarState(progressBarState: ProgressBarState) {
        binding.viewLoading.apply {
            when (progressBarState) {
                ProgressBarState.HandlerData -> {
                    root.isVisible = true
                    imgLoading.isVisible = true
                    tvLoading.isVisible = false
                    tvProgressBarValue.isVisible = true
                    tvProgressBarDes.isVisible = true

                    // Start long running operation in a background thread
                    var progress = 1
                    Thread {
                        while (progress < 99) {
                            // Update the progress bar and display the
                            progress += 1
                            //current value in the text view
                            Handler(Looper.getMainLooper()).post {
                                tvProgressBarValue.text =
                                    getString(R.string.resolving, String.format(Locale.US,"%d", progress))
                            }
                            try {
                                // Sleep for 200 milliseconds.
                                Thread.sleep(200)
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                        }
                    }.start()
                }

                ProgressBarState.Idle -> {
                    root.isGone = true
                }

                ProgressBarState.LoadingData -> {
                    root.isVisible = true
                    imgLoading.isVisible = true
                    tvLoading.isVisible = true
                    tvProgressBarValue.isVisible = false
                    tvProgressBarDes.isVisible = false
                }
            }
        }
    }

    private fun initUi() {
        binding.viewLoading.apply {
            val dotsAnimator = tvLoading.getDotsSpanAnimator(R.string.loading)
            dotsAnimator?.start()
        }
    }

    private fun initListener() {
        binding.viewLoading.root.setOnClickListener { }
    }
}

