package app.jinha.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import app.jinha.sensor.databinding.ActivityGyroscopeBinding
import app.jinha.sensor.databinding.ActivityVibratorBinding

/*
* 참고 : https://copycoding.tistory.com/23?category=1013954
* https://raon-studio.tistory.com/7
* */
class VibratorActivity : AppCompatActivity() {
    lateinit var binding: ActivityVibratorBinding
    lateinit var vibrator : Vibrator
    lateinit var mContext : Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVibratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@VibratorActivity

        vibrator = mContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val vLoop = longArrayOf(0, 500, 100, 500, 100, 500, 100)

        binding.buttonTime.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(150, -1))
            }
            else{
                vibrator.vibrate(150)
            }
        }

        binding.buttonPattern.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(vLoop, -1))
            }
            else{
                vibrator.vibrate(vLoop, -1)
            }
        }

        binding.buttonPatternRepeat.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(vLoop, 0))
            }
            else{
                vibrator.vibrate(vLoop, 0)
            }
        }

        binding.buttonFinish.setOnClickListener {
            vibrator.cancel()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action

        return super.onTouchEvent(event)
    }
}