package vcmsa.projects.smartspend

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

class UserDao {
    @Dao
    interface UserDao {
        @Query("SELECT * FROM users WHERE username = :username")
        suspend fun findByUsername(username: String): User?

        @Insert
        suspend fun insert(user: User)
    }
}