package com.angelicao.gifapp.giflist

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.angelicao.gifapp.R
import com.angelicao.repository.data.Gif
import com.bumptech.glide.Glide

class GifDiffCallback : DiffUtil.ItemCallback<Gif>() {
    override fun areItemsTheSame(oldItem: Gif, newItem: Gif): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Gif, newItem: Gif): Boolean =
        oldItem == newItem
}

class GifListAdapter(
    private val favoriteClicked: (Gif) -> Unit,
    private val shareClicked: (Gif) -> Unit,
    private val gifItemClick: (Gif) -> Unit
) :
    PagingDataAdapter<Gif, GifListAdapter.GifViewHolder>(GifDiffCallback()) {

    class GifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gifImage: ImageView = itemView.findViewById(R.id.gif_image)
        val favorite: ImageButton = itemView.findViewById(R.id.favorite)
        val share: ImageButton = itemView.findViewById(R.id.share)
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GifViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.gif_item, null)
    )

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        getItem(position)?.run {
            loadImageGif(url, holder.gifImage)
            holder.gifImage.contentDescription = title
            setFavoriteButtonColor(favorite, holder.favorite)
            holder.favorite.setOnClickListener {
                favoriteClicked(this)
                this.favorite = !favorite
                notifyItemChanged(position)
            }
            holder.share.setOnClickListener { shareClicked(this) }
            holder.gifImage.setOnClickListener { gifItemClick(this) }
        }
    }

    private fun loadImageGif(url: String, gifImage: ImageView) {
        val drawablePlaceholder = ContextCompat.getDrawable(gifImage.context, R.drawable.ic_image)
        drawablePlaceholder?.setTint(ContextCompat.getColor(gifImage.context, R.color.colorGray))
        Glide
            .with(gifImage.context)
            .load(url)
            .placeholder(drawablePlaceholder)
            .into(gifImage)
    }

    private fun setFavoriteButtonColor(favorite: Boolean, favoriteButton: ImageButton) {
        favoriteButton.run {
            setColorFilter(if (favorite) {
                ContextCompat.getColor(context, R.color.colorAccent)
            } else {
                ContextCompat.getColor(context, R.color.colorGray)
            }, PorterDuff.Mode.MULTIPLY)
        }
    }
}
