package io.github.farhad.rbc.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.farhad.rbc.databinding.AccountsFragmentBinding
import io.github.farhad.rbc.ui.util.BaseFragment

class AccountsFragment : BaseFragment() {

    private var _binding: AccountsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AccountsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}