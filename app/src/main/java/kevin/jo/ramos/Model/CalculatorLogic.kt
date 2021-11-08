package kevin.jo.ramos.Model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object CalculatorLogic {
    //Value used to know if operation is over, and if a new one can start.
    private var _isReadyForReset = false

    //The expression being typed into the calculator by user.
    private val expression = MutableLiveData<String>()
    val currentExpression: LiveData<String>
            get() = expression

    //The expression containing the answer to the expression.
    private val answerString = MutableLiveData<String>()
    val currentAnswerString: LiveData<String>
        get() = answerString

    //List of past expressions with their answers.
    private val recentHistoryList = MutableLiveData<MutableList<String>>()
    val currentHistoryList: LiveData<MutableList<String>>
        get() = recentHistoryList

    private const val operatorString: String = "+-×÷"

    init {
        expression.value = ""
        answerString.value = ""
        recentHistoryList.value = mutableListOf()
    }

    //Number Buttons:
    fun onPushNumberButton(char: String) {
        //If number button is pushed after having recently pushing equals.
        if (_isReadyForReset == true) onButtonAfterEquals(true)

        expression.value = expression.value.plus(char)
        updateAnswerString()

    }

    //Utility Buttons:
    fun onPushButtonClear() {
        //if the strings are empty, empty the recent history.
        _isReadyForReset = false
        if (expression.value == "") recentHistoryList.value = mutableListOf()

        //clear interface
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

        //case where no operands where input
        if (expression.value.equals("")) { return }

        //case preventing two operators in a row.
        if (operatorString.contains(expression.value?.last().toString())) { return }

        //case where the current operand only has a point.
        if (separateTerms().last() == ".") { return }

        //If number button is pushed after having recently pushing equals.
        if (_isReadyForReset == true) onButtonAfterEquals(false)

        expression.value = expression.value.plus(char)

    }

    fun onPushButtonPercent() {
        //Case where no number has been input yet.

        if (expression.value == "") return

        if (operatorString.contains(expression.value?.last().toString())) return
            //Nothing happens when the most recent character typed in was
            //an operator

        val terms = separateTerms()
        val percentage: Float = terms.last().toFloat().div(100)
        val offset = terms.last().length

        expression.value = expression.value?.dropLast(offset).plus(percentage.toString() )
    }

    //Equals Function
    fun onPushButtonEquals() {
        if (expression.value == "") return
        updateAnswerString()
        updateHistoryString()
        _isReadyForReset = true

    }

    private fun onButtonAfterEquals(isNumberButton: Boolean) {
        _isReadyForReset = false

        if (isNumberButton) {
            onPushButtonClear()
            return
        }

        val answer = answerString.value?.replace(" ", "")?.
                                        replace("=", "")
        onPushButtonClear()
        expression.value = answer

        //on next button push
        //if the button is a number, clear the fields and type new operator
        //if the button is an operator, clear the operator field and put the answer there
        // with the new operator

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
        if (separatedTerms.size == 1) { answerString.value = separatedTerms[0]; return }

        val postFixExpression: List<String> = infixToPostfix(separatedTerms)
        val computation: Float = computePostFix(postFixExpression)

        answerString.value = computation.toString()
    }

    //History Text View
    private fun updateHistoryString() {
        val operation = expression.value
        val answer = answerString.value
        val mList = recentHistoryList.value?.reverse()

        recentHistoryList.value?.add("$operation = $answer")
    }

    fun removeHistoryString(position: Int) {
        recentHistoryList.value?.removeAt(position)

    }

}