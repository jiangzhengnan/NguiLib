package com.ng.ui.show.main

/**
 * 描述:
 * @author Jzn
 * @date 2020-06-12
 */
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ng.ui.R

/**
 * @作者： ljp
 * @时间： 2020/5/23 9:19
 * @描述：
 **/
class LeftListAdapter(val context: Context, var data: List<ItemInfo>) :
        RecyclerView.Adapter<LeftListAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvLeft: TextView = v.findViewById(R.id.tv_left)
    }

    interface OnLeftItemClick {
        fun onItem(pos: Int)
    }

    private lateinit var onItemListener:OnLeftItemClick

    fun setItemListener(callback:OnLeftItemClick){
        onItemListener = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(context).inflate(R.layout.item_left, parent, false)
        var vh = VH(view)
        vh.itemView.setOnClickListener {
            //拿到用户点击的位置
            val position = vh.adapterPosition
            val info = data[position]
            onItemListener?.let {
                onItemListener.onItem(position)
            }
        }
        return vh
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        var info = data[position]
        holder.tvLeft.text = info.name
    }
}
