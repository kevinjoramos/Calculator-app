package kevin.jo.ramos.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kevin.jo.ramos.MainViewModel
import kevin.jo.ramos.R
import kevin.jo.ramos.UI.Adapters.HistoryAdapter
import kevin.jo.ramos.data.Expression
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

        // SWIPE GESTURE FOR DELETING DATABASE ENTRIES
        val itemTouchHelperCallback =
            object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val text = viewHolder.itemView
                        .findViewById<TextView>(R.id.txt_recent).text

                    val operationString = text.split(" ")[0]
                    val computationString = text.split(" ")[2]

                    val expression = Expression(0,operationString, computationString)

                    viewModel.removeExpressionFromDatabase(expression)
                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
                }

            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)


        return binding.root
    }


}