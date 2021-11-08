package kevin.jo.ramos.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ExpressionDAO {
    @Query("SELECT * FROM Expression")
    fun readAllData(): LiveData<List<Expression>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expression: Expression)

    @Query("DELETE FROM Expression WHERE expressionString = :expressionString")
    suspend fun delete(expressionString: String)

}