package io.github.farhad.rbc.validator

import io.github.farhad.rbc.model.validator.AccountDetailInputValidatorImpl
import io.github.farhad.rbc.model.validator.AccountValidationResult
import org.junit.Assert
import org.junit.Test

class AccountDetailValidatorTest {

    @Test
    fun when_account_number_is_null_it_returns_validation_result_account_number_empty() {
        // arrange
        val validator = AccountDetailInputValidatorImpl()

        // act
        val result = validator.validate(null)

        // assert
        Assert.assertTrue(result == AccountValidationResult.ACCOUNT_NUMBER_EMPTY)
    }

    @Test
    fun when_account_number_is_empty_it_returns_validation_result_account_number_empty() {
        // arrange
        val validator = AccountDetailInputValidatorImpl()

        // act
        val result = validator.validate("")

        // assert
        Assert.assertTrue(result == AccountValidationResult.ACCOUNT_NUMBER_EMPTY)
    }

    @Test
    fun when_account_number_is_null_String_it_returns_validation_result_account_number_empty() {
        // arrange
        val validator = AccountDetailInputValidatorImpl()

        // act
        val result = validator.validate("null")

        // assert
        Assert.assertTrue(result == AccountValidationResult.ACCOUNT_NUMBER_EMPTY)
    }

    @Test
    fun when_account_number_is_valid_it_returns_validation_result_success() {
        // arrange
        val validator = AccountDetailInputValidatorImpl()

        // act
        val result = validator.validate("23232")

        // assert
        Assert.assertTrue(result == AccountValidationResult.SUCCESS)
    }
}