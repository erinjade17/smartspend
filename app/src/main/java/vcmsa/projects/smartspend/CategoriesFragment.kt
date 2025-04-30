package vcmsa.projects.smartspend

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

    class CategoriesFragment : Fragment() {
        private lateinit var viewModel: HomeViewModel
        private lateinit var categoriesRecyclerView: RecyclerView
        private lateinit var categoryAdapter: CategoryAdapter
        private lateinit var addCategoryButton: Button
        private lateinit var dialog: AlertDialog

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.fragment_categories, container, false)
            categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView)
            addCategoryButton = view.findViewById(R.id.addCategoryButton)
            return view
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

            // Set up RecyclerView and Adapter
            categoryAdapter = CategoryAdapter(emptyList())
            categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            categoriesRecyclerView.adapter = categoryAdapter

            // Observe the categories LiveData
            viewModel.categories.observe(viewLifecycleOwner) { categories ->
                categoryAdapter.updateCategories(categories)
            }

            // Load categories
            viewModel.loadHomePageData()

            // Set click listener for the add category button
            addCategoryButton.setOnClickListener {
                showAddCategoryDialog()
            }
        }

        private fun showAddCategoryDialog() {
            val builder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.dialog_add_category, null)
            builder.setView(dialogView)

            val categoryNameEditText = dialogView.findViewById<EditText>(R.id.categoryNameEditText)

            builder.setPositiveButton("Add") { dialog, _ ->
                val categoryName = categoryNameEditText.text.toString()
                if (categoryName.isNotBlank()) {
                    viewModel.addCategory(categoryName)
                    dialog.dismiss()
                } else {
                    Toast.makeText(requireContext(), "Please enter a category name", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            dialog = builder.create()
            dialog.show()
        }
    }

}