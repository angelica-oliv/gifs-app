package com.angelicao.gifapp.giflist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angelicao.gifapp.R
import com.angelicao.repository.data.Gif
import org.koin.android.viewmodel.ext.android.viewModel

private const val FAVORITE_GIF_LIST_ACTIVITY = "com.angelicao.favorite.FavoriteGifListActivity"
class GifListActivity : AppCompatActivity(R.layout.activity_gif_list) {
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
        initViews()
        setObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_gif_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> {
            launchActivity(FAVORITE_GIF_LIST_ACTIVITY)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun launchActivity(className: String) {
        Intent().setClassName(packageName, className)
            .also {
                startActivity(it)
            }
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
