package com.iwon.githubuser.page

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.iwon.githubuser.GlobalVariable
import com.iwon.githubuser.R
import com.iwon.githubuser.api.ApiConfig
import com.iwon.githubuser.api.response.ListUsersResponse
import com.iwon.githubuser.databinding.FragmentFollowingBinding
import com.iwon.githubuser.page.adapter.FollowingAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FollowingFragment(private val username : String) : Fragment() {

    private var _binding : FragmentFollowingBinding? = null
    private val binding get() = _binding!!
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
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
        getListFollowing()
    }

    private fun setup(){
        val layoutManager = LinearLayoutManager(mContext)
        binding.rvFollowing.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(mContext, layoutManager.orientation)
        binding.rvFollowing.addItemDecoration(itemDecoration)
    }

    private fun getListFollowing(){
        val call = ApiConfig.getApiService().getFollowing(
            GlobalVariable.headerAccept,
            GlobalVariable.headerAuth,
            username
        )
        call.enqueue(object : Callback<List<ListUsersResponse>>{
            override fun onResponse(
                call: Call<List<ListUsersResponse>>,
                response: Response<List<ListUsersResponse>>
            ) {
                if (response.code() == GlobalVariable.iRESPONSE_OK && response.body() != null){
                    loadData(response.body()!!)
                }else{
                    defaultError()
                }
            }

            override fun onFailure(call: Call<List<ListUsersResponse>>, t: Throwable) {
                defaultError()
            }
        })
    }

    private fun loadData(list : List<ListUsersResponse>){
        val adapter = FollowingAdapter(mContext, list)
        binding.rvFollowing.adapter = adapter
    }

    private fun defaultError(){
        Log.d(GlobalVariable.TAG, "defaultError: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}