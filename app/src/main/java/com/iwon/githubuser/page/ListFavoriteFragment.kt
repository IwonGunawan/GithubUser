package com.iwon.githubuser.page

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.iwon.githubuser.databinding.FragmentListFavoriteBinding
import com.iwon.githubuser.helper.ViewModelFactory
import com.iwon.githubuser.page.adapter.ListFavoriteAdapter
import com.iwon.githubuser.page.viewModel.ListUserViewModel

class ListFavoriteFragment : Fragment() {

    private var _binding: FragmentListFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var mContext: Context
    private lateinit var listFavoriteAdapter: ListFavoriteAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getListFavorite()
    }

    private fun getListFavorite(){
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val listUserViewModel: ListUserViewModel by viewModels { factory }

        listFavoriteAdapter = ListFavoriteAdapter { userEntity ->
            if (userEntity.isBoomark){
                listUserViewModel.unFavorite(userEntity)
            }else{
                listUserViewModel.setFavorite(userEntity)
            }
        }

        listUserViewModel.getFavorite().observe(viewLifecycleOwner, { listFavorite->
            listFavoriteAdapter.submitList(listFavorite)
        })

        binding?.rvListBookmark?.apply {
            layoutManager = LinearLayoutManager(mContext)
            setHasFixedSize(true)
            adapter = listFavoriteAdapter

        }
        binding?.rvListBookmark?.addItemDecoration(DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL))
    }


}