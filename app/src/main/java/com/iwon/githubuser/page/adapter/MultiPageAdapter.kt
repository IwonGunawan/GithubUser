package com.iwon.githubuser.page.adapter

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iwon.githubuser.page.FollowerFragment
import com.iwon.githubuser.page.FollowingFragment

class MultiPageAdapter(activity: AppCompatActivity, private val username: String) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position){
            0 -> fragment = FollowerFragment(username)
            1 -> fragment = FollowingFragment(username)
        }

        return fragment as Fragment
    }
}