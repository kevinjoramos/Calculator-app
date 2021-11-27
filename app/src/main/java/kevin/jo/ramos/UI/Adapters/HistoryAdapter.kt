package kevin.jo.ramos.UI.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kevin.jo.ramos.R
import kevin.jo.ramos.data.Expression

class HistoryAdapter(private val onDeleteBookmark: (String) -> Unit,
                     private val onClickBookmark: (String) -> Unit):
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var historyData: MutableList<Expression>

    init {
        historyData = mutableListOf()
    }


    class HistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = HistoryViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.history_operation_holder, parent, false))

        view.itemView.findViewById<ImageButton>(R.id.delete_query_button).setOnClickListener {
            val text = view.itemView.findViewById<TextView>(R.id.txt_recent).text
            val operationString = text.split(" ")[0]
            onDeleteBookmark(operationString)
            notifyItemRemoved(view.adapterPosition)
        }

        val text = view.itemView.findViewById<TextView>(R.id.txt_recent)

        text.setOnClickListener {
            val computationString = text.text.split(" ")[2]
            onClickBookmark(computationString)
        }

        return view
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val currentItem = historyData[position]
        val expressionString = currentItem.expressionString
        val answerString = currentItem.answerString
        val fullOperation = "$expressionString = $answerString"
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