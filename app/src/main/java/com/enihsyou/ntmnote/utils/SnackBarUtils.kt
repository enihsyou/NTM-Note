package com.enihsyou.ntmnote.utils

import android.support.annotation.NonNull
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View

object SnackBarUtils {
    fun show(@NonNull view: View, @StringRes message: Int) =
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()

    fun showAction(@NonNull view: View, @StringRes message: Int, @StringRes action: Int, listener: View.OnClickListener) =
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setAction(action, listener)
            .show()
}
