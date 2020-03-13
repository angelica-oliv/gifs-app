package com.angelicao.gifapp.giflist

import android.graphics.PorterDuff
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.P
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.api.load
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.angelicao.gifapp.R
import com.angelicao.repository.data.Gif
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

class GifListAdapter(private val gifList: List<Gif>, private val favoriteClicked: (Gif) -> Unit): RecyclerView.Adapter<GifListAdapter.GifViewHolder>() {
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
            loadImageGif(url.toHttpUrl(), holder.gifImage)
            holder.gifImage.contentDescription = title
            setFavoriteButtonColor(favorite, holder.favorite)
            holder.favorite.setOnClickListener { favoriteClicked(this) }
        }
    }

    private fun loadImageGif(url: HttpUrl, gifImage: ImageView) {
        val imageLoader = ImageLoader(gifImage.context) {
            componentRegistry {
                if (SDK_INT >= P) {
                    add(ImageDecoderDecoder())
                } else {
                    add(GifDecoder())
                }
            }
        }
        val drawablePlaceholder = ContextCompat.getDrawable(gifImage.context, R.drawable.ic_image)
        drawablePlaceholder?.setTint(ContextCompat.getColor(gifImage.context, R.color.colorGray))
        imageLoader.load(gifImage.context, url) {
            target(gifImage)
            placeholder(drawablePlaceholder)
        }
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