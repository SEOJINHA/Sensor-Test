package app.jinha.sensor

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.jinha.sensor.databinding.ActivityAccelerometerBinding
import kotlin.math.pow
import kotlin.math.sqrt

/*
* 참고 : https://copycoding.tistory.com/12?category=1013954
* */
class AccelerometerActivity : AppCompatActivity(), SensorEventListener {
    lateinit var binding : ActivityAccelerometerBinding
    lateinit var sensorManager: SensorManager
    lateinit var accelerSensor: Sensor
    lateinit var linearAcclerSensor: Sensor

    lateinit var mContext : Context
    val alpha = 0.8f
    var lastUpdate = 0.toLong()
    var tempX : Float = 0.0f
    var tempY : Float = 0.0f
    var tempZ : Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccelerometerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@AccelerometerActivity
        sensorManager = mContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        linearAcclerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor == accelerSensor) {
            val gAccX = event.values[0]
            val gAccY = event.values[1]
            val gAccZ = event.values[2]

            val tempX = alpha * tempX + (1 - alpha) * gAccX
            val tempY = alpha * tempY + (1 - alpha) * gAccY
            val tempZ = alpha * tempZ + (1 - alpha) * gAccZ

            val accX = gAccX - tempX
            val accY = gAccY - tempY
            val accZ = gAccZ - tempZ

            val gTotal = sqrt(gAccX.pow(2) + gAccY.pow(2) + gAccZ.pow(2))
            val total = sqrt(accX.pow(2) + accY.pow(2) + accZ.pow(2))

            binding.tvgXaxis.text = "X axis : " + String.format("%.2f", gAccX)
            binding.tvgYaxis.text = "Y axis : " + String.format("%.2f", gAccY)
            binding.tvgZaxis.text = "Z axis : " + String.format("%.2f", gAccZ)
            binding.tvgTotal.text = "Total Gravity : " + String.format("%.2f", gTotal) + "m/s\u00B2"

            binding.tvXaxis.text = "X axis : " + String.format("%.2f", accX)
            binding.tvYaxis.text = "Y axis : " + String.format("%.2f", accY)
            binding.tvZaxis.text = "Z axis : " + String.format("%.2f", accZ)
            binding.tvTotal.text = "Total Gravity axis : " + String.format("%.2f", total) + "m/s\u00B2"
        }

        if (event?.sensor == linearAcclerSensor) {
            val lAccX = event.values[0]
            val lAccY = event.values[1]
            val lAccZ = event.values[2]

            val lTotal = sqrt(lAccX.pow(2) + lAccY.pow(2) + lAccZ.pow(2))

            binding.tvlXaxis.text = "X axis : " + String.format("%.2f", lAccX)
            binding.tvlYaxis.text = "Y axis : " + String.format("%.2f", lAccY)
            binding.tvlZaxis.text = "Z axis : " + String.format("%.2f", lAccZ)
            binding.tvlTotal.text = "Total Gravity axis : " + String.format("%.2f", lTotal) + "m/s\u00B2"
        }
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, accelerSensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, linearAcclerSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}