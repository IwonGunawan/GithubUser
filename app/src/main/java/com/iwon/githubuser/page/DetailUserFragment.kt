package com.iwon.githubuser.page

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.iwon.githubuser.GlobalVariable
import com.iwon.githubuser.R
import com.iwon.githubuser.api.ApiConfig
import com.iwon.githubuser.api.response.UserResponse
import com.iwon.githubuser.databinding.FragmentDetailUserBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserFragment : Fragment() {

    private var _binding : FragmentDetailUserBinding? = null
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
        _binding = FragmentDetailUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(GlobalVariable.GRAPH_USERNAME).toString()
        getDetail(username)

        binding.follower.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(GlobalVariable.GRAPH_USERNAME, username)
            view?.findNavController()?.navigate(R.id.action_detailUserFragment_to_multiPageFragment, bundle)
        }
    }

    private fun getDetail(username : String){
        val call = ApiConfig.getApiService().getUser(
            GlobalVariable.headerAccept,
            GlobalVariable.headerAuth,
            username
        )
        call.enqueue(object : Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.code() == GlobalVariable.iRESPONSE_OK && response.body() != null){
                    loadData(response.body()!!)
                }else{
                    defaultError()
                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                defaultError()
            }
        })

    }

    private fun loadData(user : UserResponse){
        Log.d(GlobalVariable.TAG, "loadData: ${user.name} ")
        Glide.with(mContext)
            .load(user.avatarUrl)
            .into(binding.imgAvatar)
        binding.tvName.text = changeNullToStr(user.name)
        binding.tvUsername.text = user.login
        binding.tvCompany.text = user.company
        binding.tvBlog.text = user.blog
        binding.tvLocation.text = user.location
        binding.tvBio.text = user.bio
        binding.tvTwitter.text = user.twitterUsername
        binding.tvFollower.text = user.followers.toString()
        binding.tvFollowing.text = user.following.toString()
    }

    private fun changeNullToStr(value: String): String {
        var data = if (value == null){
            "<no data>"
        }else{
            value
        }
        return data
    }

    private fun defaultError(){
        Log.d(GlobalVariable.TAG, "defaultError:")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}