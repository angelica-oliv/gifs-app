package com.angelicao.gifapp.giflist

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.angelicao.gifapp.R

class GifListHostActivity : AppCompatActivity(R.layout.activity_gif_list_host) {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_gif_list, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.action_favorite)
        item.isVisible = findNavController(R.id.main_content).currentDestination?.id == R.id.gif_list_fragment
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> {
            val action = GifListFragmentDirections.navigateToFavoriteGifListFragment()
            findNavController(R.id.main_content).navigate(action)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
