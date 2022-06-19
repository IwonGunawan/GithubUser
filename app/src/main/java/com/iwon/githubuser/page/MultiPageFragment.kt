package com.iwon.githubuser.page

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.iwon.githubuser.GlobalVariable
import com.iwon.githubuser.MainActivity
import com.iwon.githubuser.R
import com.iwon.githubuser.page.adapter.MultiPageAdapter


class MultiPageFragment : Fragment() {
    companion object{
        private val TAB_TITLE = intArrayOf(
            R.string.lbl_follower,
            R.string.lbl_following
        )
    }

    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_multi_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager : ViewPager2 = view.findViewById(R.id.view_pager2)
        val tabs : TabLayout = view.findViewById(R.id.tab_layout)
        val mActivity : MainActivity = activity as MainActivity
        val username : String = arguments?.getString(GlobalVariable.GRAPH_USERNAME).toString()
        val multiPageAdapter = MultiPageAdapter(mActivity, username)


        viewPager.adapter = multiPageAdapter
        TabLayoutMediator(tabs, viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()

        mActivity.supportActionBar?.elevation = 0f
    }

}