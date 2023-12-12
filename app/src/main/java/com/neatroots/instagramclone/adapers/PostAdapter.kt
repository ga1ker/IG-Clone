package com.neatroots.instagramclone.adapers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.neatroots.instagramclone.Models.Post
import com.neatroots.instagramclone.Models.User
import com.neatroots.instagramclone.R
import com.neatroots.instagramclone.databinding.PostRvBinding
import com.neatroots.instagramclone.utils.USER_NODE


class PostAdapter(var context : Context,var postList: ArrayList<Post>) :RecyclerView.Adapter<PostAdapter.MyHolder>(){


    inner class MyHolder(var binding : PostRvBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
      var binding=PostRvBinding.inflate(LayoutInflater.from(context),parent,false)

        return MyHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

        try {
            Firebase.firestore.collection(USER_NODE).document(postList.get(position).uid).get().addOnSuccessListener {

                var user=it.toObject<User>()
                Glide.with(context).load(user!!.image).placeholder(R.drawable.user).into(holder.binding.profileImage)
                holder.binding.name.text=user.name
            }

        }catch (e:Exception){

        }
        Glide.with(context).load(postList.get(position).postUrl).placeholder(R.drawable.loading).into(holder.binding.postImage)
        try {
            val text = TimeAgo.using(postList.get(position).time.toLong())

            holder.binding.time.text=text

        }catch (e:Exception){
            holder.binding.time.text=""
        }

        holder.binding.share.setOnClickListener {
            var i=Intent(Intent.ACTION_SEND)
            i.type="text/plain"
            i.putExtra(Intent.EXTRA_TEXT,postList.get(position).postUrl)
            context.startActivity(i)

        }
        holder.binding.csption.text=postList.get(position).caption
            holder.binding.like.setOnClickListener {
                holder.binding.like.setImageResource(R.drawable.heart_like)
            }


    }
}