package com.example.socialhive.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.socialhive.databinding.RecyclerRowBinding
import com.example.socialhive.model.Post
import com.squareup.picasso.Picasso

class PostRecyclerAdapter(private val postList : ArrayList<Post>) : RecyclerView.Adapter<PostRecyclerAdapter.PostHolder>(){

    // ** "RecyclerRowBinding" sınıfı ile "recycler_row" view'ını kullanacağımızı belirterek birbirine bağladık.
    class PostHolder(val binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false);
        return PostHolder(binding);
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.binding.recyclerEmailText.text = postList.get(position).email;
        holder.binding.recyclerCommentText.text = postList.get(position).comment;
        holder.binding.recyclerDateTimeText.text = "Eklenme Tarihi: "+postList.get(position).dateTime;
        Picasso.get().load("${postList.get(position).downloadUrl}").into(holder.binding.recyclerViewImage);
    }

    override fun getItemCount(): Int {
        return postList.size;
    }
}