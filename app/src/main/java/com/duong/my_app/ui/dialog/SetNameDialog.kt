package com.duong.my_app.ui.dialog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import com.duong.my_app.R
import com.duong.my_app.base.BaseDialog
import com.duong.my_app.databinding.DialogSetNameBinding

class SetNameDialog : BaseDialog() {

    private var _binding: DialogSetNameBinding? = null
    private val binding get() = _binding!!
    private var name = ""
    var onSetNameListener: (String) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSetNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initListener() {
        binding.apply {
            imgClose.setOnClickListener {
                dismiss()
            }
            edtName.doOnTextChanged { text, start, before, count ->
                text?.toString()?.let { name = it }
            }
            btnOk.setOnClickListener {
                if (name.isNotEmpty()) onSetNameListener(name)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SetName.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = SetNameDialog().apply {
                arguments = bundleOf()
            }
    }
}