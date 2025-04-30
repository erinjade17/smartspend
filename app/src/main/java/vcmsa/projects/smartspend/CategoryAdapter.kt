package vcmsa.projects.smartspend

import android.view.LayoutInflater
import android.view.LayoutInflater.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.filament.View



class CategoryAdapter(private var categories: List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

        class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val categoryNameTextView: TextView = itemView.findViewById(R.id.categoryNameTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            val view = from(parent.context).inflate(R.layout.item_category, parent, false)
            return CategoryViewHolder(view)
        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            val category = categories[position]
            holder.categoryNameTextView.text = category.name
        }

        override fun getItemCount(): Int {
            return categories.size
        }

        fun updateCategories(newCategories: List<Category>) {
            categories = newCategories
            notifyDataSetChanged()
        }
    }


