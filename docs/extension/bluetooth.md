# Bluetooth 확장 함수

[문서 목록](README.md) · [BleExtension 소스](../../extension/src/main/java/com/kjj/extension/BleExtension.kt)

## 왜 사용하나요?

본딩·연결 조회, 서비스 전체에서 Characteristic/Descriptor 탐색, CCCD 값 확인 코드를 줄입니다. 단, 현재 구현의 이름과 실제 동작이 다른 부분 및 속성 검사 결함을 확인한 뒤 사용해야 합니다.

## 권한과 호출 순서

Android 12(API 31) 이상에서는 필요한 Bluetooth 기능의 런타임 권한을 먼저 요청하고 허용 여부를 확인하세요. 연결·본딩 조회에는 `BLUETOOTH_CONNECT`가 필요하며, 스캔 권한은 별도입니다. API 30에서는 해당 OS의 Bluetooth 권한을 구성해야 합니다. 함수의 annotation이 권한 요청을 대신하지 않습니다.

GATT 탐색은 연결 후 `discoverServices()`를 요청하고 서비스 탐색 성공 콜백을 받은 다음 호출합니다. `BluetoothGatt`의 연결 해제·close는 호출 앱이 관리합니다.

## 연결·본딩 API

| API | 입력·반환값 | 실제 동작·주의사항 |
| --- | --- | --- |
| `BluetoothManager.exitBondedDevice(macAddress)` | MAC 문자열 → Boolean | 이름과 달리 본딩 해제 함수가 아님. GATT 연결 장치 목록의 주소와 비교. adapter가 없으면 false |
| `BluetoothAdapter.getBondedList()` | `Set<BluetoothDevice>?` | 본딩된 장치 목록. 예외를 출력하고 null 반환 |
| `BluetoothAdapter.isBondedByAddress(macAddress)` | 문자열 → Boolean | 주소의 소문자·대문자 표현에 부분 문자열 포함 검사. 엄격한 주소 동등성 비교가 아님 |
| `BluetoothAdapter.isBondedBySsid(ssid)` | 문자열 → Boolean | 장치 이름/SSID가 아니라 주소에 해당 문자열이 포함되는지 검사 |
| `BluetoothDevice.removeBond()` | Boolean | 숨김 removeBond 리플렉션 호출. 예외 시 false. true여도 내부 반환값·최종 해제 성공을 검증하지 않음 |

```kotlin
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import com.kjj.extension.exitBondedDevice
import com.kjj.extension.getBondedList
import com.kjj.extension.isBondedByAddress

// 호출 전에 해당 OS에서 필요한 권한을 실제로 허용받아야 합니다.
@SuppressLint("MissingPermission")
fun inspectBonding(manager: BluetoothManager, address: String) {
    val adapter = manager.adapter ?: return
    val bonded = adapter.getBondedList() // 실패 시 null
    val matchesBondedAddress = adapter.isBondedByAddress(address)
    val gattConnected = manager.exitBondedDevice(address)
}
```

권한 누락을 자동으로 해결하지 않으며, 모든 함수가 예외를 잡는 것도 아닙니다. 숨김 API 사용은 기기·OS 정책에 따라 차단될 수 있습니다.

## GATT·Descriptor API

| API | 입력·반환값 | 동작 |
| --- | --- | --- |
| `BluetoothGatt.findCharacteristic(uuid)` | UUID → Characteristic? | 전체 서비스에서 첫 UUID 일치 항목. 없으면 null |
| `BluetoothGatt.findDescriptor(uuid)` | UUID → Descriptor? | 전체 characteristic에서 첫 UUID 일치 항목. 없으면 null |
| `BluetoothGattDescriptor.character(type)` | ByteArray → Boolean | 현재 로컬 value와 배열 내용 비교 |
| `BluetoothGattDescriptor.isEnabled()` | Boolean | 현재 로컬 value가 notification 또는 indication 활성화 값과 일치하는지 확인 |
| `BluetoothGatt.refresh()` | Boolean | 숨김 refresh 리플렉션의 Boolean 결과. 예외 시 false. 서비스 탐색 완료를 보장하지 않음 |

```kotlin
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import com.kjj.extension.character
import com.kjj.extension.findCharacteristic
import com.kjj.extension.findDescriptor
import com.kjj.extension.isEnabled
import java.util.UUID

fun inspectDiscoveredServices(gatt: BluetoothGatt, characteristicUuid: UUID) {
    val characteristic = gatt.findCharacteristic(characteristicUuid) ?: return
    val cccdUuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    val descriptor = gatt.findDescriptor(cccdUuid)
    val locallyEnabled = descriptor?.isEnabled() ?: false
    val notificationValue =
        descriptor?.character(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) ?: false
}
```

CCCD UUID는 여러 characteristic에서 중복됩니다. 특정 characteristic의 CCCD가 필요하면 찾은 characteristic의 `getDescriptor(cccdUuid)`를 사용하세요. Descriptor 확인 함수는 원격 read를 수행하지 않으며 최신 원격 상태를 보장하지 않습니다. 값을 먼저 읽거나 앱에서 적절히 갱신해야 합니다. notification 구독 설정·원격 write도 대신하지 않습니다.

## Characteristic 속성 검사: 현재 구현 주의

| API | 의도된 검사 |
| --- | --- |
| `containsProperty(property)` | 임의 property 비트마스크 |
| `isReadable()` | PROPERTY_READ |
| `isWritable()` | PROPERTY_WRITE |
| `isWritableWithoutResponse()` | PROPERTY_WRITE_NO_RESPONSE |
| `isIndicatable()` | PROPERTY_INDICATE |
| `isNotifiable()` | PROPERTY_NOTIFY |

이 함수들은 Boolean을 반환하지만 현재 `containsProperty`가 `properties and property` 대신 `property and property`를 계산합니다. 따라서 0이 아닌 마스크는 실제 지원 여부와 무관하게 true가 되고 위의 편의 함수들도 true가 됩니다. 지원 여부 판단에는 다음처럼 Android 속성을 직접 검사하세요.

```kotlin
import android.bluetooth.BluetoothGattCharacteristic

fun canRead(characteristic: BluetoothGattCharacteristic): Boolean =
    (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_READ) != 0
```

문서에서 동작을 분명히 설명하기 위한 대체 예제입니다. 이번 문서 PR은 속성 검사나 숨김 API 구현을 수정하지 않습니다.
