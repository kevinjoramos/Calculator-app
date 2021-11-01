package kevin.jo.ramos.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Expression(
    @PrimaryKey val id: Int,
    @ColumnInfo val name: String,
    @ColumnInfo val date: Date,
    @ColumnInfo val expressionString: String,
    @ColumnInfo val answerString: String
)
