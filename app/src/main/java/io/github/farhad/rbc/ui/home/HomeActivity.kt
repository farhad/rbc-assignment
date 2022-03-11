package io.github.farhad.rbc.ui.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import io.github.farhad.rbc.databinding.HomeActivityBinding
import io.github.farhad.rbc.di.ViewModelFactory
import io.github.farhad.rbc.ui.account.AccountsFragment
import io.github.farhad.rbc.ui.splash.SplashAction
import io.github.farhad.rbc.ui.splash.SplashFragment
import io.github.farhad.rbc.ui.splash.SplashViewModel
import javax.inject.Inject

class HomeActivity : DaggerAppCompatActivity() {

    companion object {
        private const val TAG_SPLASH = "splash_fragment"
        private const val TAG_ACCOUNTS = "accounts_fragment"
    }

    @Inject
    lateinit var viewModeFactory: ViewModelFactory

    private lateinit var splashViewModel: SplashViewModel

    private lateinit var binding: HomeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        splashViewModel = ViewModelProvider(this, viewModeFactory)[SplashViewModel::class.java]

        if (savedInstanceState == null) {
            findAndReplaceFragment(TAG_SPLASH)
        }
    }

    override fun onStart() {
        super.onStart()

        splashViewModel.splashActions.observe(this) {
            when (it) {
                is SplashAction.ShowAccounts -> {
                    findAndReplaceFragment(TAG_ACCOUNTS)
                }
            }
        }
    }

    private fun findAndReplaceFragment(tag: String) {
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if (fragment == null) {
            fragment = when (tag) {
                TAG_SPLASH -> SplashFragment.newInstance()
                TAG_ACCOUNTS -> AccountsFragment()
                else -> null
            }
        }

        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainter.id, it, tag)
                .addToBackStack(tag)
                .commit()
        }
    }

    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(binding.fragmentContainter.id)) {
            is AccountsFragment -> finish()
            else -> super.onBackPressed()
        }
    }
}