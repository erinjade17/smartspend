package vcmsa.projects.smartspend

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val passwordHash: String,  // Correct property name and type
    val email: String? = null // Add other fields as necessary
)
