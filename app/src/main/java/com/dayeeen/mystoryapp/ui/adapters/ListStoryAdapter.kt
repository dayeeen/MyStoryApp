package com.dayeeen.mystoryapp.ui.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dayeeen.mystoryapp.R
import com.dayeeen.mystoryapp.data.response.ListStoryItem
import com.dayeeen.mystoryapp.databinding.ListStoryBinding
import com.dayeeen.mystoryapp.ui.activity.DetailStoryActivity

class ListStoryAdapter : ListAdapter<ListStoryItem, ListStoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(private val binding: ListStoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: ListStoryItem) {
            binding.tvItemName.text = user.name
            binding.caption.text = user.description
            Glide.with(itemView.context)
                .load(user.photoUrl)
                .placeholder(R.drawable.load_photo)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.ivItemPhoto)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra("USER_ID", user.id)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        androidx.core.util.Pair.create(binding.ivItemPhoto, "photo"),
                        androidx.core.util.Pair.create(binding.tvItemName, "name"),
                        androidx.core.util.Pair.create(binding.caption, "description")
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
