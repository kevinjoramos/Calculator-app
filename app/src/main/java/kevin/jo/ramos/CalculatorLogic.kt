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
        val operatorsString: String = "+-✕÷"
        if (expression.value == "") { }
        else if (operatorsString.contains(expression.value?.last().toString() ) ) { }
        else {
            val separatedTerms: List<String> = separateTerms()
            val postFixExpression: List<String> = infixToPostfix(separatedTerms)
            val computation: Float = computePostFix(postFixExpression)

            expression.value = computation.toString()
        }


    }


    private fun separateTerms(): List<String> {
        val operatorsString: String = "+-✕÷"
        val numbersString: String = "1234567890."
        val termsList: MutableList<String> = mutableListOf("")

        if (expression.value != null) {
            val value = expression.value

            //Main Code: Convert the string into a list of strings
            //separating operators and operands.

            if (value != null) {

                var j: Int = 0
                for (i in 0 until value.length) {


                    if (numbersString.contains(value[i]) ) {
                        termsList[j] = termsList[j].plus(value[i])

                    } else {
                        termsList.addAll(listOf<String>(value[i].toString(), ""))
                        j += 2
                    }
                }
            }
        }
        Log.i("CalculatorLogic","$termsList")
        return termsList
    }

    fun infixToPostfix(separatedTerms: List<String>): List<String> {

        val operatorsString: String = "-+÷✕"
        val operatorStack = mutableListOf<String>()
        val postfixExpression = mutableListOf<String>()

        for (term in separatedTerms) {
            if (term == "+") {
                if (operatorStack.isEmpty()) operatorStack.add(term)
                else if ("-+".contains(operatorStack.last())) {
                    operatorStack.add(term)
                } else {
                    while (operatorStack.size != 0) {
                        postfixExpression.add(operatorStack.removeLast())
                    }
                    operatorStack.add(term)
                }
                continue
            }

            if (term == "-") {
                if (operatorStack.isEmpty()) operatorStack.add(term)
                else if ("-".contains(operatorStack.last())) {
                    operatorStack.add(term)
                } else {
                    while (operatorStack.size != 0) {
                        postfixExpression.add(operatorStack.removeLast())
                    }
                    operatorStack.add(term)
                }
                continue
            }

            if (term == "÷") {
                if (operatorStack.isEmpty()) operatorStack.add(term)
                else if ("-+÷".contains(operatorStack.last())) {
                    operatorStack.add(term)
                } else {
                    while (operatorStack.size != 0) {
                        postfixExpression.add(operatorStack.removeLast())
                    }
                    operatorStack.add(term)
                }
                continue
            }

            if (term == "✕") {
                if (operatorStack.isEmpty()) operatorStack.add(term)
                else if ("-+÷✕".contains(operatorStack.last())) {
                    operatorStack.add(term)
                }
                continue
            }

            postfixExpression.add(term)
        }

        while (operatorStack.size != 0) {
            postfixExpression.add(operatorStack.removeLast())
        }

        Log.i("CalculatorLogic","$postfixExpression")

        return postfixExpression

    }

    fun computePostFix(postFixExpression: List<String>): Float {
        val operatorsString: String = "-+÷✕"
        val newExpression: MutableList<String> = mutableListOf()
        var answer: Float = 0.0F

        //Base Case
        if (postFixExpression.size == 3) {
            val operand1 = postFixExpression[0].toFloat()
            val operand2 = postFixExpression[1].toFloat()
            val operator = postFixExpression[2]

            if (operator == "+") {
                answer = operand1.plus(operand2)
            }

            if (operator == "-") {
                answer = operand1.minus(operand2)
            }

            if (operator == "÷") {
                answer = operand1.div(operand2)
            }

            if (operator == "✕") {
                answer = operand1.times(operand2)
            }

            return answer.toFloat()


        }

        //Search for first Number-Number-Operator couple, and resolve it
        for (i in 2 until postFixExpression.size) {
            if (operatorsString.contains(postFixExpression[i])) {
                if (!operatorsString.contains(postFixExpression[i-1])
                    && !operatorsString.contains(postFixExpression[i-2])) {
                    val operand1: Float = postFixExpression[i-2].toFloat()
                    val operand2: Float = postFixExpression[i-1].toFloat()
                    val operator: String = postFixExpression[i]

                    if (operator == "+") {
                        answer = operand1.plus(operand2)
                    }

                    if (operator == "-") {
                        answer = operand1.minus(operand2)
                    }

                    if (operator == "÷") {
                        answer = operand1.div(operand2)
                    }

                    if (operator == "✕") {
                        answer = operand1.times(operand2)
                    }
                    newExpression.addAll(postFixExpression.take(i - 2))

                    newExpression.add(answer.toString())

                    newExpression.addAll(postFixExpression.drop(i + 1))

                    Log.i("CalculatorLogic","$newExpression")
                    break
                }
            }
        }

        return computePostFix(newExpression)
    }
}