package com.example.tobuy.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.tobuy.MainActivity
import com.example.tobuy.arch.ToBuyViewModel
import com.example.tobuy.database.AppDatabase

 abstract class BaseFragment : Fragment() {

    protected val mainActivity: MainActivity
        get () = activity as MainActivity

    protected val appDatabase : AppDatabase
        get() = AppDatabase.getDatabase(requireActivity())

     protected val sharedViewModel : ToBuyViewModel by viewModels()
     protected val sharedViewModel2 : ToBuyViewModel by activityViewModels()

     protected fun navigateUp(){
         mainActivity.navController.navigateUp()
     }

     protected fun navigateViaGraph(actionId : Int){
         mainActivity.navController.navigate(actionId)
     }
}

