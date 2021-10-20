package kevin.jo.ramos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val currentExpression: LiveData<String> = CalculatorLogic.currentExpression
    val currentHistoryString: LiveData<String> = CalculatorLogic.currentHistoryString
    val currentAnswerString: LiveData<String> = CalculatorLogic.currentAnswerString

    //Number Buttons:
    fun onPushButton0() {
        CalculatorLogic.onPushButton0()
    }

    fun onPushButton1() {
        CalculatorLogic.onPushButton1()
    }

    fun onPushButton2() {
        CalculatorLogic.onPushButton2()
    }

    fun onPushButton3() {
        CalculatorLogic.onPushButton3()
    }

    fun onPushButton4() {
        CalculatorLogic.onPushButton4()
    }

    fun onPushButton5() {
        CalculatorLogic.onPushButton5()
    }

    fun onPushButton6() {
        CalculatorLogic.onPushButton6()
    }

    fun onPushButton7() {
        CalculatorLogic.onPushButton7()
    }

    fun onPushButton8() {
        CalculatorLogic.onPushButton8()
    }

    fun onPushButton9() {
        CalculatorLogic.onPushButton9()
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
    fun onPushButtonAdd() {
        CalculatorLogic.onPushButtonAdd()
    }

    fun onPushButtonSubtract() {
        CalculatorLogic.onPushButtonSubtract()
    }

    fun onPushButtonMultiply() {
        CalculatorLogic.onPushButtonMultiply()
    }

    fun onPushButtonDivide() {
        CalculatorLogic.onPushButtonDivide()
    }

    fun onPushButtonPercent() {
        CalculatorLogic.onPushButtonPercent()
    }

    fun onPushButtonEquals() {
        CalculatorLogic.onPushButtonEquals()
    }





}