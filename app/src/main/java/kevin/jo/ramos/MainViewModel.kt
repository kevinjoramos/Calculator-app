package kevin.jo.ramos

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kevin.jo.ramos.Model.CalculatorLogic
import kevin.jo.ramos.Model.HistoryRepository
import kevin.jo.ramos.data.AppDatabase
import kevin.jo.ramos.data.Expression
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val expressionData: LiveData<List<Expression>>
    private val historyRepository: HistoryRepository

    init {
        val expressionDao = AppDatabase.getDatabase(application).expressionDao()
        historyRepository = HistoryRepository(expressionDao)
        expressionData = historyRepository.readAllData
    }

    val currentExpression: LiveData<String> = CalculatorLogic.currentExpression
    val currentHistoryList: LiveData<MutableList<String>> = CalculatorLogic.currentHistoryList
    val currentAnswerString: LiveData<String> = CalculatorLogic.currentAnswerString

    fun addExpressionToDatabase(expression: Expression) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.addExpression(expression)
        }
    }

    fun removeExpressionFromDatabase(expression: Expression) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.deleteExpression(expression)
        }
    }

    //Number Buttons:
    fun onPushNumberButton(char: String) {
        CalculatorLogic.onPushNumberButton(char)
    }

    //Utility Buttons:
    fun onPushButtonClear() {
        CalculatorLogic.onPushButtonClear()
    }

    fun onPushButtonDelete() {
        CalculatorLogic.onPushButtonDelete()
    }

    fun onPushButtonPeriod() {
        CalculatorLogic.onPushButtonPeriod()
    }

    //Operator Buttons:
    fun onPushButtonOperator(char: String) {
        CalculatorLogic.onPushButtonOperator(char)
    }

    fun onPushButtonPercent() {
        CalculatorLogic.onPushButtonPercent()
    }

    fun onPushButtonEquals() {
        CalculatorLogic.onPushButtonEquals()
    }

    fun removeHistoryString(position: Int) {
        CalculatorLogic.removeHistoryString(position)

    }





}