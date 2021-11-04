package kevin.jo.ramos.UI.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kevin.jo.ramos.R
import org.w3c.dom.Text

class RecentExpressionAdapter: RecyclerView.Adapter<RecentExpressionAdapter.RecentViewHolder>() {
    private lateinit var dataSet: MutableList<String>

    //view holder
    class RecentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentViewHolder {
        return RecentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.text_view_recent, parent, false))
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