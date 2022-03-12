package io.github.farhad.rbc.ui.account.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.farhad.rbc.databinding.AccountDetailFragmentBinding
import io.github.farhad.rbc.ui.util.BaseFragment

class AccountDetailFragment : BaseFragment() {

    private var _binding: AccountDetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AccountDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}