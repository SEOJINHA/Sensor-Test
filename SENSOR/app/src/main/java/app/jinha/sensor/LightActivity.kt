package app.jinha.sensor

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import app.jinha.sensor.databinding.ActivityLightBinding

/*
* 참고 : https://copycoding.tistory.com/9?category=1013954
* */
class LightActivity : AppCompatActivity(), SensorEventListener {
    lateinit var binding: ActivityLightBinding
    lateinit var sensorManager: SensorManager
    var lightSensor : Sensor? = null

    lateinit var mContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLightBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@LightActivity
        sensorManager = mContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (lightSensor == null){
            Toast.makeText(mContext, "No Light Sensor Found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor == lightSensor){
            binding.tvLight.text = "Light : ${event?.values?.get(0)}"
        }
    }
}