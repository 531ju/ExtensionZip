package com.kjj.extensionzip.ext

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import androidx.annotation.RequiresPermission
import java.util.UUID
import kotlin.math.acos

@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun BluetoothManager.exitBondedDevice(macAddress : String) : Boolean {
    if(adapter == null) {
        return false
    }
    val device = getConnectedDevices(BluetoothProfile.GATT)
    var isExist = false
    device.forEach {
        if(it.address == macAddress.lowercase() || it.address == macAddress.uppercase()) {
            isExist = true
        }
    }
    return isExist
}

@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun BluetoothAdapter.getBondedList(): Set<BluetoothDevice>? {
    return try {
        bondedDevices
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun BluetoothAdapter.isBondedByAddress(macAddress: String): Boolean {
    var isExist = false
    bondedDevices?.forEach { device ->
        if (device.address?.contains(macAddress.lowercase())==true || device.address?.contains(macAddress.uppercase())==true) {
            isExist = true
        }
    }
    return isExist
}

@RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
fun BluetoothAdapter.isBondedBySsid(ssid: String): Boolean {
    var isExist = false
    bondedDevices?.forEach { device ->
        if (device.address?.contains(ssid) == true) {
            isExist = true
        }
    }
    return isExist
}

fun BluetoothDevice.removeBond(): Boolean {
    return try {
        this.javaClass.getMethod("removeBond").invoke(this)
        true

    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun BluetoothGatt.findCharacteristic(uuid: UUID): BluetoothGattCharacteristic? {
    services?.forEach { service ->
        service.characteristics?.firstOrNull { characteristic ->
            characteristic.uuid == uuid
        }?.let { matchingCharacteristic ->
            return matchingCharacteristic
        }
    }
    return null
}

fun BluetoothGatt.findDescriptor(uuid: UUID): BluetoothGattDescriptor? {
    services?.forEach { service ->
        service.characteristics.forEach { characteristic ->
            characteristic.descriptors?.firstOrNull { descriptor ->
                descriptor.uuid == uuid
            }?.let { matchingDescriptor ->
                return matchingDescriptor
            }
        }
    }
    return null
}

fun BluetoothGattCharacteristic.containsProperty(property: Int) = property and property != 0

fun BluetoothGattCharacteristic.isReadable(): Boolean =
    containsProperty(BluetoothGattCharacteristic.PROPERTY_READ)

fun BluetoothGattCharacteristic.isWritable(): Boolean =
    containsProperty(BluetoothGattCharacteristic.PROPERTY_WRITE)

fun BluetoothGattCharacteristic.isWritableWithoutResponse(): Boolean =
    containsProperty(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)

fun BluetoothGattCharacteristic.isIndicatable(): Boolean =
    containsProperty(BluetoothGattCharacteristic.PROPERTY_INDICATE)

fun BluetoothGattCharacteristic.isNotifiable(): Boolean =
    containsProperty(BluetoothGattCharacteristic.PROPERTY_NOTIFY)

fun BluetoothGattDescriptor.character(type: ByteArray): Boolean =
    value.contentEquals(type)

fun BluetoothGattDescriptor.isEnabled(): Boolean =
    character(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) || character(
        BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
    )

fun BluetoothGatt.refresh() : Boolean {
    return try {
        this.javaClass.getMethod("refresh").invoke(this) as Boolean
    } catch (e : Exception) {
        false
    }
}