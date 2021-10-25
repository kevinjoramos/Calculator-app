package kevin.jo.ramos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kevin.jo.ramos.databinding.FragmentExpandedInterfaceBinding

class ExpandedInterfaceFragment : Fragment() {
    lateinit var binding: FragmentExpandedInterfaceBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_expanded_interface, container, false)

        val navController: NavController = findNavController()

        binding.buttonBlank.setOnClickListener { closeScientificCalculator(navController) }

        return binding.root
    }

    private fun closeScientificCalculator(navController: NavController) {
        navController.navigate(R.id.action_expandedInterfaceFragment_to_interfaceFragment)
    }
}