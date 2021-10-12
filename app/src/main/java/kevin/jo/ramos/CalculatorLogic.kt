package kevin.jo.ramos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.exp

object CalculatorLogic {
    private val expression = MutableLiveData<String>()
    val currentExpression: LiveData<String>
            get() = expression
    //val expressionAsList: MutableList<String> = mutableListOf()

    init {
        expression.value = ""
    }

    //Number Buttons:
    fun onPushButton0() {
        expression.value = expression.value.plus("0")
    }

    fun onPushButton1() {
        expression.value = expression.value.plus("1")
    }

    fun onPushButton2() {
        expression.value = expression.value.plus("2")
    }

    fun onPushButton3() {
        expression.value = expression.value.plus("3")
    }

    fun onPushButton4() {
        expression.value = expression.value.plus("4")
    }

    fun onPushButton5() {
        expression.value = expression.value.plus("5")
    }

    fun onPushButton6() {
        expression.value = expression.value.plus("6")
    }

    fun onPushButton7() {
        expression.value = expression.value.plus("7")
    }

    fun onPushButton8() {
        expression.value = expression.value.plus("8")
    }

    fun onPushButton9() {
        expression.value = expression.value.plus("9")
    }

    //Utility Buttons:
    fun onPushButtonClear() {
        expression.value = ""
    }

    fun onPushButtonDelete() {

        if (expression.value.equals("")) {
            expression.value = ""
        }

        else {
            expression.value = expression.value?.dropLast(1)
        }
    }

    //
    fun onPushButtonPeriod() {
        //Case where no number has been input yet.
        if (expression.value == "") {
            expression.value = expression.value.plus(".")

        } else {

            //replace all operators with spaces so that the numbers are grouped.
            val operatorsRemoved: String? = expression.value?.replace("+"," ")?.
            replace("-"," ")?.
            replace("✕", " ")?.
            replace("÷", " ")

            //each number is seperated into a list with the split.
            val numbersListed = operatorsRemoved?.split(" ")

            //we check the last number if it already has a period before writing one.
            if (numbersListed?.last()?.contains(".") == false) {
                expression.value = expression.value.plus(".")
            }
        }
    }


    //Operator Buttons:
    fun onPushButtonAdd() {
        val operators: String = "+-✕÷"
        if (!expression.value.equals("")) {
            if (!operators.contains(expression.value?.last().toString()))
                expression.value = expression.value.plus("+")
        }
    }

    fun onPushButtonSubtract() {
        val operators: String = "+-✕÷"
        //check to see if the last char in the expression was something
        //in the list of operators
        if (!expression.value.equals("")) {
            if (!operators.contains(expression.value?.last().toString()))
                expression.value = expression.value.plus("-")
        }
    }

    fun onPushButtonMultiply() {
        val operators: String = "+-✕÷"
        //check to see if the last char in the expression was something
        //in the list of operators
        if (!expression.value.equals("")) {
            if (!operators.contains(expression.value?.last().toString()))
                expression.value = expression.value.plus("✕")
        }
    }

    fun onPushButtonDivide() {
        val operators: String = "+-✕÷"
        //check to see if the last char in the expression was something
        //in the list of operators
        if (!expression.value.equals("")) {
            if (!operators.contains(expression.value?.last().toString()))
                expression.value = expression.value.plus("÷")
        }
    }

    fun onPushButtonPercent() {
        val operators: String = "+-✕÷"

        //Case where no number has been input yet.
        if (expression.value == "") {
            expression.value = ""

        } else if (operators.contains(expression.value?.last().toString())) {
            //Nothing happens when the most recent character typed in was
            //an operator
        } else {


            //replace all operators with spaces so that the numbers are grouped.
            val operatorsRemoved: String? =
                expression.value?.replace("+", " ")?.replace("-", " ")?.replace("✕", " ")
                    ?.replace("÷", " ")


            //each number is separated into a list with the split.
            if (operatorsRemoved != null) {
                val numbersListed = operatorsRemoved.split(" ")
                val percentage = numbersListed.last().toFloat().div(100)

                //we need the length of the original number so we now how much
                //of the string we need to replace.
                val offset = numbersListed.last().length


                expression.value = expression.value?.dropLast(offset).plus(percentage.toString())
            }
        }
    }

    //Equals Function
    fun onPushButtonEquals() {

    }
}