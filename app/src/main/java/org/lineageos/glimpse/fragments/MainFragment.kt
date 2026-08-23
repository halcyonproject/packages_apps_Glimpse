/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.glimpse.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import org.lineageos.glimpse.R
import org.lineageos.glimpse.SettingsActivity
import org.lineageos.glimpse.ext.getViewProperty
import org.lineageos.glimpse.models.AlbumType
import org.lineageos.glimpse.ui.views.HalcyonBottomNavBar

class MainFragment : Fragment(R.layout.fragment_main) {
    // Views
    private val appBarLayout by getViewProperty<AppBarLayout>(R.id.appBarLayout)
    private val collapsingToolbarLayout by getViewProperty<CollapsingToolbarLayout>(R.id.collapsingToolbarLayout)
    private val navigationBarView by getViewProperty<HalcyonBottomNavBar>(R.id.navigationBarView)
    private val settingsMaterialButton by getViewProperty<MaterialButton>(R.id.settingsMaterialButton)
    private val toolbar by getViewProperty<MaterialToolbar>(R.id.toolbar)
    private val viewPager2 by getViewProperty<ViewPager2>(R.id.viewPager2)

    private val onPageChangeCallback by lazy {
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                navigationBarView.setSelectedTab(position)
                collapsingToolbarLayout.title = getString(tabTitles[position])
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Collapsing toolbar title
        collapsingToolbarLayout.title = getString(tabTitles[viewPager2.currentItem])

        settingsMaterialButton.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
        }

        // ViewPager2
        viewPager2.isUserInputEnabled = false
        viewPager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = fragments.size
            override fun createFragment(position: Int) = fragments[position]()
        }
        viewPager2.offscreenPageLimit = fragments.size
        viewPager2.registerOnPageChangeCallback(onPageChangeCallback)

        navigationBarView.setOnTabSelectedListener { position ->
            viewPager2.currentItem = position
            collapsingToolbarLayout.title = getString(tabTitles[position])
            appBarLayout.setExpanded(true, true)
        }
    }

    override fun onDestroyView() {
        // ViewPager2
        viewPager2.unregisterOnPageChangeCallback(onPageChangeCallback)
        viewPager2.adapter = null

        super.onDestroyView()
    }

    companion object {
        private val tabTitles = intArrayOf(
            R.string.reels_title,
            R.string.albums_title,
            R.string.library_title,
        )

        // Keep in sync with HalcyonBottomNavBar tabs
        private val fragments = arrayOf(
            {
                AlbumFragment().apply {
                    arguments = AlbumFragment.createBundle(
                        albumType = AlbumType.REELS,
                        hideToolbar = true,
                    )
                }
            },
            { AlbumsFragment() },
            { LibraryFragment() },
        )
    }
}
