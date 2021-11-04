package kevin.jo.ramos.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpressionDAO {
    @Query("SELECT * FROM Expression")
    fun readAllData(): LiveData<List<Expression>>

    @Insert
    suspend fun insert(expression: Expression)

    @Delete
    suspend fun delete(expression: Expression)

}