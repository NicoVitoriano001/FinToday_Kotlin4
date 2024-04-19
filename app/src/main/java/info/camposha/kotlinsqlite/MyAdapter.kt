package info.camposha.kotlinsqlite

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val context: Context, private val dataList: List<String>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView1: TextView = itemView.findViewById(R.id.textView1)
        private val textView2: TextView = itemView.findViewById(R.id.textView2)
        private val textView3: TextView = itemView.findViewById(R.id.textView3)
        private val textView4: TextView = itemView.findViewById(R.id.textView4)
        private val textView5: TextView = itemView.findViewById(R.id.textView5)
        private val textView6: TextView = itemView.findViewById(R.id.textView6)

        fun bind(item: String) {
            // Atribua os valores aos TextViews conforme necessário
            textView1.text = "Valor para o textView1"
            textView2.text = "Valor para o textView2"
            textView3.text = "Valor para o textView3"
            textView4.text = "Valor para o textView4"
            textView5.text = "Valor para o textView5"
            textView6.text = "Valor para o textView6"
            // Atribua os valores para os outros TextViews aqui conforme necessário
        }
    }

}
