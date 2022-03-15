package io.github.farhad.rbc.ui.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import io.github.farhad.rbc.databinding.HomeActivityBinding
import io.github.farhad.rbc.di.ViewModelFactory
import io.github.farhad.rbc.ui.account.list.AccountsFragment
import io.github.farhad.rbc.ui.account.list.AccountsViewModel
import io.github.farhad.rbc.ui.navigation.FragmentFactory
import io.github.farhad.rbc.ui.splash.SplashViewModel
import io.github.farhad.rbc.ui.util.BaseFragment
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModeFactory: ViewModelFactory

    private lateinit var splashViewModel: SplashViewModel
    private lateinit var accountViewModel: AccountsViewModel

    private lateinit var binding: HomeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashViewModel = ViewModelProvider(this, viewModeFactory)[SplashViewModel::class.java]
        accountViewModel = ViewModelProvider(this, viewModeFactory)[AccountsViewModel::class.java]

        if (savedInstanceState == null) {
            FragmentFactory.create()?.let { findAndReplaceFragment(it) }
        }
    }

    override fun onStart() {
        super.onStart()

        splashViewModel.navigationAction.observe(this) { action ->
            FragmentFactory.create(action)?.let { findAndReplaceFragment(it) }
        }

        accountViewModel.navigationAction.observe(this) { action ->
            FragmentFactory.create(action)?.let { findAndReplaceFragment(it) }
        }
    }

    private fun findAndReplaceFragment(newFragment: BaseFragment) {
        val fragment = supportFragmentManager.findFragmentByTag(newFragment.navigationTag) ?: newFragment
        fragment as BaseFragment
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment, fragment.navigationTag)
            .addToBackStack(fragment.navigationTag)
            .commit()
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(binding.fragmentContainer.id)) {
            is AccountsFragment -> finish()
            else -> super.onBackPressed()
        }
    }
}