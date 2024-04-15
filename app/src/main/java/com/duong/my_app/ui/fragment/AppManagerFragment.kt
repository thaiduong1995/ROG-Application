package com.duong.my_app.ui.fragment

import android.app.AppOpsManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.duong.my_app.R
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.base.BaseViewModel
import com.duong.my_app.databinding.FragmentAppManagerBinding
import com.duong.my_app.ui.adapter.AppAdapter
import com.duong.my_app.vm.AppManagerViewModel

class AppManagerFragment : BaseFragment() {

    override val viewModel by viewModels<AppManagerViewModel>()

    private lateinit var binding: FragmentAppManagerBinding
    private val appAdapter = AppAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun fetchData(context: Context) {
        super.fetchData(context)
        requestPermission(context)
    }

    private fun requestPermission(context: Context) {
        try {
            val packageManager = context.packageManager
            val applicationInfo = packageManager.getApplicationInfo(context.packageName, 0)
            val appManager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            var modeApp = 0
            modeApp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appManager.unsafeCheckOpNoThrow(
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    applicationInfo.uid,
                    applicationInfo.packageName
                )
            } else appManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                applicationInfo.uid,
                applicationInfo.packageName
            )
            modeApp == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = AppManagerFragment().apply {
            arguments = bundleOf()
        }
    }
}