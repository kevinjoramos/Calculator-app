package kevin.jo.ramos.UI.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kevin.jo.ramos.R
import kevin.jo.ramos.data.Expression
import java.util.Collections.reverse

class RecentsAdapter(private val onBookmarkOperation: (Expression, Int) -> Unit,
                     private val onClickRecent: (String) -> Unit):
    RecyclerView.Adapter<RecentsAdapter.RecentViewHolder>() {

    private lateinit var dataSet: MutableList<String>

    //view holder
    class RecentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        val view = RecentViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.interface_recents_holder, parent, false))

        view.itemView.findViewById<TextView>(R.id.txt_recent).setOnClickListener {
            val text = view.itemView.findViewById<TextView>(R.id.txt_recent).text
            val computationString = text.split(" ")[2]
            onClickRecent(computationString)
        }

        view.itemView.findViewById<ImageButton>(R.id.add_query_button).setOnClickListener {
            val text = view.itemView.findViewById<TextView>(R.id.txt_recent).text
            val operationString = text.split(" ")[0]
            val computationString = text.split(" ")[2]

            val expression = Expression(operationString, computationString)
            onBookmarkOperation(expression, view.adapterPosition)
            notifyItemRemoved(view.adapterPosition)
        }

        return view
    }

    override fun onBindViewHolder(holder: RecentViewHolder, position: Int) {
        val currentItem = dataSet[position]
        holder.itemView.findViewById<TextView>(R.id.txt_recent).text = currentItem
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setData(expressions: MutableList<String>) {

        this.dataSet = expressions
        notifyDataSetChanged()
    }


}