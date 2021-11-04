package kevin.jo.ramos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Expression(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val expressionString: String,
    val answerString: String
)
