package com.angelicao.favorite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angelicao.favorite.di.FavoriteKoinComponent
import com.angelicao.gifapp.giflist.GifListAdapter
import com.angelicao.repository.data.Gif
import com.google.android.play.core.splitcompat.SplitCompat
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteGifListActivity : AppCompatActivity(R.layout.activity_favorite_gif_list), FavoriteKoinComponent {
    private val favoriteGifListViewModel by viewModel<FavoriteGifListViewModel>()
    private var gifList: RecyclerView? = null

    private val favoriteClick: (Gif) -> Unit = { gif ->
        favoriteGifListViewModel.removeFavorite(gif)
    }
    private val shareClick: (Gif) -> Unit = { gif ->
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, gif.url)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        setObservers()
    }

    private fun initViews() {
        gifList = findViewById(R.id.favorite_gif_list)
        gifList?.layoutManager = GridLayoutManager(this, 1)
    }

    private fun setObservers() {
        favoriteGifListViewModel.gifList.observe(this, Observer {
            it?.let {
                gifList?.adapter = GifListAdapter(it, favoriteClick, shareClick)
            }
        })
    }
}