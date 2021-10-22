package kevin.jo.ramos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.exp

object CalculatorLogic {
    //Value used to know if operation is over, and if a new one can start.
    private val _isReadyForReset = MutableLiveData<Boolean>()
    val isReadyForReset: LiveData<Boolean>
            get() = isReadyForReset

    //The expression being typed into the calculator by user.
    private val expression = MutableLiveData<String>()
    val currentExpression: LiveData<String>
            get() = expression

    //The expression containing the answer to the expression.
    private val answerString = MutableLiveData<String>()
    val currentAnswerString: LiveData<String>
        get() = answerString

    //List of past expressions with their answers.
    private val historyString = MutableLiveData<String>()
    val currentHistoryString: LiveData<String>
        get() = historyString

    private const val operatorString: String = "+-×÷"

    init {
        expression.value = "0"
        answerString.value = ""
        historyString.value = ""
        _isReadyForReset.value = false
    }

    //Number Buttons:
    fun onPushNumberButton(char: String) {
        resetDisplay()
        expression.value = expression.value.plus(char)
        updateAnswerString()

    }

    //Utility Buttons:
    fun onPushButtonClear() {
        if (_isReadyForReset.value == true) {
            updateHistoryString()
            _isReadyForReset.value = false
        };
        if (expression.value == "") historyString.value = ""
        expression.value = ""
        answerString.value = ""
    }

    fun onPushButtonDelete() {
        if (expression.value.equals("")) { return }

        expression.value = expression.value?.dropLast(1)
    }


    fun onPushButtonPeriod() {
        val separatedTerms: List<String> = separateTerms()

        //we check the last number if it already has a period before writing one.
        if (separatedTerms.last().contains(".") == false) {
            expression.value = expression.value.plus(".")
        }
    }

    //Operator Buttons:
    fun onPushButtonOperator(char: String) {
        if (_isReadyForReset.value == true) {
            updateHistoryString()
            _isReadyForReset.value = false
        }

        //case where no operands were input
        if (expression.value.equals("")) { return }

        //case preventing two operators in a row.
        if (operatorString.contains(expression.value?.last().toString())) { return }

        //case where the current operand only has a point.
        if (separateTerms().last() == ".") { return }

        expression.value = expression.value.plus(char)

    }

    fun onPushButtonPercent() {


        //Case where no number has been input yet.
        if (expression.value == "") {
            expression.value = ""

        } else if (operatorString.contains(expression.value?.last().toString())) {
            //Nothing happens when the most recent character typed in was
            //an operator
        } else {


            //replace all operators with spaces so that the numbers are grouped.
            val operatorsRemoved: String? =
                expression.value?.replace("+", " ")?.replace("-", " ")?.replace("×", " ")
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
        updateAnswerString()
        _isReadyForReset.value = true
    }

    private fun separateTerms(): List<String> {
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
                else {
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

            if (term == "×") {
                if (operatorStack.isEmpty()) operatorStack.add(term)
                else if ("-+÷×".contains(operatorStack.last())) {
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

            if (operator == "×") {
                answer = operand1.times(operand2)
            }

            return answer.toFloat()


        }

        //Search for first Number-Number-Operator couple, and resolve it
        for (i in 2 until postFixExpression.size) {
            if (operatorString.contains(postFixExpression[i])) {
                if (!operatorString.contains(postFixExpression[i-1])
                    && !operatorString.contains(postFixExpression[i-2])) {
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

                    if (operator == "×") {
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

    //Answer Text View
    fun updateAnswerString(){

        val separatedTerms: List<String> = separateTerms()
        if (separatedTerms.size == 1) { answerString.value = "= " + separatedTerms[0]; return }

        val postFixExpression: List<String> = infixToPostfix(separatedTerms)
        val computation: Float = computePostFix(postFixExpression)

        answerString.value = "= ".plus(computation.toString())
        Log.i("CalculatorLogic", "Update Answer String ${answerString.value}")

    }

    //History Text View
    private fun updateHistoryString() {
        val operation = expression.value
        val answer = answerString.value

        historyString.value += operation + answer + "\n\n"

    }

    private fun resetDisplay() {
        if ( _isReadyForReset.value == false ) { return }
        updateHistoryString()
        onPushButtonClear()
        _isReadyForReset.value = false
    }
}