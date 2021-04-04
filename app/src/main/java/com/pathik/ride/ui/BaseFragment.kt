package com.pathik.ride.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VM : ViewModel, VB : ViewBinding> : Fragment() {

}
//
//abstract class BaseFragment <VM: ViewModel, VB: ViewBinding, BR : BaseRepositry> : Fragment(){
//
//}