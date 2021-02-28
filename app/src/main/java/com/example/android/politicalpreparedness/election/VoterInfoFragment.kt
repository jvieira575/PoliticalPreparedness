package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.network.models.Division

/**
 * A [Fragment] that displays voter information.
 */
class VoterInfoFragment : Fragment() {

    private val viewModel: VoterInfoViewModel by lazy {
        ViewModelProvider(this, VoterInfoViewModelFactory(requireActivity().application)).get(VoterInfoViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val arguments = VoterInfoFragmentArgs.fromBundle(arguments!!)
        val electionId : Int = arguments.argElectionId
        val division : Division = arguments.argDivision

        // Retrieve the voter information
        viewModel.getVoterInformation(electionId, division)

        viewModel.url.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                loadUrl(it)
            }
        })

        return binding.root
    }

    /**
     *
     */
    private fun loadUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
        viewModel.navigateToUrlCompleted()
    }
}