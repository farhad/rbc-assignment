package io.github.farhad.rbc.model.validator

import io.github.farhad.rbc.ui.util.isNotNullOrEmpty

interface AccountDetailInputValidator {

    fun validate(accountNumber: String?): AccountValidationResult
}

enum class AccountValidationResult {
    SUCCESS,
    ACCOUNT_NUMBER_EMPTY;
}

class AccountDetailInputValidatorImpl : AccountDetailInputValidator {
    override fun validate(accountNumber: String?): AccountValidationResult {
        return if (accountNumber.isNotNullOrEmpty()) AccountValidationResult.SUCCESS else AccountValidationResult.ACCOUNT_NUMBER_EMPTY
    }
}
