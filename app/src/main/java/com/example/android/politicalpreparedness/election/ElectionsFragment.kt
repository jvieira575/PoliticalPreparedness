package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import timber.log.Timber
import java.util.*

/**
 * A [Fragment] that displays upcoming and saved elections.
 */
class ElectionsFragment: Fragment() {

    //TODO: Declare ViewModel
    private val viewModel: ElectionsViewModel by lazy {
        ViewModelProvider(this, ElectionsViewModelFactory(requireActivity().application)).get(ElectionsViewModel::class.java)
    }

    private lateinit var savedElectionListAdapter: ElectionListAdapter
    private lateinit var upcomingElectionListAdapter: ElectionListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //TODO: Add ViewModel values and create ViewModel
        // Observe whether the user has navigated to the voter information view
        viewModel.navigateToVoterInformation.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                navigateToVoterInformation(it)
                viewModel.navigateToVoterInformationCompleted()
            }
        })
        // TODO: Add binding values

        // TODO: Initiate recycler adapters
        savedElectionListAdapter = ElectionListAdapter(ElectionListener {
            Timber.e("Saved Election clicked. Name: %s Division: %s", it.name, it.division)
            //TODO: Link elections to voter info
            viewModel.navigateToVoterInformation(it)
        })

        upcomingElectionListAdapter = ElectionListAdapter(ElectionListener {
            //TODO: Link elections to voter info
            Timber.e("Upcoming Election clicked. Name: %s Division: %s", it.name, it.division)
            //navigateToVoterInformation(it)
            viewModel.navigateToVoterInformation(it)
        })

        // TODO: Populate recycler adapters
        binding.upcomingElectionsRecyclerView.adapter = upcomingElectionListAdapter
        binding.savedElectionsRecyclerView.adapter = savedElectionListAdapter

        // TODO: Refresh just the saved elections? Observe saved elections and update list
        // TODO: Refresh just the saved elections? Observe upcoming elections and update list
        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer<List<Election>> { upcomingElections ->
            upcomingElections?.apply {
                upcomingElectionListAdapter.elections = upcomingElections
            }
        })

        viewModel.savedElections.observe(viewLifecycleOwner, Observer<List<Election>> { savedElections ->
            savedElections?.apply {
                savedElectionListAdapter.elections = savedElections
            }
        })

        return binding.root
    }

    //TODO: Refresh adapters when fragment loads
    /**
     * Lifecyle method that is called when the [Fragment] has its views loaded.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Refresh just the saved elections? Observe upcoming elections and update list
//        viewModel.upcomingElections.observe(viewLifecycleOwner, Observer<List<Election>> { upcomingElections ->
//            upcomingElections?.apply {
//                upcomingElectionListAdapter.elections = upcomingElections
//            }
//        })
//
//        viewModel.savedElections.observe(viewLifecycleOwner, Observer<List<Election>> { savedElections ->
//            savedElections?.apply {
//                savedElectionListAdapter.elections = savedElections
//            }
//        })
    }

    /**
     * Navigates the the [VoterInfoFragment] and passes the required [Election] and [Division] data.
     */
    private fun navigateToVoterInformation(election : Election) {
        Timber.e("Upcoming Election clicked. Name: %s Division: %s", election.name, election.division)
        this.findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id, election.division))
    }
}