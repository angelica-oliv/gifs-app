package com.angelicao.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angelicao.favorite.di.FavoriteKoinComponent
import com.angelicao.repository.data.Gif
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteGifListFragment : Fragment(), FavoriteKoinComponent {
    private val favoriteGifListViewModel by viewModel<FavoriteGifListViewModel>()
    private var gifList: RecyclerView? = null
    private var gifAdapter: FavoriteGifListAdapter? = null

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = inflater.inflate(R.layout.fragment_favorite_gif_list, container, false)
        initViews(contentView)
        setObservers()
        return contentView
    }

    private fun initViews(contentView: View?) {
        contentView?.run {
            gifList = findViewById(R.id.favorite_gif_list)
            gifList?.layoutManager = GridLayoutManager(context, 1)
            gifAdapter = FavoriteGifListAdapter(favoriteClick, shareClick)
            gifList?.adapter = gifAdapter
        }
    }

    private fun setObservers() {
        favoriteGifListViewModel.gifList.observe(viewLifecycleOwner, Observer {
            it?.let {
                gifAdapter?.submitList(it.toMutableList())
            }
        })
    }
}
