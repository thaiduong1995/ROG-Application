package com.duong.rog.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.duong.rog.base.BaseDialog
import com.duong.rog.databinding.DialogDeleteBinding

/**
 * Created by Hưng Nguyễn on 30/01/2024
 * Phone: 0335236374
 * Email: nguyenhunghung2806@gmail.com
 */
class DialogDelete : BaseDialog() {

    private var _binding: DialogDeleteBinding? = null
    private val binding get() = _binding!!
    var onConfirmListener: () -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDeleteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initListener() {
        super.initListener()
        binding.tvCancel.setOnClickListener {
            dismiss()
        }

        binding.tvConfirm.setOnClickListener {
            onConfirmListener()
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        const val TAG = "DialogDelete"

        @JvmStatic
        fun newInstance() = DialogDelete().apply {
            arguments = bundleOf()
        }
    }
}