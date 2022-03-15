package io.github.farhad.rbc.viewstate

import com.rbc.rbcaccountlibrary.Transaction
import io.github.farhad.rbc.TestUtils
import io.github.farhad.rbc.ui.account.detail.AccountDetailsDataItem
import io.github.farhad.rbc.ui.account.detail.mapToViewDataItems
import io.github.farhad.rbc.ui.util.formatDate
import org.junit.Assert
import org.junit.Test
import java.util.*

class TransactionsToViewDataItemsMapperTest {

    @Test
    fun when_transactions_list_it_returns_empty_data_items() {
        // arrange
        val transactions = listOf<Transaction>()

        // act
        val dataItems = transactions.mapToViewDataItems()

        // assert
        Assert.assertTrue(dataItems.isEmpty())
    }

    @Test
    fun it_create_date_and_transaction_dataItems_for_each_transaction() {
        // arrange
        val transaction = TestUtils.newTransaction("bill payment", "20.14", Calendar.getInstance())
        val transactions = listOf(transaction)

        // act
        val dataItems = transactions.mapToViewDataItems()

        // assert
        Assert.assertTrue(dataItems.isNotEmpty())
        Assert.assertTrue(dataItems.size == 2)
        Assert.assertTrue(dataItems[0] is AccountDetailsDataItem.Date)
        Assert.assertTrue(dataItems[1] is AccountDetailsDataItem.Transaction)
        Assert.assertTrue((dataItems[0] as AccountDetailsDataItem.Date).formattedDate == formatDate(transaction.date))
        Assert.assertTrue((dataItems[1] as AccountDetailsDataItem.Transaction).description == transaction.description)
        Assert.assertTrue((dataItems[1] as AccountDetailsDataItem.Transaction).amount == transaction.amount)
    }

    @Test
    fun it_create_date_and_transaction_dataItems_for_each_transaction_and_groups_them_by_date() {
        // arrange
        val dateOne = Calendar.getInstance()
        dateOne.set(2021, 12, 14)
        val transactionOne = TestUtils.newTransaction("bill 1", "20.14", dateOne)
        val transactionTwo = TestUtils.newTransaction("bill 2", "20.14", dateOne)

        val dateTwo = Calendar.getInstance()
        dateTwo.set(2021, 6, 14)
        val transactionThree = TestUtils.newTransaction("bill 3", "20.14", dateTwo)

        val transactions = listOf(transactionOne, transactionTwo, transactionThree)

        // act
        val dataItems = transactions.mapToViewDataItems()

        // assert
        Assert.assertTrue(dataItems.isNotEmpty())
        Assert.assertTrue(dataItems.size == 5)
        Assert.assertTrue(dataItems[0] is AccountDetailsDataItem.Date)
        Assert.assertTrue(dataItems[1] is AccountDetailsDataItem.Transaction)
        Assert.assertTrue(dataItems[2] is AccountDetailsDataItem.Transaction)
        Assert.assertTrue(dataItems[3] is AccountDetailsDataItem.Date)
        Assert.assertTrue(dataItems[4] is AccountDetailsDataItem.Transaction)

        Assert.assertTrue((dataItems[0] as AccountDetailsDataItem.Date).formattedDate == formatDate(dateOne))

        Assert.assertTrue((dataItems[1] as AccountDetailsDataItem.Transaction).description == transactionOne.description)
        Assert.assertTrue((dataItems[1] as AccountDetailsDataItem.Transaction).amount == transactionOne.amount)

        Assert.assertTrue((dataItems[2] as AccountDetailsDataItem.Transaction).description == transactionTwo.description)
        Assert.assertTrue((dataItems[2] as AccountDetailsDataItem.Transaction).amount == transactionTwo.amount)

        Assert.assertTrue((dataItems[3] as AccountDetailsDataItem.Date).formattedDate == formatDate(dateTwo))

        Assert.assertTrue((dataItems[4] as AccountDetailsDataItem.Transaction).description == transactionThree.description)
        Assert.assertTrue((dataItems[4] as AccountDetailsDataItem.Transaction).amount == transactionThree.amount)
    }
}