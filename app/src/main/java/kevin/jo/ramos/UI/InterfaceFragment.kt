package kevin.jo.ramos.UI
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kevin.jo.ramos.MainViewModel
import kevin.jo.ramos.R
import kevin.jo.ramos.UI.Adapters.RecentsAdapter
import kevin.jo.ramos.data.Expression
import kevin.jo.ramos.databinding.FragmentInterfaceBinding

class InterfaceFragment : Fragment() {

    val viewModel: MainViewModel by activityViewModels()
    lateinit var binding: FragmentInterfaceBinding
    lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // inflate the layout for this fragment and bind
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_interface, container, false
        )

        // bind viewmodel
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        // find navcontroller
        navController = findNavController()

        //BUTTON ONCLICK LISTENERS
        // numbers
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

        // infix operators
        binding.buttonAdd.setOnClickListener { onPushOperatorButton(it) }
        binding.buttonSubtract.setOnClickListener { onPushOperatorButton(it) }
        binding.buttonDivision.setOnClickListener { onPushOperatorButton(it) }
        binding.buttonMultiply.setOnClickListener { onPushOperatorButton(it) }

        //utility
        binding.buttonClear.setOnClickListener { onPushClear() }
        binding.buttonDelete.setOnClickListener { onPushDelete() }
        binding.buttonPoint.setOnClickListener { viewModel.insertPoint(".") }
        binding.buttonPercent.setOnClickListener { viewModel.requestPercent() }
        binding.buttonEqual.setOnClickListener { onPushEquals()}
        binding.buttonBlank.setOnClickListener { openScientificCalculator() }

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
        val adapter = RecentsAdapter(::onBookmarkOperation, ::onClickRecent)
        val recyclerView = binding.recentExpressions
        recyclerView.adapter = adapter

        viewModel.currentHistoryList.observe(viewLifecycleOwner, Observer { recents ->
            adapter.setData(recents)
            recyclerView.scrollToPosition(adapter.itemCount - 1)
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

    private fun onClickRecent(answer: String) {
        viewModel.insertPreviousAnswer(answer)
    }

    private fun onPushOperatorButton(view: View) {

        binding.operationText.setTextSize(50F)
        binding.operationText.setTextColor(Color.parseColor("#FFFFFFFF"))

        binding.answerText.setTextSize(32F)
        binding.answerText.setTextColor(Color.parseColor("#66FFFFFF"))

        when (view) {
            binding.buttonAdd -> viewModel.insertInfixOperator("+")
            binding.buttonSubtract -> viewModel.insertInfixOperator("-")
            binding.buttonDivision -> viewModel.insertInfixOperator("÷")
            binding.buttonMultiply -> viewModel.insertInfixOperator("×")
        }
    }

    private fun onPushClear() {
        viewModel.requestClear()
        binding.operationText.setTextSize(50F)
        binding.answerText.setTextSize(32F)

    }

    private fun onPushEquals() {
        viewModel.requestEquals()
        binding.operationText.setTextSize(32F)
        binding.operationText.setTextColor(Color.parseColor("#66FFFFFF"))

        binding.answerText.setTextSize(50F)
        binding.answerText.setTextColor(Color.parseColor("#FFFFFFFF"))
    }

    private fun onPushDelete() {
        viewModel.requestDelete()
        binding.operationText.setTextSize(50F)
        binding.operationText.setTextColor(Color.parseColor("#FFFFFFFF"))

        binding.answerText.setTextSize(32F)
        binding.answerText.setTextColor(Color.parseColor("#66FFFFFF"))
    }

    private fun openScientificCalculator() {
        navController.navigate(R.id.action_interfaceFragment_to_expandedInterfaceFragment)
    }

    fun onBookmarkOperation(expression: Expression, adapterPosition: Int) {
        viewModel.removeHistoryString(adapterPosition)
        viewModel.addExpressionToDatabase(expression)
        Toast.makeText(requireContext(), "Operation Saved", Toast.LENGTH_LONG).show()
    }
}
