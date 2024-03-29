package com.iwon.githubuser.page

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
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
import com.iwon.githubuser.db.entity.UserEntity
import com.iwon.githubuser.helper.Result
import com.iwon.githubuser.helper.SettingFactory
import com.iwon.githubuser.helper.SettingPreferences
import com.iwon.githubuser.helper.ViewModelFactory
import com.iwon.githubuser.page.adapter.ListUserAdapter
import com.iwon.githubuser.page.adapter.SearchUserAdapter
import com.iwon.githubuser.page.viewModel.ListUserViewModel
import com.iwon.githubuser.page.viewModel.SettingViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "settings")

class ListUserFragment : Fragment() {

    private var _binding : FragmentListUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var mContext : Context
    private lateinit var mActivity : MainActivity
    private lateinit var listUserAdapter: ListUserAdapter
    private lateinit var listUserViewModel: ListUserViewModel
    private lateinit var settingViewModel : SettingViewModel
    private lateinit var lightMenu: MenuItem
    private lateinit var darkMenu: MenuItem

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mActivity = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListUserBinding.inflate(inflater, container, false)
        // set view model
        val factory : ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        listUserViewModel = ViewModelProvider(mActivity,factory).get(
            ListUserViewModel::class.java
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listUserAdapter = ListUserAdapter{ userEntity ->
            if (userEntity.isBoomark){
                listUserViewModel.unFavorite(userEntity)
            }else{
                listUserViewModel.setFavorite(userEntity)
            }
        }

        getListUser()
    }

    private fun getListUser(){
        if (view != null){
            listUserViewModel.getListUser().observe(viewLifecycleOwner, { result ->
                if (result != null){
                    when(result){
                        is Result.Loading -> {
                            showLoading()
                        }
                        is Result.Success -> {
                            hideLoading()
                            val userData = result.data
                            listUserAdapter.submitList(userData)
                        }
                        is Result.Error -> {
                            hideLoading()
                            var msg = "Terjadi kesalahan ${result.error.message.toString()}"
                            if (GlobalVariable.isIOException(result.error)){
                                msg = mContext.resources.getString(R.string.error_no_connection)
                            }
                            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }

        binding?.rvListUser?.apply {
            layoutManager = GridLayoutManager(mContext, 3)
            setHasFixedSize(true)
            adapter = listUserAdapter

            listUserAdapter.callbackListener = object : ListUserAdapter.CallbackListener{
                override fun onClick(userEntity: UserEntity) {
                    toDetailPage(userEntity.userName)
                }
            }
        }
    }

    private fun toDetailPage(userName : String){
        val bundle = Bundle()
        bundle.putString(GlobalVariable.GRAPH_USERNAME, userName)
        view?.findNavController()
            ?.navigate(R.id.action_listUserFragment_to_detailUserFragment, bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        val menuInflater = activity?.menuInflater
        menuInflater?.inflate(R.menu.option_menu, menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        lightMenu = menu?.findItem(R.id.menu_light_mode)!!
        darkMenu = menu?.findItem(R.id.menu_dark_mode)!!

        // setup
        setupSettingPref()

        // search bar
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
                    getListUser()
                }
                return true
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_favorite -> {
                view?.findNavController()?.navigate(R.id.action_listUserFragment_to_listBookmarkFragment)
                return true
            }
            R.id.menu_light_mode -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                settingViewModel.setTheme(true)
                return true
            }
            R.id.menu_dark_mode -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                settingViewModel.setTheme(false)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupSettingPref(){
        val preferences = SettingPreferences.getInstance(mContext.dataStore)
        settingViewModel = ViewModelProvider(mActivity, SettingFactory(preferences)).get(
            SettingViewModel::class.java
        )
        settingViewModel.getTheme().observe(mActivity, {isDarkMode ->
            if (isDarkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                lightMenu.setVisible(false)
                darkMenu.setVisible(true)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                lightMenu.setVisible(true)
                darkMenu.setVisible(false)
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
                    val msg = StringBuilder(mContext.resources.getString(R.string.search_found)).append(totalCount)

                    loadData(items)
                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
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

    private fun loadData(data : List<ListUsersResponse>){
        hideLoading()
        val searchUserAdapter = SearchUserAdapter(mContext, data, listUserViewModel)
        binding.rvListUser.adapter = searchUserAdapter

        searchUserAdapter.callbackListener = object : SearchUserAdapter.CallbackListener{
            override fun onClick(user: ListUsersResponse) {
                toDetailPage(user.login)
            }
        }
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