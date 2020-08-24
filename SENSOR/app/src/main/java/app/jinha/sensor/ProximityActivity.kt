package app.jinha.sensor

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import app.jinha.sensor.databinding.ActivityProximityBinding

/*
* 참고 : https://copycoding.tistory.com/3?category=1013954
* */
class ProximityActivity : AppCompatActivity(), SensorEventListener {
    lateinit var binding: ActivityProximityBinding

    lateinit var sensorManager: SensorManager
    var proximitySensor: Sensor? = null

    lateinit var mContext : Context

    val TAG = "ProximityActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProximityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@ProximityActivity

        sensorManager = mContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (proximitySensor == null){
            Toast.makeText(mContext, "No Proximity Sensor Found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        when (accuracy){
            SensorManager.SENSOR_STATUS_UNRELIABLE ->
                Toast.makeText(mContext, "UNRELIABLE", Toast.LENGTH_SHORT).show()
            SensorManager.SENSOR_STATUS_ACCURACY_LOW ->
                Toast.makeText(mContext, "LOW", Toast.LENGTH_SHORT).show()
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM ->
                Toast.makeText(mContext, "MEDIUM", Toast.LENGTH_SHORT).show()
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH ->
                Toast.makeText(mContext, "HIGH", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor == proximitySensor) {
            Log.e(TAG, "Proximity Sensor Value = " + (event?.values?.get(0) ?: -1))

            if (event?.values?.get(0) ?: -1 == 0.toFloat()){
                binding.tvProximity.text = "Near : " + event?.values?.get(0).toString()
            }
            else{
                binding.tvProximity.text = "Far : " + event?.values?.get(0).toString()
            }
        }
    }
}