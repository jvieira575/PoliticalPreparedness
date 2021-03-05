package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.domain.ApiStatus
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * [ViewModel] designed to store and manage UI-related data in a lifecycle conscious way. Used in
 * [com.example.android.politicalpreparedness.election.VoterInfoFragment].
 */
class VoterInfoViewModel(application: Application) : ViewModel() {

    // The elections database and elections repository
    private val electionsDatabase = ElectionDatabase.getInstance(application)
    private val electionsRepository = ElectionsRepository(electionsDatabase)

    // Live Data to hold voter information's election
    private val _voterInformationElection = MutableLiveData<Election>()
    val voterInformationElection: LiveData<Election>
        get() = _voterInformationElection

    // Live Data to hold voter information's administration body
    private val _voterInformationElectionAdministrationBody = MutableLiveData<AdministrationBody>()
    val voterInformationElectionAdministrationBody: LiveData<AdministrationBody>
        get() = _voterInformationElectionAdministrationBody

    // Live Data to hold voter information's address
    private val _voterInformationCorrespondenceAddress = MutableLiveData<Address>()
    val voterInformationCorrespondenceAddress: LiveData<Address>
        get() = _voterInformationCorrespondenceAddress

    // Live Data to hold whether the election has been saved
    private val _isElectionSaved = MutableLiveData<Boolean>()
    val isElectionSaved: LiveData<Boolean>
        get() = _isElectionSaved

    // Live Data to hold the selected URL
    private val _url = MutableLiveData<String>()
    val url: LiveData<String>
        get() = _url

    // Live Data that stores the status of the most recent voter information request
    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    /**
     * Retrieves voter information.
     */
    fun getVoterInformation(electionId: Int, division: Division) {
        viewModelScope.launch {
            try {
                _status.value = ApiStatus.LOADING
                // Check if this is a saved election first (this will help set up state for the button)
                val savedElection : Election? = electionsDatabase.electionDao.getElection(electionId)
                Timber.i("Election already saved? %s", (savedElection != null))
                _isElectionSaved.value = savedElection != null

                // Get voter information
                val voterInformationResponse = electionsRepository.getVoterInformation(electionId, division)

                // Set individual values
                _voterInformationElection.value = voterInformationResponse.election
                _voterInformationElectionAdministrationBody.value = voterInformationResponse.state?.first()?.electionAdministrationBody
                _voterInformationCorrespondenceAddress.value = voterInformationResponse.state?.first()?.electionAdministrationBody?.correspondenceAddress
                _status.value = ApiStatus.DONE
            } catch (e: Exception) {
                Timber.e(e, "Could not retrieve voter information...")
                _status.value = ApiStatus.ERROR
                clear()
            }
        }
    }

    /**
     * Follows or unfollows an [Election].
     */
    fun followOrUnfollowElection() {
        viewModelScope.launch {
            _voterInformationElection.value?.let {
                if (_isElectionSaved.value == true) {
                    // Delete the Election and set the flag to false
                    electionsRepository.deleteElection(it.id)
                    _isElectionSaved.value = false
                } else {
                    // Save the Election and set the flag to true
                    electionsRepository.saveElection(it)
                    _isElectionSaved.value = true
                }
            }
        }
    }

    /**
     * Sets the selected URL and navigates to the website.
     */
    fun navigateToUrl(url : String) {
        Timber.i("Url clicked is: %s", url)
        _url.value = url
    }

    /**
     * Clears the selected URL after navigating to the website.
     */
    fun navigateToUrlCompleted() {
        _url.value = null
    }

    /**
     * Helper method to clear election data.
     */
    private fun clear() {
        _voterInformationElection.value = null
        _voterInformationElectionAdministrationBody.value = null
        _voterInformationCorrespondenceAddress.value = null
    }
}