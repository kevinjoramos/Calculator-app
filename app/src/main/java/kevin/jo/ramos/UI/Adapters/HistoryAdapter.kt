package kevin.jo.ramos.UI.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import kevin.jo.ramos.R
import kevin.jo.ramos.data.Expression

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private var historyData: MutableList<Expression>

    init {
        historyData = mutableListOf()
    }


    class HistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.text_view_recent, parent, false))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = historyData[position]
        val expressionString = currentItem.expressionString
        val answerString = currentItem.answerString
        val fullOperation = expressionString + answerString
        holder.itemView.findViewById<TextView>(R.id.txt_recent).text = fullOperation

    }

    override fun getItemCount(): Int {
        return historyData.size

    }

    fun setHistoryData(expressions: MutableList<Expression>) {
        this.historyData = expressions
        notifyDataSetChanged()
    }

}