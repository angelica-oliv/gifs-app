package com.angelicao.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.angelicao.repository.data.Gif
import com.angelicao.uidefinitions.FONT_SIZE_MEDIUM_BOLD
import com.angelicao.uidefinitions.FONT_SIZE_MEDIUM_NORMAL
import com.angelicao.uidefinitions.SPACING_MEDIUM
import com.angelicao.uidefinitions.SPACING_SMALL
import com.google.accompanist.glide.GlideImage

class DetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val gif = getGifArgument()

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    GifImageDetails(gif)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().invalidateOptionsMenu()
    }

    private fun getGifArgument() =
        arguments?.getParcelable<Gif>("gif")
        ?: throw IllegalStateException("Gif Argument should be passed to Details Screen")
}

@Composable
fun GifImageDetails(gif: Gif) {
    Column(
        modifier = Modifier
            .padding(SPACING_MEDIUM.dp)
            .fillMaxWidth()
    ) {
        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            loading = {
                Box(Modifier.fillMaxWidth()) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            },
            contentScale = ContentScale.Fit,
            data = gif.largerGifUrl,
            contentDescription = stringResource(id = R.string.gif_description),
            fadeIn = true
        )
        Spacer(Modifier.size(SPACING_MEDIUM.dp))
        GifTitle(gif)
    }
}

@Composable
fun GifTitle(gif: Gif) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            fontWeight = FontWeight.Bold,
            fontSize = FONT_SIZE_MEDIUM_BOLD.sp,
            text = stringResource(id = R.string.gif_title)
        )
        Spacer(Modifier.size(SPACING_SMALL.dp))
        Text(
            text = gif.title,
            fontSize = FONT_SIZE_MEDIUM_NORMAL.sp
        )
    }
}
