package com.angelicao.gifapp.giflist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angelicao.gifapp.R
import org.koin.android.viewmodel.ext.android.viewModel


class GifListActivity : AppCompatActivity() {
    private val gifListViewModel by viewModel<GifListViewModel>()
    private var gifList: RecyclerView? = null

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
                gifList?.adapter = GifListAdapter(it) { gif ->
                    gifListViewModel.onFavoriteClicked(gif)
                }
            }
        })
    }
}
