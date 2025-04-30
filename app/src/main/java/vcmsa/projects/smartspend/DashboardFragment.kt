package vcmsa.projects.smartspend

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.compose.material3.Button
import androidx.compose.ui.text.intl.Locale
import androidx.fragment.app.Fragment
import com.google.type.Date

class DashboardFragment {
    class DashboardFragment : Fragment() {
        private lateinit var viewModel: HomeViewModel
        private lateinit var expenseBarChart: BarChart
        private lateinit var monthSpinner: Spinner
        private lateinit var goalEditText: EditText
        private lateinit var timeLimitEditText: EditText
        private lateinit var setGoalButton: Button
        private var selectedMonth: Int = 0
        private var categories: List<Category> = emptyList()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
            expenseBarChart = view.findViewById(R.id.expenseBarChart)
            monthSpinner = view.findViewById(R.id.monthSpinner)
            goalEditText = view.findViewById(R.id.goalEditText)
            timeLimitEditText = view.findViewById(R.id.timeLimitEditText)
            setGoalButton = view.findViewById(R.id.setGoalButton)
            return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

            // Set up month spinner
            val months = resources.getStringArray(R.array.months_array) // Create string-array in res/values/arrays.xml
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            monthSpinner.adapter = adapter

            monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedMonth = position
                    updateBarChart()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }

            // Get categories
            viewModel.categories.observe(viewLifecycleOwner){
                categories = it
                updateBarChart() //update chart after categories are loaded
            }
            viewModel.loadHomePageData()

            setGoalButton.setOnClickListener {
                // Get user inputs
                val goal = goalEditText.text.toString().toDoubleOrNull() ?: 0.0
                val timeLimit = timeLimitEditText.text.toString().toIntOrNull() ?: 0

                // TODO: Save the goal and time limit (e.g., in shared preferences or database)
                Toast.makeText(requireContext(), "Goal set for ${months[selectedMonth]}: $goal in $timeLimit days", Toast.LENGTH_SHORT).show()
            }

            setupBarChart()
            updateBarChart() // Initial update
        }

        private fun setupBarChart() {
            expenseBarChart.description.isEnabled = false
            expenseBarChart.setDrawValueAboveBar(true)
            expenseBarChart.setPinchZoom(false)
            expenseBarChart.setDrawGridBackground(false)

            val xAxis = expenseBarChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            xAxis.granularity = 1f
            xAxis.labelCount = categories.size

            val leftAxis = expenseBarChart.axisLeft
            leftAxis.setLabelCount(6)
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            leftAxis.spaceTop = 15f
            leftAxis.axisMinimum = 0f

            val rightAxis = expenseBarChart.axisRight
            rightAxis.setDrawGridLines(false)
            rightAxis.setLabelCount(6)
            rightAxis.spaceTop = 15f
            rightAxis.axisMinimum = 0f

            val l = expenseBarChart.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            l.orientation = Legend.LegendOrientation.HORIZONTAL
            l.setDrawInside(false)
            l.formSize = 8f
            l.formToTextSpace = 4f
            l.xEntrySpace = 6f
        }

        private fun updateBarChart() {
            val monthStart = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(2024 - 1900, selectedMonth, 1)) //year hardcoded
            val monthEnd = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(2024 - 1900, selectedMonth + 1, 0))  // Last day of month

            val entries = ArrayList<BarEntry>()
            val barLabels = ArrayList<String>()

            var maxExpense = 0f

            for (i in categories.indices) {
                val category = categories[i]
                val totalForCategoryLiveData = viewModel.getTotalForCategory(category.id, monthStart, monthEnd)
                totalForCategoryLiveData.observe(viewLifecycleOwner) { total ->
                    val totalFloat = total?.toFloat() ?: 0f
                    entries.add(BarEntry(totalFloat, i))
                    barLabels.add(category.name)

                    if (totalFloat > maxExpense) {
                        maxExpense = totalFloat
                    }

                    // Check if all data is loaded
                    if (entries.size == categories.size) {
                        val dataSet = BarDataSet(entries, "Category Expenses")
                        dataSet.color = Color.rgb(104, 241, 175)
                        dataSet.valueTextColor = Color.BLACK
                        dataSet.valueTextSize = 16f

                        val data = BarData(barLabels, dataSet)
                        data.setValueFormatter(object : ValueFormatter() {
                            override fun getFormattedValue(value: Float): String {
                                return "R${String.format("%.2f", value)}"
                            }
                        })

                        expenseBarChart.data = data
                        expenseBarChart.xAxis.values = barLabels
                        expenseBarChart.axisLeft.axisMaximum = maxExpense * 1.2f
                        expenseBarChart.notifyDataSetChanged()
                        expenseBarChart.invalidate()
                    }
                }
            }
        }
    }
}