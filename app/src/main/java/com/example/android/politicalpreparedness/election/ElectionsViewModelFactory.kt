package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

//TODO: Create Factory to generate ElectionViewModel with provided election datasource
/**
 * A custom implementation of a [ViewModelProvider.Factory] to generate [ElectionsViewModel] instances.
 */
class ElectionsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    /**
     * Creates a new instance of the [ElectionsViewModel].
    */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ElectionsViewModel(application) as T
        }
        throw IllegalArgumentException("Unable to construct ElectionsViewModel.")
    }
}