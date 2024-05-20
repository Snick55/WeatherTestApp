package com.snick55.weathertestapp.presentation

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.snick55.weathertestapp.R
import com.snick55.weathertestapp.core.Container
import com.snick55.weathertestapp.core.DisabledGPSException
import com.snick55.weathertestapp.core.GenericException
import com.snick55.weathertestapp.core.observe
import com.snick55.weathertestapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

//use activity without fragment because we have only one screen
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = ForecastsAdapter()
        binding.forecastRV.adapter = adapter
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
        observe(adapter)
        binding.root.setTryAgainListener {
            getLocation()
        }

    }

    private fun observe(adapter: ForecastsAdapter){
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                binding.root.observe(this@MainActivity, viewModel.weather) {
                    binding.cityNameTV.text = it.name
                    binding.currentTempTV.text = getString(R.string.temp_title,it.currentTemp.toString())
                    adapter.submitList(it.forecasts)
                }
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    this,
                    permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                permission.ACCESS_FINE_LOCATION,
                permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            } else {
                requestPermissions()
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        binding.root.container = Container.Error(GenericException())
                    } else {
                        val lat = location.latitude
                        val long = location.longitude
                        viewModel.getWeatherByLatLon(lat, long)
                    }
                }
            } else {
                Toast.makeText(this, "Please Turn on Your device Location", Toast.LENGTH_LONG)
                    .show()
                binding.root.container = Container.Error(DisabledGPSException())
            }
        } else requestPermissions()
    }


    private companion object {
        const val PERMISSION_ID = 100
    }
}
