package app.jinha.sensor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.jinha.sensor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var mContext : Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@MainActivity

        // 센서 종류 확인
        binding.buttonList.setOnClickListener {
            startActivity(Intent(mContext, SensorListActivity::class.java))
        }

        // 가속도
        binding.buttonAcc.setOnClickListener {
            startActivity(Intent(mContext, AccelerometerActivity::class.java))
        }

        // 근접
        binding.buttonPro.setOnClickListener {
            startActivity(Intent(mContext, ProximityActivity::class.java))
        }

        // 걸음 감지
        binding.buttonStepDetector.setOnClickListener {
            startActivity(Intent(mContext, StepDetectorActivity::class.java))
        }

        // 걸음수 감지
        binding.buttonStepCount.setOnClickListener {
            startActivity(Intent(mContext, StepCounterActivity::class.java))
        }

        // 광
        binding.buttonLight.setOnClickListener {
            startActivity(Intent(mContext, LightActivity::class.java))
        }

        // 회전
        binding.buttonGyroscope.setOnClickListener {
            startActivity(Intent(mContext, GyroscopeActivity::class.java))
        }

        // 자기장 센서 / 금속 탐지기
        binding.buttonMagneticField.setOnClickListener {
            startActivity(Intent(mContext, MagneticFieldActivity::class.java))
        }

        // 중력
        binding.buttonGravity.setOnClickListener {
            startActivity(Intent(mContext, GravityActivity::class.java))
        }

        // 진동
        binding.buttonVibrator.setOnClickListener {
            startActivity(Intent(mContext, VibratorActivity::class.java))
        }

        // GPS 거리 측정
        binding.buttonGps.setOnClickListener {
            startActivity(Intent(mContext, GPSActivity::class.java))
        }

        // 기압
        binding.buttonPressure.setOnClickListener {
            startActivity(Intent(mContext, PressureActivity::class.java))
        }
    }
}