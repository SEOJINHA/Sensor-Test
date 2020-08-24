package app.jinha.sensor

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import app.jinha.sensor.databinding.ActivityGpsBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.text.SimpleDateFormat
import java.util.*

/*
 * 진행 중!
 * */
class GPSActivity : AppCompatActivity(), LocationListener {

    companion object {
        private val REQUEST_CHECK_SETTINGS = 0x1
        private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 60000
        private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2
    }

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mSettingsClient: SettingsClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var mCurrentLocation: Location? = null

    var mLatitude : Double? = null                                                                  // 측정 위도
    var mLongitude : Double? = null                                                                 // 측정 경도

    lateinit var locationManager : LocationManager
    var mLastLocation : Location? = null
    var speed : Double = 0.0

    lateinit var binding: ActivityGpsBinding
    var TAG = "GPSActivity"

    lateinit var mContext : Context

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGpsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@GPSActivity

        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(
                    mContext,
                    "Permission Granted",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(
                    mContext,
                    "Permission Denied\n$deniedPermissions",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .check()

        locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, this)

        val isEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        binding.tvGpsEnable.text = ": $isEnable"

        // GPS 설정
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@GPSActivity)
        mSettingsClient = LocationServices.getSettingsClient(this@GPSActivity)

        createLocationCallback()
        createLocationRequest()
        buildLocationSettingsRequest()
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onLocationChanged(location: Location?) {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        val deltaTime: Long?

        val currentSpeed = String.format("%.3f", location?.speed).toDouble()
        binding.tvGetSpeed.text = ": $currentSpeed"
        val formatDate = simpleDateFormat.format(location?.time?.let { Date(it) })
        binding.tvTime.text = ": $formatDate"

        if (mLastLocation != null) {
            // 시간 간격
            deltaTime = (location?.time?.minus(mLastLocation!!.time))
            binding.tvTimeDif.text = ": $deltaTime sec"
            binding.tvDistDif.text = ": ${mLastLocation?.distanceTo(location)} m"

            // 속도 계산
            val speed = mLastLocation?.distanceTo(location)?.toDouble()!!

            val formatLastDate =
                simpleDateFormat.format(location?.time?.let { Date(it) })
            binding.tvLastTime.text = ": $formatLastDate"

            val calSpeed = String.format("%.3f", speed)
            binding.tvCalSpeed.text = ": $calSpeed"

            mLastLocation = location
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onProviderEnabled(provider: String?) {
        TODO("Not yet implemented")
    }

    override fun onProviderDisabled(provider: String?) {
        TODO("Not yet implemented")
    }

    private fun startLocationUpdates() {
        mSettingsClient!!.checkLocationSettings(mLocationSettingsRequest)
            .addOnSuccessListener(this) {
                Log.e(TAG, "모든 위치 설정 O")
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    return@addOnSuccessListener
                }
                mFusedLocationClient!!.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback, Looper.myLooper()
                )
                updateLocation()
            }
            .addOnFailureListener(this) { e ->
                when ((e as ApiException).statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        Log.e(TAG, "위치 설정 업그레이드 진행")
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().

                            // 확인 클릭 시 바로 위치 설정 on
                            val rae = e as ResolvableApiException
                            rae.startResolutionForResult(
                                this@GPSActivity,
                                REQUEST_CHECK_SETTINGS
                            )
                        } catch (sie: IntentSender.SendIntentException) {
                            Log.e(TAG, "PendingIntent 실행 X")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        Log.e(TAG, "위치 설정 변경 불가")
                    }
                }
                updateLocation()
            }
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun updateLocation() {
        if (mCurrentLocation != null) {
            mLatitude = mCurrentLocation!!.latitude
            mLongitude = mCurrentLocation!!.longitude

            val latitudeMessage = String.format(
                Locale.ENGLISH, "%s: %f", "Latitude",
                mCurrentLocation!!.latitude
            )

            Log.e(TAG, latitudeMessage)

            val longitudeMessage = String.format(
                Locale.ENGLISH, "%s: %f", "Longitude",
                mCurrentLocation!!.longitude
            )

            Log.e(TAG, longitudeMessage)
        }
    }

    override fun onResume() {
        super.onResume()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLocationUpdates()
            updateLocation()
        }
    }

    override fun onPause() {
        super.onPause()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this) {
                    Log.e(TAG, "mFusedLocationClient addOnCompleteListener 실행")
                }
        }
    }

    override fun onStop() {
        super.onStop()

        locationManager.removeUpdates(this)
    }

    // 현재 위치 정보 가져오기
    private fun createLocationCallback() {
        mLocationCallback = object : LocationCallback() {
            @SuppressLint("SimpleDateFormat", "SetTextI18n")
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                mCurrentLocation = locationResult.lastLocation
                updateLocation()
            }
        }
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()

        mLocationRequest?.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest?.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CHECK_SETTINGS -> when (resultCode) {
                Activity.RESULT_OK -> Log.e(TAG, "위치 설정 변경 동의")
                Activity.RESULT_CANCELED -> {
                    Log.e(TAG, "위치 설정 변경 동의 X")
                    updateLocation()
                }
            }
        }
    }

    private fun buildLocationSettingsRequest() {
        val builder = LocationSettingsRequest.Builder()
        mLocationRequest?.let { builder.addLocationRequest(it) }
        mLocationSettingsRequest = builder.build()
    }
}