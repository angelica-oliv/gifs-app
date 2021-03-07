package com.angelicao.gifapp.giflist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angelicao.gifapp.R
import com.angelicao.repository.data.Gif
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

class GifListFragment : Fragment() {
    private val gifListViewModel by viewModel<GifListViewModel>()
    private var gifList: RecyclerView? = null
    private var adapter: GifListAdapter? = null

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
    private val gifItemClick: (Gif) -> Unit = { gif ->
        val action = GifListFragmentDirections.navigateToDetailsFragment()
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contentView = inflater.inflate(R.layout.fragment_gif_list, container, false)
        initViews(contentView)
        setObservers()
        return contentView
    }

    private fun initViews(contentView: View?) {
        contentView?.run {
            gifList = findViewById(R.id.gif_list)
            gifList?.layoutManager = GridLayoutManager(context, 2)
            gifList?.setHasFixedSize(true)

            adapter = GifListAdapter(favoriteClick, shareClick, gifItemClick)
            gifList?.adapter = adapter
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            gifListViewModel.gifList.collectLatest {
                adapter?.submitData(it)
                adapter?.notifyDataSetChanged()
            }
        }
    }
}
