package vcmsa.projects.smartspend

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

class CategoryDao {
    @Dao
    interface CategoryDao {
        @Query("SELECT * FROM categories")
        suspend fun getAll(): List<Category>

        @Query("SELECT * FROM categories WHERE id = :id")
        suspend fun getById(id: Int): Category

        @Insert
        suspend fun insert(category: Category)

        @Update
        suspend fun update(category: Category)

        @Delete
        suspend fun delete(category: Category)
    }
}