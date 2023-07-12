package com.dededev.logistics.uiAdmin.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dededev.logistics.databinding.FragmentHomeBinding
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentContainerView
import com.dededev.logistics.R
import com.dededev.logistics.uiAdmin.ViewModelFactory
import com.dededev.logistics.uiAdmin.home.daerah.HomeDaerahFragment
import com.dededev.logistics.uiAdmin.home.pusat.HomePusatFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var containerPusat: FragmentContainerView
    private lateinit var containerDaerah: FragmentContainerView
    private lateinit var fragmentPusat: HomePusatFragment
    private lateinit var fragmentDaerah: HomeDaerahFragment
    private lateinit var buttonPusat: TextView
    private lateinit var buttonDaerah: TextView

    private var isFragmentPusatVisible = true

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = obtainViewModel(this@HomeFragment)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        containerPusat = binding.fragmentContainerPusat
        containerDaerah = binding.fragmentContainerDaerah
        buttonPusat = binding.btnPusat
        buttonDaerah = binding.btnDaerah
        fragmentPusat = HomePusatFragment()
        fragmentDaerah = HomeDaerahFragment()


        buttonPusat.setOnClickListener {
            if (!isFragmentPusatVisible) {
                buttonPusat.apply {
                    setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.main_blue))
                }
                buttonDaerah.apply {
                    setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_gray))
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
                }
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_pusat, fragmentPusat)
                    .commit()
                isFragmentPusatVisible = true
                homeViewModel.isPusatActive = true
            }
        }

        buttonDaerah.setOnClickListener {
            if (isFragmentPusatVisible) {
                buttonPusat.apply {
                    setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_gray))
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
                }
                buttonDaerah.apply {
                    setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                    setTextColor(ContextCompat.getColor(requireContext(), R.color.main_blue))
                }
                childFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_pusat, fragmentDaerah)
                    .commit()
                isFragmentPusatVisible = false
                homeViewModel.isPusatActive = false
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (homeViewModel.isPusatActive) {
            buttonPusat.apply {
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.main_blue))
            }
            buttonDaerah.apply {
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_gray))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            }
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_pusat, fragmentPusat)
                .commit()
        } else {
            buttonPusat.apply {
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bg_gray))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            }
            buttonDaerah.apply {
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
                setTextColor(ContextCompat.getColor(requireContext(), R.color.main_blue))
            }
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_pusat, fragmentDaerah)
                .commit()
        }
    }

    private fun obtainViewModel(fragment: HomeFragment): HomeViewModel {
        val factory = ViewModelFactory.getInstance(fragment.requireActivity().application)
        return ViewModelProvider(fragment, factory)[HomeViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}