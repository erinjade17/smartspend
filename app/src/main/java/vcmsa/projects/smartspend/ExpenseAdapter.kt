package vcmsa.projects.smartspend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ExpenseAdapter(private var expenses: List<Expense>) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

        class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
            val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
            val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
            val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
            val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView) // Add this
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
            return ExpenseViewHolder(view)
        }

        override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
            val expense = expenses[position]
            holder.dateTextView.text = "Date: ${expense.date}"
            holder.descriptionTextView.text = "Description: ${expense.description}"
            holder.amountTextView.text = "Amount: R${String.format("%.2f", expense.amount)}"
            // Get category name from ID.  You might want to store the name in the Expense entity.
            //holder.categoryTextView.text = "Category: ${expense.categoryId}"
            holder.categoryTextView.text = "Category: ${expense.categoryId}"

            if (expense.photoPath != null) {
                // Load the image using a library like Glide or Picasso
                // Example using Glide (add Glide dependency to build.gradle):
                Glide.with(holder.photoImageView.context)
                    .load(expense.photoPath)
                    .into(holder.photoImageView)
                holder.photoImageView.visibility = View.VISIBLE // Make sure it's visible
            } else {
                holder.photoImageView.visibility = View.GONE // Hide if no photo
            }
        }

        override fun getItemCount(): Int {
            return expenses.size
        }

        fun updateExpenses(newExpenses: List<Expense>) {
            expenses = newExpenses
            notifyDataSetChanged()
        }
    }

