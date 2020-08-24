package app.jinha.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import app.jinha.sensor.databinding.ActivitySensorListBinding

/*
* 참고 : https://copycoding.tistory.com/2?category=1013954
* */
class SensorListActivity : AppCompatActivity() {
    lateinit var binding: ActivitySensorListBinding
    lateinit var sensorManager: SensorManager

    lateinit var mContext : Context
    var sSensorList = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySensorListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@SensorListActivity
        sensorManager = mContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val listSensor = sensorManager.getSensorList(Sensor.TYPE_ALL)

        for (list in listSensor){
            Log.e("SENSOR", "list : ${list.name}")
            sSensorList += ("=========================\n" +
                    "Name : ${list.name} \n" +
                    "Vendor : ${list.vendor} \n" +
                    "version : ${list.version} \n" +
                    "Power : ${list.power} \n" +
                    "Type : ${list.type} \n" +
                    "toString : ${list.toString()} \n\n")
        }
        binding.tvListSensor.text = sSensorList

        Log.e("SENSOR","SENSOR : $sSensorList")
        // 스크롤 가능하도록 설정
        binding.tvListSensor.movementMethod = ScrollingMovementMethod()
    }
}