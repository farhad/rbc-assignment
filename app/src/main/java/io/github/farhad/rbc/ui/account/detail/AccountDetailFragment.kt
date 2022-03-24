package io.github.farhad.rbc.ui.account.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.*
import io.github.farhad.rbc.R
import io.github.farhad.rbc.databinding.AccountDetailFragmentBinding
import io.github.farhad.rbc.databinding.TransactionDateListItemBinding
import io.github.farhad.rbc.databinding.TransactionItemListItemBinding
import io.github.farhad.rbc.ui.account.list.AccountDataItem
import io.github.farhad.rbc.ui.navigation.FragmentFactory
import io.github.farhad.rbc.ui.util.BaseFragment
import io.github.farhad.rbc.ui.util.changeVisibility
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AccountDetailFragment : BaseFragment() {

    init {
        entryPoint = FragmentFactory.isEntryPoint(this)
    }

    companion object {
        private const val ACCOUNT_DATA_ITEM = "account_data_item"

        fun newInstance(accountDataItem: AccountDataItem.Item): AccountDetailFragment {
            return AccountDetailFragment().apply {
                arguments = bundleOf(ACCOUNT_DATA_ITEM to accountDataItem)
            }
        }
    }

    private lateinit var viewModel: AccountDetailsViewModel
    private lateinit var adapter: TransactionsAdapter

    private var _binding: AccountDetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[AccountDetailsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = AccountDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLayout()

        arguments?.getSerializable(ACCOUNT_DATA_ITEM)?.let { viewModel.setUp(it as AccountDataItem.Item) }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.accountInformation.onEach {
                    when (it) {
                        is AccountInformationViewState.AccountInformation -> {
                            binding.textviewNameAndNumber.text = it.nameAndNumber
                            binding.textviewBalance.text = it.balanceWithCurrencySymbol
                        }

                        else -> {
                            //no op
                        }
                    }
                }.launchIn(this)

                viewModel.accountDetails.onEach {
                    binding.progressbar.changeVisibility(it.loadingVisible)
                    binding.recyclerview.changeVisibility(it.listVisible)
                    binding.textviewError.changeVisibility(it.errorVisible)
                    adapter.submitList(it.dataItems)

                    when (it) {
                        is AccountDetailViewState.Error -> {
                            binding.textviewError.text = getString(R.string.error_loading_transactions)
                        }

                        is AccountDetailViewState.EmptyResult -> {
                            binding.textviewError.text = getString(R.string.error_empty_list_transactions)
                        }
                        else -> {
                            // no op
                        }
                    }
                }.launchIn(this)
            }
        }
    }

    private fun setUpLayout() {
        adapter = TransactionsAdapter()
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireActivity(), RecyclerView.VERTICAL))
            isNestedScrollingEnabled = true
        }
        binding.recyclerview.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class TransactionsAdapter : ListAdapter<AccountDetailsDataItem, TransactionsAdapter.TransactionViewHolder>(TransactionDiffUtil) {

        companion object {
            private object TransactionDiffUtil : DiffUtil.ItemCallback<AccountDetailsDataItem>() {
                override fun areContentsTheSame(oldItem: AccountDetailsDataItem, newItem: AccountDetailsDataItem): Boolean {
                    return oldItem == newItem
                }

                override fun areItemsTheSame(oldItem: AccountDetailsDataItem, newItem: AccountDetailsDataItem): Boolean {
                    return oldItem == newItem
                }
            }
        }

        enum class ViewType {
            DATE,
            TRANSACTION;

            companion object {
                fun from(value: Int): ViewType {
                    return if (value == DATE.ordinal) DATE else TRANSACTION
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
            return when (ViewType.from(viewType)) {
                ViewType.DATE -> {
                    DateItemViewHolder(TransactionDateListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                }

                ViewType.TRANSACTION -> {
                    TransactionItemViewHolder(TransactionItemListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
                }
            }
        }

        override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
            holder.bind(currentList[holder.bindingAdapterPosition])
        }

        override fun getItemViewType(position: Int): Int {
            return if (currentList[position] is AccountDetailsDataItem.Date) ViewType.DATE.ordinal else ViewType.TRANSACTION.ordinal
        }

        override fun getItemCount(): Int = currentList.size

        abstract class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            abstract fun bind(item: AccountDetailsDataItem)
        }

        class DateItemViewHolder(private val binding: TransactionDateListItemBinding) : TransactionViewHolder(binding.root) {
            override fun bind(item: AccountDetailsDataItem) {
                item as AccountDetailsDataItem.Date
                binding.textviewDate.text = item.formattedDate
            }
        }

        class TransactionItemViewHolder(private val binding: TransactionItemListItemBinding) : TransactionViewHolder(binding.root) {
            override fun bind(item: AccountDetailsDataItem) {
                item as AccountDetailsDataItem.Transaction
                binding.textviewDescription.text = item.description
                binding.textviewAmount.text = item.amountWithCurrencySymbol
            }
        }
    }
}