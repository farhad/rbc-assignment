package io.github.farhad.rbc.ui.navigation;

public interface NavigationAction {
    final class ShowAccounts implements NavigationAction {
    }

    final class ShowAccountDetails implements NavigationAction {
        private final String accountName;
        private final String accountNumber;
        private final String accountBalance;

        public ShowAccountDetails(String accountName, String accountNumber, String accountBalance) {
            this.accountName = accountName;
            this.accountNumber = accountNumber;
            this.accountBalance = accountBalance;
        }

        public String getAccountName() {
            return accountName;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getAccountBalance() {
            return accountBalance;
        }
    }
}
