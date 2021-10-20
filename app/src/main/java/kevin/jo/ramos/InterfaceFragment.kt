package kevin.jo.ramos
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SimpleAdapter
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kevin.jo.ramos.databinding.FragmentInterfaceBinding

class InterfaceFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModel: MainViewModel by viewModels()

        val binding: FragmentInterfaceBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_interface, container, false
        )

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        binding.buttonEqual.setOnClickListener {
            Log.i("IntefaceFragment", "Onclicklistener called")
            binding.operationText.setTextSize(32F)
            binding.answerText.setTextSize(50F)
        }

        //Answer string observer.
        val answerStringObserver = Observer<String> { answer ->

            if (answer.isNotEmpty()) {
                binding.answerText.visibility = View.VISIBLE
            } else {
                binding.answerText.visibility = View.GONE
            }
        }

        viewModel.currentAnswerString.observe(this, answerStringObserver)



        return binding.root


    }




}
