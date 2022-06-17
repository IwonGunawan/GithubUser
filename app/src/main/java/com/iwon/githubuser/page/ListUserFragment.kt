package com.iwon.githubuser.page

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iwon.githubuser.GlobalVariable
import com.iwon.githubuser.api.ApiConfig
import com.iwon.githubuser.api.response.ListUsersResponse
import com.iwon.githubuser.databinding.FragmentListUserBinding
import com.iwon.githubuser.page.adapter.ListUserAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListUserFragment : Fragment() {

    private var _binding : FragmentListUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var mContext : Context
    private lateinit var listUsers : List<ListUsersResponse>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setup()
        getListUsers()
    }

    private fun setup(){
        val layoutManager = GridLayoutManager(mContext, 3)
        binding.rvListUser.layoutManager = layoutManager

        val itemDecoration = DividerItemDecoration(mContext, layoutManager.orientation)
        binding.rvListUser.addItemDecoration(itemDecoration)
    }

    private fun getListUsers(){
        val call = ApiConfig.getApiService().getListUsers(
            GlobalVariable.headerAccept,
            GlobalVariable.headerAuth
        )
        call.enqueue(object : Callback<List<ListUsersResponse>>{
            override fun onResponse(
                call: Call<List<ListUsersResponse>>,
                response: Response<List<ListUsersResponse>>
            ) {
                if (response.code() == GlobalVariable.iRESPONSE_OK && response.body() != null){
                    listUsers = response.body()!!
                    val adapter = ListUserAdapter(mContext, listUsers)
                    binding.rvListUser.adapter = adapter
                }else{
                    defaultError()
                }
            }

            override fun onFailure(call: Call<List<ListUsersResponse>>, t: Throwable) {
                defaultError()
            }
        })
    }

    private fun defaultError() {

    }
}