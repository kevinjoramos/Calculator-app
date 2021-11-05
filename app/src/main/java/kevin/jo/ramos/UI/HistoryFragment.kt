package kevin.jo.ramos.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kevin.jo.ramos.MainViewModel
import kevin.jo.ramos.R
import kevin.jo.ramos.UI.Adapters.HistoryAdapter
import kevin.jo.ramos.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {
    val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val binding: FragmentHistoryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_history, container, false)

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        //Recycler View
        val adapter = HistoryAdapter()
        val recyclerView = binding.historyRecycler
        recyclerView.adapter = adapter

        viewModel.expressionData.observe(viewLifecycleOwner, Observer { recents ->
            adapter.setHistoryData(recents.toMutableList())
        })


        return binding.root
    }


}