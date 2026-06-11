package com.neorec.demo

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    companion object {
        // 你的设备
        const val TARGET_MAC = "B4:35:22:40:89:F6"
    }

    private val bt get() =
        (getSystemService(BLUETOOTH_SERVICE) as BluetoothManager?)?.adapter

    private lateinit var log: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        log = findViewById(R.id.log)
        val btn = findViewById<Button>(R.id.btn_connect)

        if (Build.VERSION.SDK_INT >= 31) {
            val perms = arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            requestPermissions(perms, 1)
        } else {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        btn.setOnClickListener {
            connect()
        }
    }

    private fun connect() {
        val adapter = bt ?: run {
            log("No BT adapter")
            return
        }
        if (!adapter.isEnabled) {
            log("BT not enabled")
            return
        }

        try {
            val dev: BluetoothDevice? =
                if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED)
                    adapter.getRemoteDevice(TARGET_MAC)
                else
                    @Suppress("DEPRECATION")
                    adapter.getRemoteDevice(TARGET_MAC)

            if (dev == null) { log("Device null"); return }

            // 真正连接由 BleWorker 负责；这里先只显示准备就绪
            log("Ready to GATT.connect → $TARGET_MAC")

            BleWorker.connect(this, dev, log)

        } catch (e: Exception) {
            log("ERR: ${e.message}")
        }
    }

    fun log(msg: String) {
        runOnUiThread {
            log.append("\n$msg")
        }
    }
}
