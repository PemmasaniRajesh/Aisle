package com.aisle.testapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.aisle.testapp.data.Profile
import com.aisle.testapp.databinding.FragmentNotesBinding
import com.aisle.testapp.others.NetworkResult
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import java.lang.reflect.Type


@AndroidEntryPoint
class NotesFragment : Fragment() {

    private lateinit var binding:FragmentNotesBinding
    private val notesViewModel by viewModels<NotesViewModel>()
    private lateinit var profilesList:MutableList<Profile>
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profilesList = mutableListOf()
        imageAdapter = ImageAdapter(profilesList,requireContext())
        var layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,true)
        layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
        binding.rvProfiles.layoutManager = layoutManager
        binding.rvProfiles.setHasFixedSize(true)
        binding.rvProfiles.adapter = imageAdapter

        notesViewModel.notesResult.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Failure -> {
                    Toast.makeText(requireContext(),"Error in getting data${it.errorMessage}",Toast.LENGTH_LONG).show()
                }
                is NetworkResult.Loading -> {

                }
                is NetworkResult.Success -> {
                    updateUI(it.data as JsonObject)
                }
            }
        }
    }

    private fun updateUI(data: JsonObject?) {
        if(data!=null){
            val invites = data.getAsJsonObject("invites")
            val profiles = invites.getAsJsonArray("profiles")
            val firstProfile = profiles.get(0).asJsonObject
            val photosArray = firstProfile.getAsJsonArray("photos")
            /*Taking first photo from profile and setting it to imageview...*/
            val firstPhoto = photosArray.get(1).asJsonObject
            val imageUrl = firstPhoto.get("photo").asString
            Glide.with(requireContext()).load(imageUrl).fitCenter().into(binding.ivPersonPhoto)

            /*for recycler view taking photos from likes and setting it to the view...*/
            val likes = data.getAsJsonObject("likes")
            val likedProfiles = likes.getAsJsonArray("profiles")
            val profilesListType: Type = object : TypeToken<ArrayList<Profile>>() {}.type
            profilesList.addAll(Gson().fromJson(likedProfiles,profilesListType))
            profilesList.addAll(Gson().fromJson(likedProfiles,profilesListType))
            imageAdapter.notifyItemRangeInserted(0,profilesList.size)
        }
    }
}