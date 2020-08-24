package app.jinha.sensor

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.jinha.sensor.databinding.ActivityPressureBinding

/*
 * 참고 - https://stuff.mit.edu/afs/sipb/project/android/docs/guide/topics/sensors/sensors_environment.html
 * */
class PressureActivity : AppCompatActivity(), SensorEventListener {
    lateinit var binding: ActivityPressureBinding
    lateinit var sensorManager: SensorManager
    lateinit var pressure : Sensor

    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPressureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@PressureActivity

        sensorManager = mContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, pressure, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        val millibarsOfPressure = event?.values?.get(0)

        binding.tvPressure.text = "$millibarsOfPressure hPa"
    }
}