package io.github.farhad.rbc.mapper

import com.rbc.rbcaccountlibrary.Account
import com.rbc.rbcaccountlibrary.AccountType
import io.github.farhad.rbc.TestUtils
import io.github.farhad.rbc.ui.account.list.AccountDataItem
import io.github.farhad.rbc.ui.account.list.mapToViewDataItems
import io.github.farhad.rbc.ui.util.getFriendlyTitle
import org.junit.Assert
import org.junit.Test

class AccountToViewDataItemMapperTest {

    @Test
    fun when_accounts_list_is_empty_is_returns_empty_data_items() {
        // arrange
        val accounts = listOf<Account>()

        // act
        val dataItems = accounts.mapToViewDataItems()

        // assert
        Assert.assertTrue(dataItems.isEmpty())
    }

    @Test
    fun it_create_type_and_item_dataItems_for_each_account() {
        // arrange
        val account = TestUtils.newAccount("CHQ", "5114", "100.00", AccountType.CHEQUING)
        val accounts = listOf(account)

        // act
        val viewStates = accounts.mapToViewDataItems()

        // assert
        Assert.assertTrue(viewStates.isNotEmpty())
        Assert.assertTrue(viewStates.size == 2)
        Assert.assertTrue(viewStates[0] is AccountDataItem.Type)
        Assert.assertTrue(viewStates[1] is AccountDataItem.Item)
        Assert.assertTrue((viewStates[0] as AccountDataItem.Type).title == account.type.getFriendlyTitle())
        Assert.assertTrue((viewStates[1] as AccountDataItem.Item).name == account.name)
        Assert.assertTrue((viewStates[1] as AccountDataItem.Item).number == account.number)
        Assert.assertTrue((viewStates[1] as AccountDataItem.Item).balance == account.balance)
    }

    @Test
    fun it_create_type_and_item_dataItems_for_each_account_and_groups_them_by_type() {
        // arrange
        val chequingAccountOne = TestUtils.newAccount("CHQ-1", "5113", "100.00", AccountType.CHEQUING)
        val chequingAccountTwo = TestUtils.newAccount("CHQ-2", "5114", "200.00", AccountType.CHEQUING)
        val creditCardAccount = TestUtils.newAccount("Master", "5524998522223636", "420.00", AccountType.CREDIT_CARD)
        val accounts = listOf(chequingAccountOne, chequingAccountTwo, creditCardAccount)

        // act
        val viewStates = accounts.mapToViewDataItems()

        // assert
        Assert.assertTrue(viewStates.isNotEmpty())
        Assert.assertTrue(viewStates.size == 5)
        Assert.assertTrue(viewStates[0] is AccountDataItem.Type)
        Assert.assertTrue(viewStates[1] is AccountDataItem.Item)
        Assert.assertTrue(viewStates[2] is AccountDataItem.Item)
        Assert.assertTrue(viewStates[3] is AccountDataItem.Type)
        Assert.assertTrue(viewStates[4] is AccountDataItem.Item)

        Assert.assertTrue((viewStates[0] as AccountDataItem.Type).title == chequingAccountOne.type.getFriendlyTitle())

        Assert.assertTrue((viewStates[1] as AccountDataItem.Item).name == chequingAccountOne.name)
        Assert.assertTrue((viewStates[1] as AccountDataItem.Item).number == chequingAccountOne.number)
        Assert.assertTrue((viewStates[1] as AccountDataItem.Item).balance == chequingAccountOne.balance)

        Assert.assertTrue((viewStates[2] as AccountDataItem.Item).name == chequingAccountTwo.name)
        Assert.assertTrue((viewStates[2] as AccountDataItem.Item).number == chequingAccountTwo.number)
        Assert.assertTrue((viewStates[2] as AccountDataItem.Item).balance == chequingAccountTwo.balance)

        Assert.assertTrue((viewStates[3] as AccountDataItem.Type).title == creditCardAccount.type.getFriendlyTitle())

        Assert.assertTrue((viewStates[4] as AccountDataItem.Item).name == creditCardAccount.name)
        Assert.assertTrue((viewStates[4] as AccountDataItem.Item).number == creditCardAccount.number)
        Assert.assertTrue((viewStates[4] as AccountDataItem.Item).balance == creditCardAccount.balance)


    }
}