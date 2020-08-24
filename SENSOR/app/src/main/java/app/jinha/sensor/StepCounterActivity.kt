package app.jinha.sensor

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import app.jinha.sensor.databinding.ActivityStepCounterBinding

/**
 * 앱 실행 시 반응이 없음 -> OS 10일 떄 권한 설정 필요
 * */
/*
* 참고 : https://copycoding.tistory.com/5?category=1013954
* */
class StepCounterActivity : AppCompatActivity(), SensorEventListener {
    lateinit var binding : ActivityStepCounterBinding
    lateinit var sensorManager: SensorManager
    var stepCountSensor : Sensor? = null
    lateinit var mContext : Context

    val ALL_PERMISSION_OK = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStepCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@StepCounterActivity
        sensorManager = mContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepCountSensor == null){
            Toast.makeText(mContext, "No Step Detect Sensor", Toast.LENGTH_SHORT).show()
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), ALL_PERMISSION_OK)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            ALL_PERMISSION_OK -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext, "승인 O", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(mContext, "승인 X", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, stepCountSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor == stepCountSensor){
            binding.tvStepCount.text = "Step Count : ${event?.values?.get(0)}"
        }
    }
}