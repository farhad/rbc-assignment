package io.github.farhad.rbc.ui.navigation

import io.github.farhad.rbc.ui.account.detail.AccountDetailFragment
import io.github.farhad.rbc.ui.account.list.AccountsFragment
import io.github.farhad.rbc.ui.navigation.NavigationAction.ShowAccountDetails
import io.github.farhad.rbc.ui.navigation.NavigationAction.ShowAccounts
import io.github.farhad.rbc.ui.splash.SplashFragment
import io.github.farhad.rbc.ui.util.BaseFragment

object FragmentFactory {

    private const val TAG_SPLASH = "splash_fragment"
    private const val TAG_ACCOUNTS = "accounts_fragment"
    private const val TAG_ACCOUNT_DETAILS = "account_details_fragment"

    fun create(action: NavigationAction? = null): BaseFragment? {
        return when (action) {
            null -> SplashFragment.newInstance().apply {
                navigationTag = TAG_SPLASH
                entryPoint = true
            }

            is ShowAccounts -> AccountsFragment.newInstance().apply {
                navigationTag = TAG_ACCOUNTS
                entryPoint = true
            }

            is ShowAccountDetails -> {
                AccountDetailFragment.newInstance(action.accountDataItem).apply {
                    navigationTag = TAG_ACCOUNT_DETAILS
                    entryPoint = false
                }
            }

            else -> null
        }
    }
}