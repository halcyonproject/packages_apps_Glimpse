/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.glimpse.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import org.hlcyn.ui.components.HalcyonFloatingBottomBar
import org.hlcyn.ui.components.HalcyonFloatingBottomBarItem
import org.hlcyn.ui.theme.HalcyonTheme
import org.lineageos.glimpse.R

class HalcyonBottomNavBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    fun interface OnTabSelectedListener {
        fun onTabSelected(position: Int)
    }

    private val selectedTabState = mutableIntStateOf(0)
    private var listener: OnTabSelectedListener? = null

    init {
        val composeView = ComposeView(context)
        addView(composeView)
        composeView.setContent {
            HalcyonTheme {
                HalcyonFloatingBottomBar {
                    HalcyonFloatingBottomBarItem(
                        selected = selectedTabState.intValue == 0,
                        onClick = { selectTab(0) },
                        icon = ImageVector.vectorResource(R.drawable.ic_photo_size_select_actual)
                    )
                    HalcyonFloatingBottomBarItem(
                        selected = selectedTabState.intValue == 1,
                        onClick = { selectTab(1) },
                        icon = ImageVector.vectorResource(R.drawable.ic_albums)
                    )
                    HalcyonFloatingBottomBarItem(
                        selected = selectedTabState.intValue == 2,
                        onClick = { selectTab(2) },
                        icon = ImageVector.vectorResource(R.drawable.ic_library)
                    )
                }
            }
        }
    }

    fun setSelectedTab(position: Int) {
        selectedTabState.intValue = position
    }

    fun getSelectedTab(): Int = selectedTabState.intValue

    fun setOnTabSelectedListener(l: OnTabSelectedListener) {
        listener = l
    }

    fun setOnTabSelectedListener(l: (Int) -> Unit) {
        listener = OnTabSelectedListener { position -> l(position) }
    }

    private fun selectTab(position: Int) {
        selectedTabState.intValue = position
        listener?.onTabSelected(position)
    }
}
