package kevin.jo.ramos.Model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.math.*
import kotlin.math.sin

object CalculatorLogic {
    //Value used to know if operation is over, and if a new one can start.
    private var isReadyForReset = false
    private val trigUnits = MutableLiveData<String>()
    val readTrigUnits: LiveData<String>
        get() = trigUnits

    //The expression being typed into the calculator by user.
    private val TermList = MutableLiveData<MutableList<String>>()
    val readTermList: LiveData<MutableList<String>>
        get() = TermList

    //The expression containing the answer to the expression.
    private val computationString = MutableLiveData<String>()
    val readComputationString: LiveData<String>
        get() = computationString

    //List of past expressions with their answers.
    private val recentExpressionsList = MutableLiveData<MutableList<String>>()
    val readRecentExpressionsList: LiveData<MutableList<String>>
        get() = recentExpressionsList

    //infixOperatorString: String = "+-×÷"

    init {
        TermList.value = mutableListOf()
        computationString.value = ""
        recentExpressionsList.value = mutableListOf()
        trigUnits.value = "degrees"
    }

    private fun getValue(): MutableList<String>? {
        return TermList.value
    }

    //Number Buttons:
    fun insertNumber(number: String) {
        val terms = getValue() ?: return

        // start a new operation if the previous button that was pushed was equals.
        if (isReadyForReset) onButtonAfterEquals(true)

        if (terms.isEmpty()) {
            terms.add(number)
            TermList.value = terms
            return
        }

        // enforce a 15 digit limit
        if (terms.last().length > 14) return


        // when previous charter was a number, continue appending to number.
        if (".0123456789".contains(terms.last().last())) { //breaks when deleting to TODO
            terms[terms.lastIndex] += number
            TermList.value = terms
            return
        }

        // when only minus sign, we start with negative number.
        if (terms.size == 1 && terms.last() == "-") {
            terms[terms.lastIndex] += number
            TermList.value = terms
            return
        }

        if (terms.last() == "-" && terms[terms.size - 2] == "(") {
            terms[terms.lastIndex] += number
            TermList.value = terms
            return
        }

        // when previous char is operator, we append the number as a new item
        // to the list.
        terms.add(number)
        TermList.value = terms

        try {
            compute()
        } catch (e: Exception) {
            Log.i("CalculatorLogic","exception occurred: $e")
            computationString.value = "Error"
        }
    }

    fun insertSpecialNumber(number: String) {
        val terms = getValue() ?: return

        // start a new operation if the previous button that was pushed was equals.
        if (isReadyForReset) onButtonAfterEquals(true)

        terms.add(number)
        TermList.value = terms

        try {
            compute()
        } catch (e: Exception) {
            Log.i("CalculatorLogic","exception occurred: $e")
            computationString.value = "Error"
        }
    }

    fun insertInfixOperator(char: String) {
        val terms = getValue() ?: return

        // guard operator first (cannot start with operator)
        if (terms.isEmpty()) return

        // guard no operators in a row. replace it
        if ("+-×÷^".contains(terms.last())) {
            terms.removeLast()
            terms.add(char)
            TermList.value = terms
            return
        }

        // if operator is inserted after pushing equals, we continue with the previous
        // expression.
        if (isReadyForReset) onButtonAfterEquals(false)

        terms.add(char)
        TermList.value = terms

    }

    fun insertAdvanceSubtract() {
        val terms = getValue() ?: return

        if (terms.isEmpty()) {
            terms.add("-")
            TermList.value = terms
            return
        }

        if ("+-×÷^".contains(terms.last())) {
            terms.add("(")
            terms.add("-")
            TermList.value = terms
            return
        }

        terms.add("-")
        TermList.value = terms
    }

    fun insertPrefixOperator(operator: String) {
        val terms = getValue() ?: return

        // when the operator is sin, cos, etc.. we need to add "("
        // as well.
        if (listOf("sin", "cos", "tan", "arcsin", "arccos", "arctan", "log", "ln")
                .contains(operator)) {
            terms.add(operator)
            terms.add("(")
            TermList.value = terms
            return
        }

        terms.add(operator)
        TermList.value = terms
    }

