package com.chanpyaeaung.weatherapp.views.activities

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.OnLongClick
import com.chanpyaeaung.weatherapp.R
import com.chanpyaeaung.weatherapp.common.Utils
import com.chanpyaeaung.weatherapp.entities.WeatherModelEntity
import com.chanpyaeaung.weatherapp.viewmodels.WeatherViewModel
import com.chanpyaeaung.weatherapp.views.adapters.WeatherAdapter
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: WeatherViewModel
    lateinit var weatherAdapter: WeatherAdapter
    lateinit var currentWeather: WeatherModelEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.configureDagger()
        this.configureViewModel()
        setupViews()

        ivWeather.setOnLongClickListener {
            val vibrate = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrate.vibrate(VibrationEffect.createOneShot(100,VibrationEffect.DEFAULT_AMPLITUDE))
            }else{
                //deprecated in API 26
                vibrate.vibrate(100)
            }
            val item = currentWeather
            val builder = AlertDialog.Builder(this@MainActivity)
                    .setTitle("Remove Country?")
                    .setMessage(item.name)
                    .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
                        viewModel.deleteWeather(item.id)
                        viewModel.getWeatherList().observe(this, Observer { weatherlist ->  copyWeathers(weatherlist)})
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                    })
                    .create()
            builder.setOnShowListener{ dialog ->
                builder.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.black))
                builder.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.black))
            }
            val dialog = builder.show()
            return@setOnLongClickListener true
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_city_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.add_city) {
            startActivity(Intent(this, AddCityActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityDispatchingAndroidInjector
    }

    fun configureDagger() {
        AndroidInjection.inject(this)
    }

    fun configureViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WeatherViewModel::class.java)
        viewModel.getWeatherList().observe(this, Observer { weatherlist ->  copyWeathers(weatherlist)})
    }

    fun copyWeathers(list: List<WeatherModelEntity>?) {
        val extra = intent.extras
        if (list !== null) {
            if(list.isEmpty() && extra == null) {
                intent = Intent(this, AddCityActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                if (extra != null) {
                    val bundle = extra.getBundle("LatLng")
                    if (bundle != null) {
                        val lat = bundle.getDouble("Lat")
                        val lng = bundle.getDouble("Lng")
                        viewModel.setLocation(lat, lng)
                        viewModel.getWeather().observe(this, Observer { weather -> updateUI(weather) })
                    } else {
                        updateUI(list.get(0))
                    }
                } else {
                    updateUI(list.get(0))
                }
                if(!list.isEmpty()) {
                    val othersWeathers = updateWeatherList(list)
                    weatherAdapter = WeatherAdapter(this, othersWeathers, object: WeatherAdapter.CustomClickListener{
                         override fun onItemClick(view: View, position: Int) {
                             updateUI(weatherAdapter.getItem(position))
                             val others = updateWeatherList(list)
                             weatherAdapter.replaceItems(others)
                        }
                    })
                    recyclerView.adapter = weatherAdapter
                }
            }
        }
    }


    fun setupViews() {
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setBackgroundColor(resources.getColor(Utils.getColorByTime()))
        constraintLayout.setBackgroundColor(resources.getColor(Utils.getColorByTime()))

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


    }

    fun updateUI(weatherModelEntity: WeatherModelEntity?) {

        if(weatherModelEntity != null) {
            currentWeather = weatherModelEntity
            ivWeather.setImageDrawable(resources.getDrawable(weatherModelEntity.icon))
            tvWeather.text = weatherModelEntity.condition
            tvCity.text = weatherModelEntity.name
            tvTemperature.text = Utils.formatTempartureString(weatherModelEntity.temp)

        }

    }

    fun updateWeatherList(mList: List<WeatherModelEntity>): ArrayList<WeatherModelEntity> {
        val othersWeathers = ArrayList<WeatherModelEntity>()
        for(w in mList) {
            if(!w.name.equals(tvCity.text.toString(), false)) {
                othersWeathers.add(w)
            }
        }
        return othersWeathers
    }


}
