package io.github.farhad.rbc.ui.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import io.github.farhad.rbc.databinding.HomeActivityBinding
import io.github.farhad.rbc.di.ViewModelFactory
import io.github.farhad.rbc.ui.account.AccountsFragment
import io.github.farhad.rbc.ui.account.AccountsViewModel
import io.github.farhad.rbc.ui.account.detail.AccountDetailFragment
import io.github.farhad.rbc.ui.navigation.NavigationAction
import io.github.farhad.rbc.ui.splash.SplashFragment
import io.github.farhad.rbc.ui.splash.SplashViewModel
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity() {

    companion object {
        private const val TAG_SPLASH = "splash_fragment"
        private const val TAG_ACCOUNTS = "accounts_fragment"
        private const val TAG_ACCOUNT_DETAILS = "account_details"
    }

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
            findAndReplaceFragment(TAG_SPLASH)
        }
    }

    override fun onStart() {
        super.onStart()

        splashViewModel.navigationAction.observe(this) {
            processNavigationAction(it)
        }

        accountViewModel.navigationAction.observe(this) {
            processNavigationAction(it)
        }
    }

    private fun processNavigationAction(action: NavigationAction) {
        when (action) {
            is NavigationAction.ShowAccounts -> {
                findAndReplaceFragment(TAG_ACCOUNTS)
            }

            is NavigationAction.ShowAccountDetails -> {
                findAndReplaceFragment(TAG_ACCOUNT_DETAILS)
            }
        }
    }

    private fun findAndReplaceFragment(tag: String) {
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = when (tag) {
                TAG_SPLASH -> SplashFragment.newInstance()
                TAG_ACCOUNTS -> AccountsFragment.newInstance()
                TAG_ACCOUNT_DETAILS -> AccountDetailFragment()
                else -> null
            }
        }

        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, it, tag)
                .addToBackStack(tag)
                .commit()
        }
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(binding.fragmentContainer.id)) {
            is AccountsFragment -> finish()
            else -> super.onBackPressed()
        }
    }
}