package com.iwon.githubuser.page

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.iwon.githubuser.GlobalVariable
import com.iwon.githubuser.R
import com.iwon.githubuser.api.ApiConfig
import com.iwon.githubuser.api.response.ListUsersResponse
import com.iwon.githubuser.databinding.FragmentFollowerBinding
import com.iwon.githubuser.page.adapter.FollowerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowerFragment(private val username : String) : Fragment() {

    private var _binding : FragmentFollowerBinding? = null
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
        _binding = FragmentFollowerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading()
        setup()
        getListFollowers()
    }

    private fun setup(){
        val layoutManager = LinearLayoutManager(mContext)
        binding.rvFollower.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(mContext, layoutManager.orientation)
        binding.rvFollower.addItemDecoration(itemDecoration)
    }

    private fun getListFollowers(){
        val call = ApiConfig.getApiService().getFollowers(
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
        hideLoading()
        val adapter = FollowerAdapter(mContext, list)
        binding.rvFollower.adapter = adapter
    }

    private fun showLoading(){
        Glide.with(mContext)
            .asGif()
            .load(R.drawable.loading)
            .into(binding.ivLoading)
        binding.ivLoading.visibility = View.VISIBLE
    }

    private fun hideLoading(){
        binding.ivLoading.visibility = View.GONE
    }

    private fun defaultError() {
        hideLoading()
        Toast.makeText(mContext, mContext.resources.getString(R.string.error_5_x_x), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}