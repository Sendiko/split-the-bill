package com.sendiko.split_the_bill.repository.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sendiko.split_the_bill.repository.AppPreferences

class ViewModelFactory private constructor(
    private val app: Application,
    private val pref: AppPreferences
): ViewModelProvider.NewInstanceFactory(){

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application, preferences: AppPreferences): ViewModelFactory {
            when (INSTANCE) {
                null -> {
                    synchronized(ViewModelFactory::class.java) {
                        INSTANCE = ViewModelFactory(application, preferences)
                    }
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplitBillViewModel::class.java)-> SplitBillViewModel(app, pref) as T
            else -> throw IllegalArgumentException("Unknown modelclass: " + modelClass.name)
        }
    }

}