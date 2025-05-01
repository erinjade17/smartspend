package vcmsa.projects.smartspend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ExpenseAdapter(private var expenses: List<Expense>, private val categories: List<Category>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val amountTextView: TextView = itemView.findViewById(R.id.amountTextView)
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        val photoImageView: ImageView = itemView.findViewById(R.id.photoImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.dateTextView.text = "Date: ${expense.date}"
        holder.descriptionTextView.text = "Description: ${expense.description}"
        holder.amountTextView.text = "Amount: R${String.format("%.2f", expense.amount)}"

        // Get category name from ID using the provided categories list
        val categoryName = categories.find { it.id == expense.categoryId }?.name ?: "Unknown"
        holder.categoryTextView.text = "Category: $categoryName"

        if (expense.photoPath != null) {
            val requestOptions = RequestOptions()
                .error(R.drawable.ic_launcher_background) //show default
            Glide.with(holder.photoImageView.context)
                .load(expense.photoPath)
                .apply(requestOptions)
                .into(holder.photoImageView)
            holder.photoImageView.visibility = View.VISIBLE
        } else {
            holder.photoImageView.visibility = View.GONE
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