    fun insertFactorial(operator: String) {
        val terms = getValue() ?: return

        // guard putting factorial first.
        if (terms.isEmpty()) return

        terms.add(operator)
        TermList.value = terms
    }

    fun insertInverse() {
        val terms = getValue() ?: return

        // guard no operands
        if (terms.isEmpty()) return

        terms.add("^")
        terms.add("(")
        terms.add("-1")
        terms.add(")")

        TermList.value = terms
    }

    fun performPercent() {
        val terms = getValue() ?: return

        // guard no operands
        if (terms.isEmpty()) return

        // guard performing percent on operator instead of operand.
        if ("+-×÷^".contains(terms.last())) return

        val percentage: Float = terms.last().toFloat().div(100)
        terms.removeLast()
        terms.add(percentage.toString())

        TermList.value = terms
    }



    //Utility Buttons:
    fun clear() {
        val terms = getValue() ?: return

        //if the strings are empty, empty the recent history.
        isReadyForReset = false
        if (terms.isEmpty()) recentExpressionsList.value = mutableListOf()

        //clear interface
        TermList.value = mutableListOf()
        computationString.value = ""
    }

    fun delete() {
        val terms = getValue() ?: return

        // guard deleting from empty string.
        if (terms.isEmpty()) return
        terms[terms.lastIndex] = terms.last().dropLast(1)

        TermList.value = terms

        try {
            compute()
        } catch (e: Exception) {
            Log.i("CalculatorLogic","exception occurred: $e")
            computationString.value = "Error"
        }
    }

    fun insertPeriod() {
        val terms = getValue() ?: return

        //guard more than 1 decimal point in a operand.
        if (terms.last().contains(".")) return

        terms[terms.lastIndex] += "."

        TermList.value = terms

        try {
            compute()
        } catch (e: Exception) {
            Log.i("CalculatorLogic","exception occurred: $e")
            computationString.value = "Error"
        }
    }

    fun changeUnits() {
        val unitType = trigUnits.value ?: return

        if (unitType == "degrees") {
            trigUnits.value = "radians"
            return
        }

        trigUnits.value = "degrees"
    }

    fun insertParenthesis(parenthesis: String) {
        val terms = getValue() ?: return

        if (parenthesis == "(") {
            terms.add("(")
            TermList.value = terms
            return
        }

        //guard placing ")" with no "(" before.
        var open = 0
        for (item in terms) {
            if (item == "(") { open += 1; continue }
            if (item == ")") { open -= 1; continue }
        }

        if (open < 1) return
        terms.add(")")
        TermList.value = terms
    }

    //Equals Function
    fun performEquals() {
        val terms = getValue() ?: return

        // guard performing equals on empty string.
        if (terms.isEmpty()) return

        try {
            compute()
        } catch (e: Exception) {
            Log.i("CalculatorLogic","exception occurred: $e")
            computationString.value = "Error"
        }
        //updateHistoryString()
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

        val terms = mutableListOf<String>()
        terms.add(answer)

        TermList.value = terms
    }

