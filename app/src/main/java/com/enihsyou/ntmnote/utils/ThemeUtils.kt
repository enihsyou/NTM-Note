package com.enihsyou.ntmnote.utils

import android.app.Application
import com.enihsyou.ntmnote.R

object ThemeUtils {
    fun changeTheme(application: Application, theme: Theme) {
        val themeId = when (theme) {
            Theme.RED         -> R.style.AppTheme
            Theme.BROWN       -> R.style.AppTheme
            Theme.BLUE        -> R.style.AppTheme
            Theme.BLUE_GREY   -> R.style.AppTheme
            Theme.YELLOW      -> R.style.AppTheme
            Theme.DEEP_PURPLE -> R.style.AppTheme
            Theme.PINK        -> R.style.AppTheme
            Theme.GREEN       -> R.style.AppTheme
        }
        application.setTheme(themeId)
    }

    enum class Theme(val raw: Int) {
        RED(0x00),
        BROWN(0x01),
        BLUE(0x02),
        BLUE_GREY(0x03),
        YELLOW(0x04),
        DEEP_PURPLE(0x05),
        PINK(0x06),
        GREEN(0x07);
    }
}
