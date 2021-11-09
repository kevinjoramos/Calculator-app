package kevin.jo.ramos.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object CalculatorLogic {
    //Value used to know if operation is over, and if a new one can start.
    private var isReadyForReset = false

    //The expression being typed into the calculator by user.
    private val operationString = MutableLiveData<String>()
    val readOperationString: LiveData<String>
            get() = operationString

    //The expression containing the answer to the expression.
    private val computationString = MutableLiveData<String>()
    val readComputationString: LiveData<String>
        get() = computationString

    //List of past expressions with their answers.
    private val recentExpressionsList = MutableLiveData<MutableList<String>>()
    val readRecentExpressionsList: LiveData<MutableList<String>>
        get() = recentExpressionsList

    private const val infixOperatorString: String = "+-×÷"

    init {
        operationString.value = ""
        computationString.value = ""
        recentExpressionsList.value = mutableListOf()
    }

    private fun getValue(): String? {
        return operationString.value
    }

    //Number Buttons:
    fun insertNumber(number: String) {
        val operationValue: String = getValue() ?: return

        // start a new operation if the previous button that was pushed was equals.
        if (isReadyForReset) onButtonAfterEquals(true)

        // enforce a 15 digit limit
        if (separateTerms().last().length > 14) return

        operationString.value = operationValue.plus(number)
        updateAnswerString()
    }

    fun insertSpecialNumber(number: String) {
        val operationValue: String = getValue() ?: return

        // start a new operation if the previous button that was pushed was equals.
        if (isReadyForReset) onButtonAfterEquals(true)

        // enforce special numbers to only be between operators.
        if (!infixOperatorString.contains(operationValue.last())) return

        operationString.value = operationValue.plus(number)
    }

    fun insertInfixOperator(char: String) {
        val operationValue: String = getValue() ?: return

        // guard operator first (cannot start with operator)
        if (operationValue == "") return

        // guard no operators in a row.
        if (infixOperatorString.contains(operationValue.last())) return

        // guard solo decimal
        if (separateTerms().last() == ".") return

        // if operator is inserted after pushing equals, we continue with the previous
        // expression.
        if (isReadyForReset) onButtonAfterEquals(false)

        operationString.value = operationString.value.plus(char)

    }

    fun insertPrefixOperator(operator: String) {


    }

    fun performPercent() {
        val operationValue: String = getValue() ?: return

        // guard no operands
        if (operationValue == "") return

        // guard performing percent on operator instead of operand.
        if (infixOperatorString.contains(operationValue.last())) return

        val terms = separateTerms()
        val percentage: Float = terms.last().toFloat().div(100)
        val offset = terms.last().length

        val value = operationValue.dropLast(offset).plus(percentage.toString())

        operationString.value = value
    }



    //Utility Buttons:
    fun clear() {
        //if the strings are empty, empty the recent history.
        isReadyForReset = false
        if (operationString.value == "") recentExpressionsList.value = mutableListOf()

        //clear interface
        operationString.value = ""
        computationString.value = ""
    }

    fun delete() {
        val operationValue: String = getValue() ?: return

        // guard deleting from empty string.
        if (operationValue == "") return

        operationString.value = operationValue.dropLast(1)
    }

    fun insertPeriod() {
        val operationValue: String = getValue() ?: return
        val separatedTerms: List<String> = separateTerms()

        //guard more than 1 decimal point in a operand.
        if (separatedTerms.last().contains(".")) return

        operationString.value = operationString.value.plus(".")

    }

    fun open2ndMenu() {

    }

    fun changeUnits() {

    }

    fun insertParenthesis(parenthesis: String) {
        val operationValue: String = getValue() ?: return

        if (parenthesis == "(") {
            operationString.value = operationValue.plus("(")
            return
        }

        //guard placing ")" with no "(" before.
        var open = 0
        for (character in operationValue) {
            if (character == '(') { open += 1; continue }
            if (character == ')') { open -= 1; continue }
        }

        if (open < 1) return
        operationString.value = operationValue.plus(")")
    }

    //Equals Function
    fun performEquals() {
        // guard performing equals on empty string.
        if (operationString.value == "") return

        updateAnswerString()
        updateHistoryString()
        isReadyForReset = true

    }

    private fun onButtonAfterEquals(isNumberButton: Boolean) {
        val computationValue: String = computationString.value ?: return
        isReadyForReset = false

        // when user enters a number after pushing equals, immediately start
        // new operation.
        if (isNumberButton) { clear(); return }

        // when user enters operator after pushing equals, collapse the expression
        // and continue.
        val answer = computationValue
        clear()
        operationString.value = answer
    }

    private fun separateTerms(operation: String = "start",
                              terms: List<String> = listOf("")): List<String> {

        // when we start the operation, get the value of operationString, and
        // call the function again recursively.
        if (operation == "start") {
            val operationValue = getValue() ?: return separateTerms("start", terms)
            return separateTerms(operationValue, terms)
        }

        val numbers = ".1234567890"

        // base case: we have cycled through all characters
        if (operation.isEmpty()) return terms

        val next = operation.drop(1)
        val indexLast = terms.lastIndex

        // when first character in operation is number, append to last string in
        // terms
        if (numbers.contains(operation[0])) {
            val number = operation[0]
            val updatedTerms = terms.take(indexLast) + (terms.last().plus(number))
            return separateTerms(next, updatedTerms)
        }

        // when first character is operator, append to terms.
        val operator: String = operation[0].toString()
        val updatedTerms = terms.plus(operator).plus("")
        return separateTerms(next, updatedTerms)
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
            if (infixOperatorString.contains(postFixExpression[i])) {
                if (!infixOperatorString.contains(postFixExpression[i-1])
                    && !infixOperatorString.contains(postFixExpression[i-2])) {
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
                    break
                }
            }
        }

        return computePostFix(newExpression)
    }

    //Answer Text View
    fun updateAnswerString(){

        val separatedTerms: List<String> = separateTerms()
        if (separatedTerms.size == 1) { computationString.value = separatedTerms[0]; return }

        val postFixExpression: List<String> = infixToPostfix(separatedTerms)
        val computation: Float = computePostFix(postFixExpression)

        computationString.value = computation.toString()
    }

    //History Text View
    private fun updateHistoryString() {
        val operation = operationString.value
        val answer = computationString.value
        val mList = recentExpressionsList.value?.reverse()

        recentExpressionsList.value?.add("$operation = $answer")
    }

    fun removeHistoryString(position: Int) {
        recentExpressionsList.value?.removeAt(position)

    }

}