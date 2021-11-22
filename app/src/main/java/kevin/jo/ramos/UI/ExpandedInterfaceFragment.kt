package kevin.jo.ramos.UI

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kevin.jo.ramos.MainViewModel
import kevin.jo.ramos.R
import kevin.jo.ramos.UI.Adapters.RecentsAdapter
import kevin.jo.ramos.data.Expression
import kevin.jo.ramos.databinding.FragmentExpandedInterfaceBinding

class ExpandedInterfaceFragment : Fragment() {
    val viewModel: MainViewModel by activityViewModels()
    lateinit var binding: FragmentExpandedInterfaceBinding
    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_expanded_interface, container, false)

        //Bind viewmodel
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        //Find NavController
        navController = findNavController()

        //BUTTON ONCLICK LISTENERS
        //Numbers
        binding.button0.setOnClickListener { onPushNumberButton(it) }
        binding.button1.setOnClickListener { onPushNumberButton(it) }
        binding.button2.setOnClickListener { onPushNumberButton(it) }
        binding.button3.setOnClickListener { onPushNumberButton(it) }
        binding.button4.setOnClickListener { onPushNumberButton(it) }
        binding.button5.setOnClickListener { onPushNumberButton(it) }
        binding.button6.setOnClickListener { onPushNumberButton(it) }
        binding.button7.setOnClickListener { onPushNumberButton(it) }
        binding.button8.setOnClickListener { onPushNumberButton(it) }
        binding.button9.setOnClickListener { onPushNumberButton(it) }

        //Special Numbers
        binding.buttonPi.setOnClickListener { onPushSpecialNumbers(it) }
        binding.buttonE.setOnClickListener { onPushSpecialNumbers(it) }

        //Infix Operators
        binding.buttonAdd.setOnClickListener { onPushInfixOperatorButton(it) }
        binding.buttonSubtract.setOnClickListener { onPushInfixOperatorButton(it) }
        binding.buttonDivision.setOnClickListener { onPushInfixOperatorButton(it) }
        binding.buttonMultiply.setOnClickListener { onPushInfixOperatorButton(it) }
        binding.buttonExponent.setOnClickListener { onPushInfixOperatorButton(it) }

        //Prefix Operators
        binding.buttonPercent.setOnClickListener { onPushPrefixButton(it) }
        binding.buttonInverse.setOnClickListener { onPushPrefixButton(it) }
        binding.buttonFactorial.setOnClickListener { onPushPrefixButton(it) }
        binding.buttonSquareRoot.setOnClickListener { onPushPrefixButton(it) }
        binding.buttonLog.setOnClickListener { onPushPrefixButton(it) }
        binding.buttonNaturalLog.setOnClickListener { onPushPrefixButton(it) }
        binding.buttonSin.setOnClickListener { onPushPrefixButton(it) }
        binding.buttonCos.setOnClickListener { onPushPrefixButton(it) }
        binding.buttonTan.setOnClickListener { onPushPrefixButton(it) }

        //Utility
        binding.buttonClear.setOnClickListener { onPushClear() }
        binding.buttonDelete.setOnClickListener { onPushDelete() }
        binding.buttonPoint.setOnClickListener { onPushPoint() }
        binding.button2nd.setOnClickListener { onPush2nd() }
        binding.buttonDegRad.setOnClickListener { onPushUnits() }
        binding.buttonLeftParenthesis.setOnClickListener { onPushParenthesis(it) }
        binding.buttonRightParenthesis.setOnClickListener { onPushParenthesis(it) }
        binding.buttonEqual.setOnClickListener { onPushEquals()}
        binding.buttonBlank.setOnClickListener { closeScientificCalculator(navController) }


        //TERM OF TERMS OBSERVER - represented in human readable form.
        viewModel.currentExpression.observe(viewLifecycleOwner, Observer { terms ->
            var operationString: String = ""
            for (item in terms) {
                operationString += item
            }

            binding.operationText.text = operationString
        })


        //ANSWER STRING OBSERVER - Updates the answer text view, and formatting.
        viewModel.currentAnswerString.observe(viewLifecycleOwner, Observer { answer ->
            if (answer.isNotEmpty()) {
                binding.answerText.visibility = View.VISIBLE
                val answerString = "= $answer"
                binding.answerText.text = answerString
            } else {
                binding.answerText.visibility = View.GONE
                binding.answerText.text = ""
            }
        })


        //Recycler View
        val adapter = RecentsAdapter(::onBookmarkOperation)
        val recyclerView = binding.recentExpressions
        recyclerView.adapter = adapter


        viewModel.currentHistoryList.observe(viewLifecycleOwner, Observer { recents ->
            adapter.setData(recents)
            recyclerView.scrollToPosition(adapter.itemCount - 1) //TODO-> this workds when I apply changes but not when running.
        })

        return binding.root
    }

    private fun onPushNumberButton(view: View) {

        binding.operationText.setTextSize(50F)
        binding.operationText.setTextColor(Color.parseColor("#FFFFFFFF"))

        binding.answerText.setTextSize(32F)
        binding.answerText.setTextColor(Color.parseColor("#66FFFFFF"))

        when (view) {

            binding.button0 -> viewModel.insertNumber("0")
            binding.button1 -> viewModel.insertNumber("1")
            binding.button2 -> viewModel.insertNumber("2")
            binding.button3 -> viewModel.insertNumber("3")
            binding.button4 -> viewModel.insertNumber("4")
            binding.button5 -> viewModel.insertNumber("5")
            binding.button6 -> viewModel.insertNumber("6")
            binding.button7 -> viewModel.insertNumber("7")
            binding.button8 -> viewModel.insertNumber("8")
            binding.button9 -> viewModel.insertNumber("9")
        }
    }

    private fun onPushSpecialNumbers(view: View) {
        when (view) {
            binding.buttonPi -> viewModel.insertSpecialNumber("π")
            binding.buttonE -> viewModel.insertSpecialNumber("e")
        }
    }

    private fun onPushInfixOperatorButton(view: View) {

        binding.operationText.setTextSize(50F)
        binding.operationText.setTextColor(Color.parseColor("#FFFFFFFF"))

        binding.answerText.setTextSize(32F)
        binding.answerText.setTextColor(Color.parseColor("#66FFFFFF"))

        when (view) {
            binding.buttonAdd -> viewModel.insertInfixOperator("+")
            binding.buttonSubtract -> viewModel.insertInfixOperator("-")
            binding.buttonDivision -> viewModel.insertInfixOperator("÷")
            binding.buttonMultiply -> viewModel.insertInfixOperator("×")
            binding.buttonExponent -> viewModel.insertInfixOperator("^")
        }
    }

    private fun onPushPrefixButton(view: View) {
        when (view) {
            binding.buttonPercent -> viewModel.requestPercent()
            binding.buttonInverse -> viewModel.insertPrefixOperator("inv")
            binding.buttonFactorial -> viewModel.insertPrefixOperator("!")
            binding.buttonSquareRoot -> viewModel.insertPrefixOperator("√")
            binding.buttonLog -> viewModel.insertPrefixOperator("log")
            binding.buttonNaturalLog -> viewModel.insertPrefixOperator("ln")
            binding.buttonSin -> viewModel.insertPrefixOperator("sin")
            binding.buttonCos -> viewModel.insertPrefixOperator("cos")
            binding.buttonTan -> viewModel.insertPrefixOperator("tan")

        }
    }

    private fun onPushClear() {
        viewModel.requestClear()
        binding.operationText.setTextSize(50F)
        binding.answerText.setTextSize(32F)

    }

    private fun onPushDelete() {
        viewModel.requestDelete()
        binding.operationText.setTextSize(50F)
        binding.operationText.setTextColor(Color.parseColor("#FFFFFFFF"))

        binding.answerText.setTextSize(32F)
        binding.answerText.setTextColor(Color.parseColor("#66FFFFFF"))
    }

    private fun onPushPoint() {
        viewModel.insertPoint(".")
    }

    private fun onPush2nd() {
        viewModel.request2nd()
    }

    private fun onPushUnits() {
        viewModel.requestChangeUnits()
    }

    private fun onPushParenthesis(view: View) {
        when (view) {
            binding.buttonLeftParenthesis -> viewModel.insertParenthesis("(")
            binding.buttonRightParenthesis -> viewModel.insertParenthesis(")")
        }
    }









    private fun onPushEquals() {
        viewModel.requestEquals()
        binding.operationText.setTextSize(32F)
        binding.operationText.setTextColor(Color.parseColor("#66FFFFFF"))

        binding.answerText.setTextSize(50F)
        binding.answerText.setTextColor(Color.parseColor("#FFFFFFFF"))
    }



    private fun closeScientificCalculator(navController: NavController) {
        navController.navigate(R.id.action_expandedInterfaceFragment_to_interfaceFragment)
    }

    fun onBookmarkOperation(expression: Expression, adapterPosition: Int) {
        viewModel.removeHistoryString(adapterPosition)
        viewModel.addExpressionToDatabase(expression)
        Toast.makeText(requireContext(), "Operation Saved", Toast.LENGTH_LONG).show()
    }
}