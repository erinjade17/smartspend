package vcmsa.projects.smartspend

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

class Expense {
    @Entity(tableName = "expenses")
    data class Expense(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "date") val date: String,
        @ColumnInfo(name = "start_time") val startTime: String,
        @ColumnInfo(name = "end_time") val endTime: String,
        @ColumnInfo(name = "description") val description: String,
        @ColumnInfo(name = "category_id") val categoryId: Int,
        @ColumnInfo(name = "photo_path") val photoPath: String? // Path to the stored photo
    )
}