    private fun compute() {
        val terms = getValue() ?: return
        val units = trigUnits.value ?: return

        // guard only 1 operand with no operators
        if (terms.size == 1) {
            val computationValue = terms.last()
            computationString.value = computationValue
            return
        }

        var computationValue = mutableListOf<String>()
        var operatorStack = mutableListOf<String>()

        for (item in terms) {

            if (item == "-") {
                if (item === terms[0]) {
                    computationValue.add("0")
                    operatorStack.add("-")
                    continue
                }

                if (operatorStack.isEmpty()) {
                    operatorStack.add(item)
                    continue
                }

                val (stack, computation) = poppingTheStack(operatorStack, computationValue)
                operatorStack = stack
                operatorStack.add(item)
                computationValue = computation
                continue
            }



            if (item == "+") {
                if (operatorStack.isEmpty()) {
                    operatorStack.add(item)
                    continue
                }

                if ("-+".contains(operatorStack.last())) {
                    operatorStack.add(item)
                    continue
                }

                val (stack, computation) = poppingTheStack(operatorStack, computationValue)
                operatorStack = stack
                operatorStack.add(item)
                computationValue = computation
                continue
            }

            if (item == "÷") {
                if (operatorStack.isEmpty()) {
                    operatorStack.add(item)
                    continue
                }

                if ("-+÷".contains(operatorStack.last())) {
                    operatorStack.add(item)
                    continue
                }

                val (stack, computation) = poppingTheStack(operatorStack, computationValue)
                operatorStack = stack
                operatorStack.add(item)
                computationValue = computation
                continue
            }

            if (item == "×") {
                if (operatorStack.isEmpty()) {
                    operatorStack.add(item)
                    continue
                }

                if ("-+÷×".contains(operatorStack.last())) {
                    operatorStack.add(item)
                    continue
                }

                val (stack, computation) = poppingTheStack(operatorStack, computationValue)
                operatorStack = stack
                operatorStack.add(item)
                computationValue = computation
                continue
            }

            if (item == "^") {
                operatorStack.add(item)
                continue
            }

            if (item == "e") { computationValue.add("2.71828183"); continue }

            if (item == "π") { computationValue.add("3.14159265"); continue}

            if (item == "(") { operatorStack.add("("); continue}

            if (item == ")") {
                val (stack, computation) = poppingTheStack(operatorStack, computationValue)
                operatorStack = stack
                computationValue = computation

                // when we reach the end of parent, remove it.
                operatorStack.removeLast()

                // when a trig or log function was called before parent
                // compute it.
                if (operatorStack.last() == "sin") {
                    operatorStack.removeLast()

                    var operand = computationValue.removeLast().toFloat()

                    if (units == "degrees") {
                        operand *= (PI.toFloat() / 180)
                    }

                    val answer = sin(operand)

                    if (answer.mod(1.0) == 0.0) {
                        computationValue.add(answer.toInt().toString())
                    } else {
                        computationValue.add(answer.toString())
                    }
                }

                else if  (operatorStack.last() == "cos") {
                    operatorStack.removeLast()

                    var operand = computationValue.removeLast().toFloat()

                    if (units == "degrees") {
                        operand *= (PI.toFloat() / 180)
                    }

                    val answer = cos(operand)

                    if (answer.mod(1.0) == 0.0) {
                        computationValue.add(answer.toInt().toString())
                    } else {
                        computationValue.add(answer.toString())
                    }
                }

                else if (operatorStack.last() == "tan") {
                    operatorStack.removeLast()

                    var operand = computationValue.removeLast().toFloat()

                    if (units == "degrees") {
                        operand *= (PI.toFloat() / 180)
                    }

                    val answer = tan(operand)

                    if (answer.mod(1.0) == 0.0) {
                        computationValue.add(answer.toInt().toString())
                    } else {
                        computationValue.add(answer.toString())
                    }
                }

                else if (operatorStack.last() == "arcsin") {
                    operatorStack.removeLast()

                    val operand = computationValue.removeLast()
                    val answer = asin(operand.toFloat())

                    if (answer.mod(1.0) == 0.0) {
                        computationValue.add(answer.toInt().toString())
                    } else {
                        computationValue.add(answer.toString())
                    }
                }

                else if (operatorStack.last() == "arccos") {
                    operatorStack.removeLast()

                    val operand = computationValue.removeLast()
                    val answer = acos(operand.toFloat())

                    if (answer.mod(1.0) == 0.0) {
                        computationValue.add(answer.toInt().toString())
                    } else {
                        computationValue.add(answer.toString())
                    }
                }

                else if (operatorStack.last() == "arctan") {
                    operatorStack.removeLast()

                    val operand = computationValue.removeLast()
                    val answer = atan(operand.toFloat())

                    if (answer.mod(1.0) == 0.0) {
                        computationValue.add(answer.toInt().toString())
                    } else {
                        computationValue.add(answer.toString())
                    }
                }

                else if (operatorStack.last() == "log") {
                    operatorStack.removeLast()

                    val operand = computationValue.removeLast()
                    val answer = log10(operand.toFloat())

                    if (answer.mod(1.0) == 0.0) {
                        computationValue.add(answer.toInt().toString())
                    } else {
                        computationValue.add(answer.toString())
                    }
                }

                else if (operatorStack.last() == "ln") {
                    operatorStack.removeLast()

                    val operand = computationValue.removeLast()
                    val answer = ln(operand.toFloat())

                    if (answer.mod(1.0) == 0.0) {
                        computationValue.add(answer.toInt().toString())
                    } else {
                        computationValue.add(answer.toString())
                    }
                }
                continue
            }

            if (item == "!") {
                val factorial = computeFactorial(computationValue.removeLast().toInt())
                computationValue.add(factorial.toString())
                continue
            }

            if (item == "sin") {
                operatorStack.add("sin")
                continue
            }

            if (item == "cos") {
                operatorStack.add("cos")
                continue
            }

            if (item == "tan") {
                operatorStack.add("tan")
                continue
            }

            if (item == "arcsin") {
                operatorStack.add("arcsin")
                continue
            }

            if (item == "arccos") {
                operatorStack.add("arccos")
                continue
            }

            if (item == "arctan") {
                operatorStack.add("arctan")
                continue
            }

            if (item == "log") {
                operatorStack.add("log")
                continue
            }

            if (item == "ln") {
                operatorStack.add("ln")
                continue
            }

            if (item == "√") {
                operatorStack.add("√")
                continue
            }

            Log.i("CalculatorLogic", "Durring: \nnStack: $operatorStack \n computation: $computationValue")

            computationValue.add(item)
        }

        if (operatorStack.isNotEmpty()) {
            val (stack, computation) = poppingTheStack(operatorStack, computationValue)
            computationValue = computation
        }

        computationString.value = computationValue[0]
        Log.i("CalculatorLogic", "End: \nnStack: $operatorStack \n computation: $computationValue")
        return

    }

