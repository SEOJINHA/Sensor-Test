package app.jinha.sensor

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Surface
import android.view.WindowManager
import app.jinha.sensor.databinding.ActivityGyroscopeBinding

/*
* 참고 : https://copycoding.tistory.com/10?category=1013954
* */
class GyroscopeActivity : AppCompatActivity(), SensorEventListener {
    lateinit var binding: ActivityGyroscopeBinding
    lateinit var sensorManager : SensorManager
    lateinit var gyroSensor : Sensor
    lateinit var mContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGyroscopeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@GyroscopeActivity
        sensorManager = mContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
    }

    @SuppressLint("SetTextI18n")
    fun getSurfaceRotation(){
        val windowService = Context.WINDOW_SERVICE
        val windowManager = mContext.getSystemService(windowService) as WindowManager
        val display = windowManager.defaultDisplay
        val rotation = display.rotation

        var xAxis = SensorManager.AXIS_X
        var yAxis = SensorManager.AXIS_Y
        val zAxis = SensorManager.AXIS_Z

        when (rotation) {
            Surface.ROTATION_0 -> binding.tvRotation.text = "Rotation : 0"
            Surface.ROTATION_90 -> {
                xAxis = SensorManager.AXIS_Y
                yAxis = SensorManager.AXIS_MINUS_X
                binding.tvRotation.text = "Rotation : 90"
            }
            Surface.ROTATION_180 -> {
                yAxis = SensorManager.AXIS_MINUS_Y
                binding.tvRotation.text = "Rotation : 180"
            }
            Surface.ROTATION_270 -> {
                xAxis = SensorManager.AXIS_MINUS_Y
                yAxis = SensorManager.AXIS_X
                binding.tvRotation.text = "Rotation : 270"
            }
        }

        binding.tvXaxis.text = "X axis : $xAxis"
        binding.tvYaxis.text = "Y axis : $yAxis"
        binding.tvZaxis.text = "Z axis : $zAxis"
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor == gyroSensor){
            getSurfaceRotation()
        }
    }
}