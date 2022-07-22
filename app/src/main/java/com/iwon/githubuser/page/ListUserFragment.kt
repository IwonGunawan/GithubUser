package com.iwon.githubuser.page

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.iwon.githubuser.GlobalVariable
import com.iwon.githubuser.MainActivity
import com.iwon.githubuser.R
import com.iwon.githubuser.api.ApiConfig
import com.iwon.githubuser.api.response.ListUsersResponse
import com.iwon.githubuser.api.response.UserSearchResponse
import com.iwon.githubuser.databinding.FragmentListUserBinding
import com.iwon.githubuser.db.entity.Favorite
import com.iwon.githubuser.helper.ViewModelFactory
import com.iwon.githubuser.page.adapter.ListUserAdapter
import com.iwon.githubuser.page.viewModel.FavoriteViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class ListUserFragment : Fragment() {

    private var _binding : FragmentListUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var mContext : Context
    private lateinit var mActivity : MainActivity
    private lateinit var mFavoriteViewModel : FavoriteViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mActivity = activity as MainActivity
        mFavoriteViewModel = obtainViewModel(mActivity)
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

        showLoading()
        setup()
        getListUsers()
    }

    private fun setup(){
        val layoutManager = GridLayoutManager(mContext, 3)
        binding.rvListUser.layoutManager = layoutManager
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
                    loadData(response.body()!!)
                }else{
                    defaultError(null)
                }
            }

            override fun onFailure(call: Call<List<ListUsersResponse>>, t: Throwable) {
               if(GlobalVariable.isIOException(t)){
                    defaultError(mContext.resources.getString(R.string.error_no_connection))
               }else{
                    defaultError(null)
               }
            }
        })
    }

    private fun loadData(data : List<ListUsersResponse>){
        hideLoading()
        val adapter = ListUserAdapter(mContext, data)
        binding.rvListUser.adapter = adapter

        adapter.callbackListener = object : ListUserAdapter.CallbackListener{
            override fun onClick(user: ListUsersResponse) {
                val bundle = Bundle()
                bundle.putString(GlobalVariable.GRAPH_USERNAME, user.login)
                view?.findNavController()
                    ?.navigate(R.id.action_listUserFragment_to_detailUserFragment, bundle)
            }

            override fun onFavorite(user: ListUsersResponse) {
                var favorite : Favorite? = Favorite(
                    userId = user.id,
                    userName = user.login,
                    avatarUrl = user.avatarUrl,
                    linkUrl = user.url)
                mFavoriteViewModel.insert(favorite as Favorite)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        val menuInflater = activity?.menuInflater
        menuInflater?.inflate(R.menu.option_menu, menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = this.resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                showLoading()
                searchUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText?.length == 0){
                    getListUsers()
                }
                return true
            }

        })
    }

    private fun searchUser(query: String?){
        val call = ApiConfig.getApiService().searchUser(
            GlobalVariable.headerAuth,
            query.toString()
        )
        call.enqueue(object : Callback<UserSearchResponse>{
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                if (response.code() == GlobalVariable.iRESPONSE_OK && response.body() != null){
                    val totalCount = response.body()!!.totalCount.toString()
                    val items = response.body()!!.items
                    //val msg = StringBuilder(mContext.resources.getString(R.string.search_found)).append(totalCount)

                    loadData(items)
                    //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
                }else{
                    defaultError(null)
                }
            }

            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
               if (GlobalVariable.isIOException(t)){
                   defaultError(mContext.resources.getString(R.string.error_no_connection))
               }else{
                   defaultError(null)
               }
            }

        })
    }

    private fun obtainViewModel(activity: AppCompatActivity) : FavoriteViewModel{
        Log.d(GlobalVariable.TAG, "connect listUserFragment with viewModel")
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
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

    private fun defaultError(msg : String?) {
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