package kevin.jo.ramos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kevin.jo.ramos.Model.CalculatorLogic
import kevin.jo.ramos.Model.HistoryRepository

class MainViewModel : ViewModel() {
    val historyRepository = HistoryRepository()

    val currentExpression: LiveData<String> = CalculatorLogic.currentExpression
    val currentHistoryString: LiveData<String> = CalculatorLogic.currentHistoryString
    val currentAnswerString: LiveData<String> = CalculatorLogic.currentAnswerString

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





}