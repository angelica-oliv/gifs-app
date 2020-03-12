package com.angelicao.gifapp.giflist

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.angelicao.gifapp.R
import com.angelicao.repository.data.Gif
import okhttp3.HttpUrl.Companion.toHttpUrl

class GifListAdapter(private val gifList: List<Gif>): RecyclerView.Adapter<GifListAdapter.GifViewHolder>() {
    class GifViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val gifImage: ImageView = itemView.findViewById(R.id.gif_image)
        val favorite: ImageButton = itemView.findViewById(R.id.favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= GifViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.gif_item, null)
    )

    override fun getItemCount(): Int = gifList.size

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        gifList[position].run {
            holder.gifImage.load(url.toHttpUrl())
            holder.gifImage.contentDescription = title
            setFavoriteButtonColor(favorite, holder.favorite)
        }
    }

    private fun setFavoriteButtonColor(favorite: Boolean, favoriteButton: ImageButton) {
        favoriteButton.run {
            setColorFilter(if(favorite) {
                ContextCompat.getColor(context, R.color.colorAccent)
            } else {
                ContextCompat.getColor(context, R.color.colorGray)
            }, PorterDuff.Mode.MULTIPLY)
        }
    }
}