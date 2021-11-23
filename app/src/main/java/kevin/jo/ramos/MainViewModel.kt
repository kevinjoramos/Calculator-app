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
    val currentExpression: LiveData<MutableList<String>> = CalculatorLogic.readTermList
    val currentHistoryList: LiveData<MutableList<String>> = CalculatorLogic.readRecentExpressionsList
    val currentAnswerString: LiveData<String> = CalculatorLogic.readComputationString
    val currentUnitType: LiveData<String> = CalculatorLogic.readTrigUnits

    init {
        val expressionDao = AppDatabase.getDatabase(application).expressionDao()
        historyRepository = HistoryRepository(expressionDao)
        expressionData = historyRepository.readAllData
    }

    //DATABASE OPERATIONS
    fun addExpressionToDatabase(expression: Expression) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.addExpression(expression)
        }
    }

    fun removeExpressionFromDatabase(operationString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepository.deleteExpression(operationString)
        }
    }

    //CALCULATOR OPERATIONS
    fun insertNumber(char: String) {
        CalculatorLogic.insertNumber(char)
    }

    fun insertSpecialNumber(char: String) {
        CalculatorLogic.insertSpecialNumber(char)
    }

    fun insertInfixOperator(char: String) {
        CalculatorLogic.insertInfixOperator(char)
    }

    fun insertPrefixOperator(char: String) {
        if (char == "!") return CalculatorLogic.insertFactorial(char)
        if (char == "inv") return CalculatorLogic.insertInverse()
        CalculatorLogic.insertPrefixOperator(char)
    }

    fun requestPercent() {
        CalculatorLogic.performPercent()
    }

    //Utility Buttons:
    fun requestClear() {
        CalculatorLogic.clear()
    }

    fun requestDelete() {
        CalculatorLogic.delete()
    }

    fun insertPoint(char: String) {
        CalculatorLogic.insertPeriod()
    }

    fun requestChangeUnits() {
        CalculatorLogic.changeUnits()
    }


    fun insertParenthesis(char: String) {
        CalculatorLogic.insertParenthesis(char)
    }

    fun requestEquals() {
        CalculatorLogic.performEquals()
    }

    //OTHER
    fun removeHistoryString(position: Int) {
        CalculatorLogic.removeHistoryString(position)

    }





}