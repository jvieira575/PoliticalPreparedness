package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * A custom implementation of a [ViewModelProvider.Factory] to generate [VoterInfoViewModel] instances.
 */
class VoterInfoViewModelFactory(private val application: Application): ViewModelProvider.Factory {

    /**
     * Creates a new instance of the [VoterInfoViewModel].
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VoterInfoViewModel(application) as T
        }
        throw IllegalArgumentException("Unable to construct VoterInfoViewModel.")
    }
}