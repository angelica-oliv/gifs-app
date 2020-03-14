package com.angelicao.gifapp.giflist

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angelicao.gifapp.R
import com.angelicao.repository.data.Gif
import org.koin.android.viewmodel.ext.android.viewModel


class GifListActivity : AppCompatActivity() {
    private val gifListViewModel by viewModel<GifListViewModel>()
    private var gifList: RecyclerView? = null

    private val favoriteClick: (Gif) -> Unit = { gif ->
        gifListViewModel.onFavoriteClicked(gif)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gif_list)

        initViews()
        setObservers()
    }

    private fun initViews() {
        gifList = findViewById(R.id.gif_list)
        gifList?.layoutManager = GridLayoutManager(this, 2)
    }

    private fun setObservers() {
        gifListViewModel.gifList.observe(this, Observer {
            it?.let {
                gifList?.adapter = GifListAdapter(it, favoriteClick, shareClick)
            }
        })
    }
}
