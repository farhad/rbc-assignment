package io.github.farhad.rbc.di.modules

import dagger.Module
import dagger.Provides
import io.github.farhad.rbc.model.validator.AccountDetailInputValidator
import io.github.farhad.rbc.model.validator.AccountDetailInputValidatorImpl
import javax.inject.Singleton

@Module
class ValidatorModule {

    @Provides
    @Singleton
    fun provideAccountDetailInputValidator(): AccountDetailInputValidator = AccountDetailInputValidatorImpl()
}