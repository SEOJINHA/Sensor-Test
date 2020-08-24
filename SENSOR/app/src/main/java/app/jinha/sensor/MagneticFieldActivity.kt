package app.jinha.sensor

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.jinha.sensor.databinding.ActivityMagneticFieldBinding
import kotlin.math.sqrt

/*
* 참고 : https://copycoding.tistory.com/11?category=1013954
* */
class MagneticFieldActivity : AppCompatActivity(), SensorEventListener {
    lateinit var binding: ActivityMagneticFieldBinding
    lateinit var sensorManager: SensorManager
    lateinit var magneticSensor: Sensor
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMagneticFieldBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@MagneticFieldActivity
        sensorManager = mContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        magneticSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor == magneticSensor) {
            val magX = event.values[0]
            val magY = event.values[1]
            val magZ = event.values[2]

            val magnitude = sqrt((magX * magX) + (magY * magY) + (magZ * magZ))

            binding.tvXaxis.text = "X axis : " + String.format("%.2f", magX)
            binding.tvYaxis.text = "Y axis : " + String.format("%.2f", magY)
            binding.tvZaxis.text = "Z axis : " + String.format("%.2f", magZ)
            binding.tvMagnetic.text = "Magnetic axis : " + String.format("%.2f", magnitude) + "\u00B5Tesla"
        }
    }
}