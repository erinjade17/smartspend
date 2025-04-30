package vcmsa.projects.smartspend

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Date
import java.util.Locale


class TotalTransactionsFragment : Fragment() {
        private lateinit var viewModel: HomeViewModel
        private lateinit var startDatePicker: DatePicker
        private lateinit var endDatePicker: DatePicker
        private lateinit var viewTransactionsButton: Button
        private lateinit var transactionsRecyclerView: RecyclerView
        private lateinit var expenseAdapter: ExpenseAdapter

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_total_transactions, container, false)
            startDatePicker = view.findViewById(R.id.startDatePicker)
            endDatePicker = view.findViewById(R.id.endDatePicker)
            viewTransactionsButton = view.findViewById(R.id.viewTransactionsButton)
            transactionsRecyclerView = view.findViewById(R.id.transactionsRecyclerView)
            return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

            // Set up RecyclerView and Adapter
            expenseAdapter = ExpenseAdapter(emptyList())
            transactionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            transactionsRecyclerView.adapter = expenseAdapter

            viewTransactionsButton.setOnClickListener {
                val startDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(startDatePicker.year - 1900, startDatePicker.month, startDatePicker.dayOfMonth))
                val endDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(endDatePicker.year - 1900, endDatePicker.month, endDatePicker.dayOfMonth))
                viewModel.getExpensesBetweenDates(startDate, endDate)
            }

            // Observe the expenses LiveData
            viewModel.expenses.observe(viewLifecycleOwner) { expenses ->
                expenseAdapter.updateExpenses(expenses)
            }
        }
    }
