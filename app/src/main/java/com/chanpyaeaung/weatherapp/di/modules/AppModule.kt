package com.chanpyaeaung.weatherapp.di.modules

import android.app.Application
import android.arch.persistence.room.Room
import com.chanpyaeaung.weatherapp.api.WeatherApi
import com.chanpyaeaung.weatherapp.database.WeatherAppDatabase
import com.chanpyaeaung.weatherapp.database.dao.WeatherDao
import com.chanpyaeaung.weatherapp.repositories.WeatherRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module(includes = arrayOf(ViewModelModule::class))
class AppModule {

    //Database Injection
    @Provides
    @Singleton
    fun provideDatabase(application: Application): WeatherAppDatabase {
        return Room.databaseBuilder(application,
                WeatherAppDatabase::class.java,
                "WeatherAppDatabase.db")
                .build()
    }

    @Provides
    @Singleton
    fun provideWeatherDao(weatherAppDatabase: WeatherAppDatabase): WeatherDao {
        return weatherAppDatabase.weattherDao()
    }


    //Repository Injection
    @Provides
    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(weatherApi: WeatherApi, weatherDao: WeatherDao, executor: Executor): WeatherRepository {
        return WeatherRepository(weatherApi, weatherDao, executor)
    }


    //Retrofit Injection
    private val BASE_URL = "http://api.openweathermap.org/data/2.5/"

    //TODO 1) Replace API key, currently using test project key
    private val API_KEY = "4ad2f3cd1ed51af73be630833d52f9eb"

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun provideRetrofit(gson: Gson, interceptors: ArrayList<Interceptor>): Retrofit {
        val clientBuilder = OkHttpClient.Builder()
        if (!interceptors.isEmpty()) {
            interceptors.forEach { interceptor ->
                clientBuilder.addInterceptor(interceptor)
            }
        }
        return Retrofit.Builder()
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build()
    }

    @Singleton
    @Provides
    fun provideInterceptors(): ArrayList<Interceptor> {

        val interceptors = arrayListOf<Interceptor>()

        val keyInterceptor = Interceptor { chain ->

            val original = chain.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("appid", API_KEY)
                    .build()

            val requestBuilder = original.newBuilder()
                    .url(url)

            val request = requestBuilder.build()
            return@Interceptor chain.proceed(request)
        }

        interceptors.add(keyInterceptor)
        return interceptors
    }


    @Singleton
    @Provides
    fun provideWeatherApi(restAdapter: Retrofit): WeatherApi {
        return restAdapter.create(WeatherApi::class.java)
    }


}