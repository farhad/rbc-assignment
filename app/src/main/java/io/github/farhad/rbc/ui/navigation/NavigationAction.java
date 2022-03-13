package io.github.farhad.rbc.ui.navigation;

public interface NavigationAction {
    final class ShowAccounts implements NavigationAction {
    }

    final class ShowAccountDetails implements NavigationAction {
        private final String accountName;
        private final String accountNumber;
        private final String accountBalance;
        private final String accountTypeName;

        public ShowAccountDetails(String accountName,
                                  String accountNumber,
                                  String accountBalance,
                                  String accountTypeName) {
            this.accountName = accountName;
            this.accountNumber = accountNumber;
            this.accountBalance = accountBalance;
            this.accountTypeName = accountTypeName;
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

        public String getAccountTypeName() {
            return accountTypeName;
        }
    }
}
