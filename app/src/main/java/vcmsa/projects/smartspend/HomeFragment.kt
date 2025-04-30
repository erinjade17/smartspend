package vcmsa.projects.smartspend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {
        private lateinit var viewModel: HomeViewModel
        private lateinit var balanceTextView: TextView
        private lateinit var totalIncomeTextView: TextView
        private lateinit var totalExpenseTextView: TextView
        private lateinit var totalTransactionsTextView: TextView
        private lateinit var addIncomeButton: Button
        private lateinit var addExpenseButton: Button
        private lateinit var categoriesRecyclerView: RecyclerView
        private lateinit var categoryAdapter: CategoryAdapter
        private lateinit var expenseAdapter: ExpenseAdapter
        private lateinit var expensesRecyclerView: RecyclerView


        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_home, container, false)
            balanceTextView = view.findViewById(R.id.balanceTextView)
            totalIncomeTextView = view.findViewById(R.id.totalIncomeTextView)
            totalExpenseTextView = view.findViewById(R.id.totalExpenseTextView)
            totalTransactionsTextView = view.findViewById(R.id.totalTransactionsTextView)
            addIncomeButton = view.findViewById(R.id.addIncomeButton)
            addExpenseButton = view.findViewById(R.id.addExpenseButton)
            categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView)
            expensesRecyclerView = view.findViewById(R.id.expensesRecyclerView)


            return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

            // Set up RecyclerView and Adapter for Categories
            categoryAdapter = CategoryAdapter(emptyList())
            categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            categoriesRecyclerView.adapter = categoryAdapter

            // Set up RecyclerView and Adapter for Expenses
            expenseAdapter = ExpenseAdapter(emptyList())  // Create an ExpenseAdapter
            expensesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            expensesRecyclerView.adapter = expenseAdapter

            // Observe LiveData from ViewModel
            viewModel.balance.observe(viewLifecycleOwner) { balance ->
                balanceTextView.text = "R ${String.format("%.2f", balance)}"
            }

            viewModel.totalIncome.observe(viewLifecycleOwner) { income ->
                totalIncomeTextView.text = "R ${String.format("%.2f", income)}"
            }

            viewModel.totalExpense.observe(viewLifecycleOwner) { expense ->
                totalExpenseTextView.text = "R ${String.format("%.2f", expense)}"
            }

            viewModel.totalTransactions.observe(viewLifecycleOwner) { count ->
                totalTransactionsTextView.text = count.toString()
            }

            viewModel.categories.observe(viewLifecycleOwner) { categories ->
                categoryAdapter.updateCategories(categories)
            }

            viewModel.expenses.observe(viewLifecycleOwner) { expenses ->
                expenseAdapter.updateExpenses(expenses)
            }

            // Load data
            viewModel.loadHomePageData()

            // Set click listeners for the buttons
            addIncomeButton.setOnClickListener {
                // Navigate to Expense Entry Fragment (for income)
                findNavController().navigate(R.id.action_homeFragment_to_expenseEntryFragment)
            }

            addExpenseButton.setOnClickListener {
                // Navigate to Expense Entry Fragment (for expense)
                findNavController().navigate(R.id.action_homeFragment_to_expenseEntryFragment)
            }
        }
    }

}