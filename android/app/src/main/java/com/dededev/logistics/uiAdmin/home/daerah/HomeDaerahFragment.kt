package com.dededev.logistics.uiAdmin.home.daerah

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dededev.logistics.R
import com.dededev.logistics.databinding.FragmentHomeDaerahBinding
import com.dededev.logistics.databinding.FragmentHomePusatBinding
import com.dededev.logistics.uiAdmin.ViewModelFactory
import com.dededev.logistics.uiAdmin.home.pusat.HomePusatViewModel

class HomeDaerahFragment : Fragment() {

    private var _binding: FragmentHomeDaerahBinding? = null
    private lateinit var homeDaerahViewModel: HomeDaerahViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeDaerahBinding.inflate(inflater, container, false)
        val factory = ViewModelFactory.getInstance(this.requireActivity().application)
        homeDaerahViewModel = ViewModelProvider(this, factory)[HomeDaerahViewModel::class.java]



        return binding.root
    }

}