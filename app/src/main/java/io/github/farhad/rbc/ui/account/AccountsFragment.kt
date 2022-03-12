package io.github.farhad.rbc.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.farhad.rbc.R
import io.github.farhad.rbc.databinding.AccountItemListItemBinding
import io.github.farhad.rbc.databinding.AccountTypeListItemBinding
import io.github.farhad.rbc.databinding.AccountsFragmentBinding
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

    private var _binding: AccountsFragmentBinding? = null
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
        _binding = AccountsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

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

    private fun setUpRecyclerView() {
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
        RecyclerView.Adapter<AccountViewHolder>() {
        private val list = mutableListOf<AccountDataItem>()

        fun submitList(newList: List<AccountDataItem>) {
            list.clear()
            list.addAll(newList)
            notifyDataSetChanged() // todo(fix this!)
        }

        override fun getItemCount(): Int = list.size

        override fun getItemViewType(position: Int): Int {
            return if (list[position] is AccountDataItem.Type) 0 else 1
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
            return when (viewType) {
                0 -> {
                    TypeViewHolder(
                        AccountTypeListItemBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )
                    )
                }

                1 -> {
                    ItemViewHolder(
                        AccountItemListItemBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        ), clickListener
                    )
                }
                else -> {
                    throw IllegalStateException()
                }
            }
        }

        override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
            holder.bind(list[holder.bindingAdapterPosition])
        }
    }

    abstract class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: AccountDataItem)
    }

    class TypeViewHolder(private val binding: AccountTypeListItemBinding) :
        AccountViewHolder(binding.root) {
        override fun bind(item: AccountDataItem) {
            item as AccountDataItem.Type
            binding.textviewName.text = item.title
        }
    }

    class ItemViewHolder(
        private val binding: AccountItemListItemBinding,
        private val clickListener: (item: AccountDataItem) -> Unit
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