    fun poppingTheStack(operatorStack: MutableList<String>,
                        computationValue: MutableList<String>):
            Pair<MutableList<String>, MutableList<String>> {

        Log.i("CalculatorLogic", "pop: \nnStack: $operatorStack \n computation: $computationValue")

        // when we pop till the end of the stack exit recursion.
        if (operatorStack.isEmpty()) return Pair(operatorStack, computationValue)

        // when we pop to a "(" character, exit recursion.
        if (operatorStack.last() == "(") {

            return Pair(operatorStack, computationValue)
        }

        // simplify
        val operator = operatorStack.removeLast()
        var answer: Float = 0F

        if (operator == "-") {
            val operand2 = computationValue.removeLast()
            val operand1 = computationValue.removeLast()
            answer = operand1.toFloat().minus(operand2.toFloat())
        }

        else if (operator == "+") {
            val operand2 = computationValue.removeLast()
            val operand1 = computationValue.removeLast()
            answer = operand1.toFloat().plus(operand2.toFloat())
        }

        else if (operator == "÷") {
            val operand2 = computationValue.removeLast()
            val operand1 = computationValue.removeLast()
            answer = operand1.toFloat().div(operand2.toFloat())
        }

        else if (operator == "×") {
            val operand2 = computationValue.removeLast()
            val operand1 = computationValue.removeLast()
            answer = operand1.toFloat().times(operand2.toFloat())
        }

        else if (operator == "^") {
            val operand2 = computationValue.removeLast()
            val operand1 = computationValue.removeLast()
            answer = operand1.toFloat().pow(operand2.toFloat())
        }

        else if (operator == "√") {
            val operand1 = computationValue.removeLast()
            answer = sqrt(operand1.toFloat())
        }

        // when answer can be integer cast to integer.
        if (answer.mod(1.0) == 0.0) {
            computationValue.add(answer.toInt().toString())
        } else {
            computationValue.add(answer.toString())
        }

        return poppingTheStack(operatorStack, computationValue)
    }

    private fun computeFactorial(number: Int): Int {
        if (number == 0) return 1
        return number * computeFactorial(number - 1)
    }

    //History Text View
    private fun updateHistoryString() {
        val operation = TermList.value
        val answer = computationString.value
        val mList = recentExpressionsList.value?.reverse()

        recentExpressionsList.value?.add("$operation = $answer")
    }

    fun removeHistoryString(position: Int) {
        recentExpressionsList.value?.removeAt(position)

    }



}