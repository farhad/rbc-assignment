package io.github.farhad.rbc.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.*
import io.github.farhad.rbc.R
import io.github.farhad.rbc.databinding.AccountItemListItemBinding
import io.github.farhad.rbc.databinding.AccountTypeListItemBinding
import io.github.farhad.rbc.databinding.AccountsListFragmentBinding
import io.github.farhad.rbc.ui.util.BaseFragment
import io.github.farhad.rbc.ui.util.changeVisibility
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AccountsFragment : BaseFragment() {

    companion object {
        fun newInstance(): AccountsFragment = AccountsFragment()
    }

    private lateinit var viewModel: AccountsViewModel
    private lateinit var adapter: AccountsAdapter

    private var _binding: AccountsListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            requireActivity(),
            viewModelFactory
        )[AccountsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AccountsListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpLayout()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.accountsViewState.collect {
                    binding.progressbar.changeVisibility(it.loadingVisible)
                    binding.textviewError.changeVisibility(it.errorVisible)
                    binding.recyclerview.changeVisibility(it.listVisible)
                    adapter.submitList(it.dataItems)

                    when (it) {
                        is AccountsViewState.Error -> binding.textviewError.text =
                            getString(R.string.error_loading_accounts)
                        is AccountsViewState.EmptyResult -> {
                            binding.textviewError.text =
                                getString(R.string.error_empty_list_accounts)
                        }
                        else -> {} // no op
                    }
                }
            }
        }
    }

    private fun setUpLayout() {
        binding.toolbar.title = getString(R.string.title_accounts)

        adapter = AccountsAdapter {
            viewModel.onAccountsSelected(it)
        }

        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(requireActivity(), RecyclerView.VERTICAL))
        }
        binding.recyclerview.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class AccountsAdapter(private val clickListener: (item: AccountDataItem) -> Unit) :
        ListAdapter<AccountDataItem, AccountsAdapter.AccountViewHolder>(AccountDataItemDiffUtil()) {

        class AccountDataItemDiffUtil : DiffUtil.ItemCallback<AccountDataItem>() {
            override fun areContentsTheSame(
                oldItem: AccountDataItem,
                newItem: AccountDataItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: AccountDataItem, newItem: AccountDataItem): Boolean {
                return oldItem == newItem
            }
        }

        enum class ViewType {
            TYPE,
            ACCOUNT;

            companion object {
                fun from(value: Int): ViewType {
                    return if (value == TYPE.ordinal) TYPE else ACCOUNT
                }
            }
        }

        override fun getItemCount(): Int = currentList.size

        override fun getItemViewType(position: Int): Int {
            return if (currentList[position] is AccountDataItem.Type) ViewType.TYPE.ordinal else ViewType.ACCOUNT.ordinal
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
            return when (ViewType.from(viewType)) {
                ViewType.TYPE -> {
                    TypeViewHolder(
                        AccountTypeListItemBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )
                    )
                }

                ViewType.ACCOUNT -> {
                    ItemViewHolder(
                        AccountItemListItemBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )
                    )
                }
            }
        }

        override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
            holder.bind(currentList[holder.bindingAdapterPosition])
        }

        abstract inner class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            abstract fun bind(item: AccountDataItem)
        }

        inner class TypeViewHolder(private val binding: AccountTypeListItemBinding) :
            AccountViewHolder(binding.root) {
            override fun bind(item: AccountDataItem) {
                item as AccountDataItem.Type
                binding.textviewName.text = item.title
            }
        }

        inner class ItemViewHolder(
            private val binding: AccountItemListItemBinding
        ) : AccountViewHolder(binding.root) {

            override fun bind(item: AccountDataItem) {
                item as AccountDataItem.Item

                binding.textviewName.text = item.name
                binding.textviewNumber.text = item.number
                binding.textviewBalance.text = "${item.balance} ${item.currencySymbol}"
                binding.constraintLayout.setOnClickListener {
                    clickListener.invoke(item)
                }
            }
        }
    }
}