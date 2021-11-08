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
import androidx.recyclerview.widget.LinearLayoutManager
import kevin.jo.ramos.MainViewModel
import kevin.jo.ramos.R
import kevin.jo.ramos.UI.Adapters.RecentsAdapter
import kevin.jo.ramos.data.Expression
import kevin.jo.ramos.databinding.FragmentInterfaceBinding

class InterfaceFragment : Fragment() {

    val viewModel: MainViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val navController = findNavController()

        val binding: FragmentInterfaceBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_interface, container, false
        )

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        //BUTTON ONCLICK LISTENERS
        binding.button0.setOnClickListener { onPushNumberButton(it, binding, viewModel) }
        binding.button1.setOnClickListener { onPushNumberButton(it, binding, viewModel) }
        binding.button2.setOnClickListener { onPushNumberButton(it, binding, viewModel) }
        binding.button3.setOnClickListener { onPushNumberButton(it, binding, viewModel) }
        binding.button4.setOnClickListener { onPushNumberButton(it, binding, viewModel) }
        binding.button5.setOnClickListener { onPushNumberButton(it, binding, viewModel) }
        binding.button6.setOnClickListener { onPushNumberButton(it, binding, viewModel) }
        binding.button7.setOnClickListener { onPushNumberButton(it, binding, viewModel) }
        binding.button8.setOnClickListener { onPushNumberButton(it, binding, viewModel) }
        binding.button9.setOnClickListener { onPushNumberButton(it, binding, viewModel) }
        binding.buttonClear.setOnClickListener { onPushClear(binding, viewModel) }
        binding.buttonDelete.setOnClickListener { onPushDelete(binding, viewModel) }
        binding.buttonPoint.setOnClickListener { viewModel.onPushButtonPeriod() }
        binding.buttonAdd.setOnClickListener { onPushOperatorButton(it, binding, viewModel) }
        binding.buttonSubtract.setOnClickListener { onPushOperatorButton(it, binding, viewModel) }
        binding.buttonDivision.setOnClickListener { onPushOperatorButton(it, binding, viewModel) }
        binding.buttonMultiply.setOnClickListener { onPushOperatorButton(it, binding, viewModel) }
        binding.buttonPercent.setOnClickListener { viewModel.onPushButtonPercent() }
        binding.buttonEqual.setOnClickListener { onPushEquals(binding, viewModel)}


        //NAVIGATE TO SCIENTIFIC CALCULATOR
        binding.buttonBlank.setOnClickListener { openScientificCalculator(navController) }

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
            recyclerView.scrollToPosition(adapter.itemCount - 1) //TODO-> this works when I apply changes but not when running.
        })

        return binding.root
    }

    private fun onPushNumberButton(view: View, binding: FragmentInterfaceBinding,
                                   viewModel: MainViewModel
    ) {

        binding.operationText.setTextSize(50F)
        binding.operationText.setTextColor(Color.parseColor("#FFFFFFFF"))

        binding.answerText.setTextSize(32F)
        binding.answerText.setTextColor(Color.parseColor("#66FFFFFF"))

        when (view) {

            binding.button0 -> viewModel.onPushNumberButton("0")
            binding.button1 -> viewModel.onPushNumberButton("1")
            binding.button2 -> viewModel.onPushNumberButton("2")
            binding.button3 -> viewModel.onPushNumberButton("3")
            binding.button4 -> viewModel.onPushNumberButton("4")
            binding.button5 -> viewModel.onPushNumberButton("5")
            binding.button6 -> viewModel.onPushNumberButton("6")
            binding.button7 -> viewModel.onPushNumberButton("7")
            binding.button8 -> viewModel.onPushNumberButton("8")
            binding.button9 -> viewModel.onPushNumberButton("9")
        }
    }

    private fun onPushOperatorButton(view: View, binding: FragmentInterfaceBinding,
                                     viewModel: MainViewModel
    ) {

        binding.operationText.setTextSize(50F)
        binding.operationText.setTextColor(Color.parseColor("#FFFFFFFF"))

        binding.answerText.setTextSize(32F)
        binding.answerText.setTextColor(Color.parseColor("#66FFFFFF"))

        when (view) {
            binding.buttonAdd -> viewModel.onPushButtonOperator("+")
            binding.buttonSubtract -> viewModel.onPushButtonOperator("-")
            binding.buttonDivision -> viewModel.onPushButtonOperator("÷")
            binding.buttonMultiply -> viewModel.onPushButtonOperator("×")
        }
    }

    private fun onPushClear(binding: FragmentInterfaceBinding, viewModel: MainViewModel) {
        viewModel.onPushButtonClear()
        binding.operationText.setTextSize(50F)
        binding.answerText.setTextSize(32F)

    }

    private fun onPushEquals(binding: FragmentInterfaceBinding, viewModel: MainViewModel) {
        viewModel.onPushButtonEquals()
        binding.operationText.setTextSize(32F)
        binding.operationText.setTextColor(Color.parseColor("#66FFFFFF"))

        binding.answerText.setTextSize(50F)
        binding.answerText.setTextColor(Color.parseColor("#FFFFFFFF"))
    }

    private fun onPushDelete(binding: FragmentInterfaceBinding, viewModel: MainViewModel) {
        viewModel.onPushButtonDelete()
        binding.operationText.setTextSize(50F)
        binding.operationText.setTextColor(Color.parseColor("#FFFFFFFF"))

        binding.answerText.setTextSize(32F)
        binding.answerText.setTextColor(Color.parseColor("#66FFFFFF"))
    }

    private fun openScientificCalculator(navController: NavController) {
        navController.navigate(R.id.action_interfaceFragment_to_expandedInterfaceFragment)
    }

    fun onBookmarkOperation(expression: Expression, adapterPosition: Int) {
        viewModel.removeHistoryString(adapterPosition)
        viewModel.addExpressionToDatabase(expression)
        Toast.makeText(requireContext(), "Operation Saved", Toast.LENGTH_LONG).show()
    }
}
