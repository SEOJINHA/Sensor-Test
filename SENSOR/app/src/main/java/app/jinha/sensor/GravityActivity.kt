package app.jinha.sensor

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.jinha.sensor.databinding.ActivityGravityBinding
import app.jinha.sensor.databinding.ActivityGyroscopeBinding
import kotlin.math.pow
import kotlin.math.sqrt

/*
* 참고 : https://copycoding.tistory.com/13?category=1013954
* */
class GravityActivity : AppCompatActivity(), SensorEventListener {
    lateinit var binding: ActivityGravityBinding
    lateinit var sensorManager : SensorManager
    lateinit var gravitySensor : Sensor
    lateinit var mContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGravityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@GravityActivity

        sensorManager = mContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor == gravitySensor)
        {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val total = sqrt(x.pow(2) + y.pow(2) + z.pow(2))

            binding.tvXaxis.text = "X axis : " + String.format("%.2f", x)
            binding.tvYaxis.text = "Y axis : " + String.format("%.2f", y)
            binding.tvZaxis.text = "Z axis : " + String.format("%.2f", z)
            binding.tvGravity.text = "Total Gravity : " + String.format("%.2f", total) + "m/s\u00B2"
        }
    }
}