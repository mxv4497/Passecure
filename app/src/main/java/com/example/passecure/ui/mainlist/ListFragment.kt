package com.example.passecure.ui.mainlist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.passecure.R
import com.example.passecure.data.model.PassecureItem
import com.example.passecure.data.repository.PassItemRepository
import com.example.passecure.databinding.FragmentListBinding
import com.example.passecure.ui.adapter.PassecureAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var binding: FragmentListBinding
    private lateinit var repository: PassItemRepository

    private val passecureAdapter = PassecureAdapter {
        navigateToDetail(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentListBinding.bind(view)
        repository = PassItemRepository(requireContext())
        binding.listRV.adapter = passecureAdapter

        lifecycleScope.launch(Dispatchers.IO) {
            val data = repository.getAll()
            withContext(Dispatchers.Main) {
                passecureAdapter.updateData(data)
            }
        }
    }

    private fun navigateToDetail(passecureItem: PassecureItem) {
        val direction = ListFragmentDirections.actionGlobalNewItemFragment(passecureItem)
        findNavController().navigate(direction)
    }
}