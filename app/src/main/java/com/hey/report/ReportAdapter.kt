package com.hey.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ReportAdapter(private var mData: List<ReportData>) :
    RecyclerView.Adapter<ReportAdapter.Holder>() {


    override fun getItemCount(): Int {
        return mData.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindData(mData[position])
    }


    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tv_name)
        var tvLocation: TextView = itemView.findViewById(R.id.tv_location)
        var tvTime: TextView = itemView.findViewById(R.id.tv_time)
        var tvResult: TextView = itemView.findViewById(R.id.tv_result)

        fun bindData(data: ReportData) {
            tvName.text = data.name
            tvLocation.text = data.location
            tvTime.text = data.time
            tvResult.text = data.result
        }
    }
}