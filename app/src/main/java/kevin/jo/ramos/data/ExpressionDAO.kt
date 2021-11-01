package kevin.jo.ramos.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpressionDAO {
    @Query("SELECT * FROM Expression")
    fun getAll(): List<Expression>

    @Insert
    fun insertAll(vararg expression: Expression)

    @Delete
    fun delete(expression: Expression)

}