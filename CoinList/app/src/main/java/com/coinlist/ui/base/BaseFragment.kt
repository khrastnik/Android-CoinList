package com.coinlist.ui.base

import com.coinlist.R
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import retrofit2.HttpException
import java.io.IOException

abstract class BaseFragment : DaggerFragment() {

    protected fun showSnackBar(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    protected fun handleException(exception: Throwable?) {
        when (exception) {
            is HttpException -> {
                showSnackBar(getString(R.string.no_internet_error_message))
            }
            is IOException -> {
                showSnackBar(getString(R.string.api_error_message))
            }
            else -> {
                showSnackBar(getString(R.string.unknown_error_message))
            }
        }
    }
}
