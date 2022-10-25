package com.mycomp.firebasemvvm.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.mycomp.firebasemvvm.R
import com.mycomp.firebasemvvm.databinding.FragmentDisplayContentBinding
import com.mycomp.firebasemvvm.domain.model.Media
import com.mycomp.firebasemvvm.ui.utils.kbToMb
import java.util.*


class DisplayContentFragment : Fragment() {
    private var _binding: FragmentDisplayContentBinding? = null
    val binding get() = _binding!!
    private val mediaAdapter = MediaAdapter()
    private var mediaList = mutableListOf<Media>()
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        _binding = FragmentDisplayContentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loadMedia.setOnClickListener {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))

        }
        initRecycler()

        binding.done.setOnClickListener {
            mediaList.forEach { item ->
                val fileToUpload = storageReference.child(item.name)
                fileToUpload.putFile(item.uri)
                    .addOnSuccessListener {
                            binding.mediaRvGroup.visibility = View.GONE
                            binding.successMessage.visibility = View.VISIBLE
                    }
            }
        }
    }

    @SuppressLint("Range")
    val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            context?.let { context ->
                if (uris.isNotEmpty()) {
                    uris.forEach { uri ->

                        var name = ""
                        var size = ""
                        if (uri.scheme.equals("content")) {
                            val cursor = context.contentResolver.query(uri, null, null, null)
                            cursor?.use { cursor ->
                                if (cursor.moveToFirst()) {
                                    name =
                                        cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                                    size = resources.getString(
                                        R.string.size,
                                        cursor.getString(cursor.getColumnIndex(OpenableColumns.SIZE))
                                            .kbToMb()
                                    )
                                }
                            }
                        }

                        mediaList.add(Media(UUID.randomUUID(), uri, name = name, size = size))
                    }
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
                if (uris.size <= 5) {
                    mediaAdapter.submitList(mediaList)
                } else {
                    Toast.makeText(
                        context, "Please choose max 5 files", Toast.LENGTH_SHORT
                    ).show()
                    mediaList = mutableListOf()
                }

                if (mediaList.isNotEmpty()) {
                    binding.loadMedia.visibility = View.GONE
                    binding.mediaRvGroup.visibility = View.VISIBLE
                }
            }

        }

    private fun initRecycler() {
        with(binding) {
            mediaRv.run {
                context?.let { context ->
                    adapter = mediaAdapter
                    layoutManager = LinearLayoutManager(
                        context, LinearLayoutManager.VERTICAL, false
                    )
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}