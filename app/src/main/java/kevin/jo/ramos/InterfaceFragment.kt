package kevin.jo.ramos
import android.os.Bundle
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SimpleAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
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

        return binding.root


    }




}
