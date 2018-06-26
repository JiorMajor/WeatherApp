package com.chanpyaeaung.weatherapp.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class FactoryViewModel @Inject constructor(creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory {

    val creators = creators

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator = creators.get(modelClass)
        if(creator == null) {
            for ( entrySet in creators.entries) {
                if (modelClass.isAssignableFrom(entrySet.key)) {
                    creator = entrySet.value
                    break
                }
            }
        }

        if(creator == null) {
            throw IllegalArgumentException("unknown model class " + modelClass::class.java)
        }

        try {
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}