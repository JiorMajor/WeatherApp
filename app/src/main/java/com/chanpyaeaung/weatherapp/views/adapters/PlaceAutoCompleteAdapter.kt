package com.chanpyaeaung.weatherapp.views.adapters

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.chanpyaeaung.weatherapp.R
import com.google.android.gms.common.data.DataBufferUtils
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.tasks.Tasks
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class PlaceAutoCompleteAdapter(context: Context, geoDataClient: GeoDataClient, bounds: LatLngBounds?, filter: AutocompleteFilter):
        ArrayAdapter<AutocompletePrediction>(context, R.layout.my_list_item, android.R.id.text1),
        Filterable {


    var mGeoDataClient = geoDataClient
    var mFilter = filter
    var mBounds = bounds
    lateinit var mResultList: ArrayList<AutocompletePrediction>

    fun setBounds(bounds: LatLngBounds) {
        mBounds = bounds
    }

    override fun getCount(): Int {
        return mResultList.size
    }

    override fun getItem(position: Int): AutocompletePrediction {
        return mResultList.get(position)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row = super.getView(position, convertView, parent)
        val item = getItem(position)
        val tvName = row.findViewById<View>(android.R.id.text1) as TextView
        tvName.text = item.getFullText(null)
        tvName.setTextColor(context.getColor(R.color.white))
        return row
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
                val results = Filter.FilterResults()

                // We need a separate list to store the results, since
                // this is run asynchronously.
                var filterData: ArrayList<AutocompletePrediction>? = ArrayList()

                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    filterData = getAutocomplete(constraint)
                }

                results.values = filterData
                if (filterData != null) {
                    results.count = filterData.size
                } else {
                    results.count = 0
                }

                return results
            }

            override fun publishResults(constraint: CharSequence?, results: Filter.FilterResults?) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    mResultList = results.values as ArrayList<AutocompletePrediction>
                    notifyDataSetChanged()
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated()
                }
            }

            override fun convertResultToString(resultValue: Any): CharSequence {
                // Override this method to display a readable result in the AutocompleteTextView
                // when clicked.
                return if (resultValue is AutocompletePrediction) {
                    resultValue.getFullText(null)
                } else {
                    super.convertResultToString(resultValue)
                }
            }
        }
    }

    private fun getAutocomplete(constraint: CharSequence): ArrayList<AutocompletePrediction>? {

        // Submit the query to the autocomplete API and retrieve a PendingResult that will
        // contain the results when the query completes.
        val results = mGeoDataClient.getAutocompletePredictions(constraint.toString(), mBounds,
                mFilter)

        // This method should have been called off the main UI thread. Block and wait for at most
        // 60s for a result from the API.
        try {
            Tasks.await(results, 60, TimeUnit.SECONDS)
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: TimeoutException) {
            e.printStackTrace()
        }

        try {
            val autocompletePredictions = results.getResult()



            // Freeze the results immutable representation that can be stored safely.
            return DataBufferUtils.freezeAndClose<AutocompletePrediction, AutocompletePrediction>(autocompletePredictions)
        } catch (e: RuntimeException) {
            // If the query did not complete successfully return null
            Toast.makeText(context, "Error contacting API: " + e.toString(),
                    Toast.LENGTH_SHORT).show()
            return null
        }

    }
}