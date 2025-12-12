package com.example.tobuy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tobuy.R
import com.example.tobuy.databinding.FragmentHomeBinding
import java.util.zip.Inflater

class HomeFragment : BaseFragment() {
      var _binding : FragmentHomeBinding? = null
      val binding get() = _binding!!

      override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
      ): View? {
            _binding = FragmentHomeBinding.inflate(inflater,container,false)
            val view = binding.root
            return view
      }

      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.fab.setOnClickListener {
                  navigateViaGraph(R.id.homeFragment_to_AddItemEntity)
            }
            sharedViewModel2.itemsLiveData.observe(viewLifecycleOwner){
                  itemEntities ->
            }
      }

      override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
      }
}