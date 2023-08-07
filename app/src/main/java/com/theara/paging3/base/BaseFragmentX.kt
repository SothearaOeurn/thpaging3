package com.theara.paging3.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

open class BaseFragmentX<T : ViewDataBinding>(@LayoutRes private val layoutResId: Int) :
    Fragment() {

    private var _binding: T? = null
    val binding: T get() = _binding!!

    open fun T.initialize() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)

        // Optionally set lifecycle owner if needed
        binding.lifecycleOwner = viewLifecycleOwner

        // Calling the extension function
        binding.initialize()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showProgress(view: View?) {
        view?.visibility = View.VISIBLE
    }

    fun hideProgress(view: View?) {
        view?.visibility = View.GONE
    }
}