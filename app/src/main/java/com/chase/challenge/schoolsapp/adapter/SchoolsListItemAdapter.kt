package com.chase.challenge.schoolsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chase.challenge.schoolsapp.data.*
import com.chase.challenge.schoolsapp.databinding.NycSchoolListItemBinding

class SchoolsListItemAdapter : RecyclerView.Adapter<SchoolsListItemAdapter.SchoolListViewHolder>() {

    var items = SchoolAndScoreList()
    lateinit var binding: NycSchoolListItemBinding
    var onItemClick: ((SchoolAndScoreListItem) -> Unit)? = null

    fun setData(data: SchoolAndScoreList){
        this.items = data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolListViewHolder {
        binding = NycSchoolListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SchoolListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: SchoolListViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

  inner  class SchoolListViewHolder(items: View): RecyclerView.ViewHolder(items) {

        fun bind(schoolItem: SchoolAndScoreListItem){
            binding.schoolItem = schoolItem
            binding.itemParentLayout.setOnClickListener(View.OnClickListener {
                onItemClick?.invoke(schoolItem)
            })
            binding.executePendingBindings()
        }
    }
}