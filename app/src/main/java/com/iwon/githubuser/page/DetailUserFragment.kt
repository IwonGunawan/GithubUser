package com.iwon.githubuser.page

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
        showLoading()
        getDetail(username)

        binding.follower.setOnClickListener{ toMultiPage(username) }
        binding.following.setOnClickListener { toMultiPage(username) }
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
                    defaultError(null)
                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                if (GlobalVariable.isIOException(t)){
                    defaultError(mContext.resources.getString(R.string.error_no_connection))
                }else{
                    defaultError(null)
                }
            }
        })
    }

    private fun loadData(user : UserResponse){
        hideLoading()
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

    private fun toMultiPage(username : String){
        val bundle = Bundle()
        bundle.putString(GlobalVariable.GRAPH_USERNAME, username)
        view?.findNavController()?.navigate(R.id.action_detailUserFragment_to_multiPageFragment, bundle)
    }

    private fun showLoading(){
        Glide.with(mContext)
            .asGif()
            .load(R.drawable.loading)
            .into(binding.ivLoading)
        binding.box1.visibility = View.VISIBLE
        binding.box2.visibility = View.GONE
    }

    private fun hideLoading(){
        binding.box1.visibility = View.GONE
        binding.box2.visibility = View.VISIBLE
    }

    private fun defaultError(msg : String?){
        hideLoading()
        val message = msg ?: mContext.resources.getString(R.string.error_5_x_x)

        val builder = AlertDialog.Builder(mContext).create()
        val view  = layoutInflater.inflate(R.layout.dialog, null)
        val tvMsg = view.findViewById<TextView>(R.id.tv_msg)
        val btnOk = view.findViewById<Button>(R.id.btn_ok)

        builder.setView(view)
        tvMsg.text = message
        btnOk.setOnClickListener { builder.dismiss() }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}