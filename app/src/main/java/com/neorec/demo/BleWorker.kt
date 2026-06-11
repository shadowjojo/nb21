package com.neorec.demo

import android.bluetooth.*
import android.os.Build
import android.text.TextUtils
import android.widget.TextView
import androidx.annotation.RequiresPermission
import java.util.*

object BleWorker {

    private var gatt: BluetoothGatt? = null

    @RequiresPermission(allOf = [android.Manifest.permission.BLUETOOTH_CONNECT])
    fun connect(activity: MainActivity, device: BluetoothDevice, log: TextView) {
        gatt?.close()
        device.connectGatt(activity, false, object : BluetoothGattCallback() {

            override fun onConnectionStateChange(g: BluetoothGatt, status: Int, newState: Int) {
                activity.log("STATE: status=$status newState=$newState")
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    activity.log("CONNECTED → discovering...")
                    g.discoverServices()
                }
                if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    activity.log("DISCONNECTED")
                }
            }

            override fun onServicesDiscovered(g: BluetoothGatt, status: Int) {
                activity.log("SERVICES status=$status")
                for (s in g.services) {
                    activity.log(" SVC: ${s.uuid}")
                    for (c in s.characteristics) {
                        activity.log("  CH : ${c.uuid}  props=${c.properties}")
                    }
                }
                // 下一步：enable Notify on 75851135... + write AcquisitionStart
            }

            override fun onCharacteristicChanged(g: BluetoothGatt, c: BluetoothGattCharacteristic) {
                activity.log("NOTIFY ${c.uuid} len=${c.value?.size}")
            }
        })
    }
}
