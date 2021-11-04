package kevin.jo.ramos.Model

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import kevin.jo.ramos.data.AppDatabase
import kevin.jo.ramos.data.Expression
import kevin.jo.ramos.data.ExpressionDAO

class HistoryRepository(private val expressionDAO: ExpressionDAO) {
    val readAllData: LiveData<List<Expression>> = expressionDAO.readAllData()

    suspend fun addExpression(expression: Expression) {
        expressionDAO.insert(expression)
    }
}