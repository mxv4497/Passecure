package com.example.passecure.ui.newsave

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.passecure.R
import com.example.passecure.data.model.PassecureItem
import com.example.passecure.data.repository.PassItemRepository
import com.example.passecure.databinding.FragmentNewItemBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class NewItemFragment : Fragment(R.layout.fragment_new_item) {
    private lateinit var binding: FragmentNewItemBinding
    private lateinit var repository: PassItemRepository
    private val args: NewItemFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNewItemBinding.bind(view)
        repository = PassItemRepository(requireContext())

        args.passecureItem?.let {

            with(binding){
                nameET.setText(it.name)
                usernameET.setText(it.username)
                passwordET.setText(it.password)
                descriptionET.setText(it.description)

                saveBtn.text = getString(R.string.update)
                generateBtn.text = getString(R.string.generateBtn_rename_on_existing_item)
                deleteBtn.visibility = View.VISIBLE

                deleteBtn.setOnClickListener {_ ->
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Delete ${nameET.text}?")
                        .setMessage("Are you sure you want to delete?")
                        .setNeutralButton(resources.getString(R.string.cancel)) { _, _ ->}
                        .setPositiveButton(resources.getString(R.string.delete)) { _, _ ->
                            deleteFromDatabase(it)
                        }
                        .show()
                }
            }
        }

        binding.cancelBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Action canceled.", Toast.LENGTH_SHORT).show()
            navigateToList()
        }
        
        binding.generateBtn.setOnClickListener {
            val newUUID = UUID.randomUUID()
            binding.passwordET .setText("$newUUID")
        }

        checkForErrors()
    }

    private fun navigateToList(){
        val direction = NewItemFragmentDirections.actionNewItemFragmentToListFragment()
        findNavController().navigate(direction)
    }

    private fun deleteFromDatabase(passecureItem: PassecureItem){
        lifecycleScope.launch(Dispatchers.IO) {
            repository.delete(passecureItem)
        }
        Toast.makeText(requireContext(), "Item ${passecureItem.name} deleted.", Toast.LENGTH_SHORT).show()
        navigateToList()
    }

    private fun saveToDatabase(passecureItem: PassecureItem){
        lifecycleScope.launch(Dispatchers.IO) {
            repository.insert(passecureItem)
        }
        Toast.makeText(requireContext(), "Item ${passecureItem.name} created.", Toast.LENGTH_SHORT).show()
        navigateToList()
    }

    private fun updateDatabase(passecureItem: PassecureItem){
        lifecycleScope.launch(Dispatchers.IO) {
            repository.update(passecureItem)
        }
        Toast.makeText(requireContext(), "Item ${passecureItem.name} updated.", Toast.LENGTH_SHORT).show()
        navigateToList()
    }

    private fun checkForErrors(){
        with (binding){
            saveBtn.setOnClickListener {
                if(nameET.text.isNullOrEmpty()){
                    nameET.error = "Can not be empty!"
                } else {
                    nameET.error = null
                }

                if(usernameET.text.isNullOrEmpty()){
                    usernameET.error = "Can not be empty!"
                } else {
                    usernameET.error = null
                }

                if(passwordET.text.isNullOrEmpty()){
                    passwordET.error = "Can not be empty!"
                } else {
                    passwordET.error = null
                }

                if(nameET.error == null && usernameET.error == null && passwordET.error == null){
                    val passecureItem = PassecureItem(
                            args.passecureItem?.id ?: 0,
                            nameET.text.toString(),
                            usernameET.text.toString(),
                            passwordET.text.toString(),
                            descriptionET.text.toString() )

                    if (args.passecureItem == null){
                        saveToDatabase(passecureItem)
                    } else {
                        MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Update ${nameET.text}?")
                                .setMessage("Are you sure you want to update?")
                                .setNeutralButton(resources.getString(R.string.cancel)) { _, _ ->}
                                .setPositiveButton(resources.getString(R.string.update)) { _, _ ->
                                    updateDatabase(passecureItem)
                                }
                                .show()

                    }

                    hideKeyboard()
                }
            }
        }
    }
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}
fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}