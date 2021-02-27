package com.example.android.politicalpreparedness.election

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao

//TODO: Create Factory to generate VoterInfoViewModel with provided election datasource
/**
 * A custom implementation of a [ViewModelProvider.Factory] to generate [VoterInfoViewModel] instances.
 */
class VoterInfoViewModelFactory(private val dataSource: ElectionDao): ViewModelProvider.Factory {

    /**
     * Creates a new instance of the [VoterInfoViewModel].
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VoterInfoViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unable to construct VoterInfoViewModel.")
    }
}