package com.example.tobuy.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.tobuy.MainActivity
import com.example.tobuy.database.AppDatabase

class BaseFragment : Fragment() {

    protected val mainActivity: MainActivity
        get () = activity as MainActivity

    protected val appDatabase : AppDatabase
        get() = AppDatabase.getDatabase(requireActivity())
}

