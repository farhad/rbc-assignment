package io.github.farhad.rbc.ui.navigation;

import io.github.farhad.rbc.ui.account.list.AccountDataItem;

public interface NavigationAction {
    final class ShowAccounts implements NavigationAction {
    }

    final class ShowAccountDetails implements NavigationAction {
        private final AccountDataItem.Item accountDataItem;

        public ShowAccountDetails(AccountDataItem.Item accountDataItem) {
            this.accountDataItem = accountDataItem;
        }

        public AccountDataItem.Item getAccountDataItem() {
            return accountDataItem;
        }
    }
}
