package com.angelicao.gifapp.giflist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.angelicao.gifapp.R
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

private const val FAVORITE_GIF_LIST_ACTIVITY = "com.angelicao.favorite.FavoriteGifListActivity"
private const val TAG = "GifListActivity"
class GifListHostActivity : AppCompatActivity(R.layout.activity_gif_list_host) {
    private var progress: Group? = null
    private var progressBar: ProgressBar? = null
    private var progressText: TextView? = null
    private val favorite by lazy { getString(R.string.module_favorite) }

    private lateinit var manager: SplitInstallManager

    private val listener = SplitInstallStateUpdatedListener { state ->
        val multiInstall = state.moduleNames().size > 1
        val names = state.moduleNames().joinToString(" - ")
        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                //  In order to see this, the application has to be uploaded to the Play Store.
                displayLoadingState(state, getString(R.string.downloading, names))
            }
            SplitInstallSessionStatus.INSTALLED -> {
                onSuccessfulLoad(names, launch = !multiInstall)
            }

            SplitInstallSessionStatus.INSTALLING -> displayLoadingState(state, getString(R.string.installing, names))
            SplitInstallSessionStatus.FAILED -> {
                toastAndLog(getString(R.string.error_for_module, state.errorCode(), state.moduleNames()))
            }
            else -> {
                // do nothing
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        manager = SplitInstallManagerFactory.create(this)
    }

    override fun onResume() {
        // Listener can be registered even without directly triggering a download.
        manager.registerListener(listener)
        super.onResume()
    }

    override fun onPause() {
        // Make sure to dispose of the listener once it's no longer needed.
        manager.unregisterListener(listener)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_gif_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_favorite -> {
            loadAndLaunchModule(favorite)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }



    private fun launchFavoriteActivity() {
        Intent().setClassName(packageName, FAVORITE_GIF_LIST_ACTIVITY)
            .also {
                startActivity(it)
            }
    }

    private fun loadAndLaunchModule(name: String) {
        updateProgressMessage(getString(R.string.loading_module, name))
        // Skip loading if the module already is installed. Perform success action directly.
        if (manager.installedModules.contains(name)) {
            updateProgressMessage(getString(R.string.already_installed))
            onSuccessfulLoad(name, launch = true)
            return
        }

        // Create request to install a feature module by name.
        val request = SplitInstallRequest.newBuilder()
            .addModule(name)
            .build()

        // Load and install the requested feature module.
        manager.startInstall(request)

        updateProgressMessage(getString(R.string.starting_install, name))
    }

    private fun onSuccessfulLoad(moduleName: String, launch: Boolean) {
        hideProgress()
        if (launch) {
            when (moduleName) {
                favorite -> launchFavoriteActivity()
            }
        }
    }

    private fun updateProgressMessage(message: String) {
        if (progress?.visibility != View.VISIBLE) showProgress()
        progressText?.text = message
    }

    private fun hideProgress() {
        progress?.visibility = View.INVISIBLE
    }

    private fun showProgress() {
        progress?.visibility = View.VISIBLE
    }

    private fun displayLoadingState(state: SplitInstallSessionState, message: String) {
        showProgress()

        progressBar?.max = state.totalBytesToDownload().toInt()
        progressBar?.progress = state.bytesDownloaded().toInt()

        updateProgressMessage(message)
    }

    private fun toastAndLog(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        Log.d(TAG, text)
    }
}
