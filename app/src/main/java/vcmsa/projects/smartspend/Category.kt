package vcmsa.projects.smartspend

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

class Category {
    @Entity(tableName = "categories")
    data class Category(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "name") val name: String
    )
}
