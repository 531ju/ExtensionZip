# 값 변환

[문서 목록](README.md) · 소스: [IntExtension](../../extension/src/main/java/com/kjj/extension/value/IntExtension.kt), [FloatExtension](../../extension/src/main/java/com/kjj/extension/value/FloatExtension.kt), [ByteArrayExtension](../../extension/src/main/java/com/kjj/extension/value/ByteArrayExtension.kt)

## 왜 사용하나요?

화면 크기 단위, 시간 표시, 색상 투명도, 바이너리 로그 변환을 반복 작성하지 않도록 합니다.

| API | 입력·반환값 | 사용 상황·주의사항 |
| --- | --- | --- |
| `Int.dpToPx()` | dp 정수 → px `Float` | 픽셀 기반 View API에 전달. 시스템 displayMetrics 밀도 사용 |
| `Float.dpToPx()` | dp 실수 → px `Float` | 소수 dp 변환. Compose의 `Dp`를 반환하지 않음 |
| `Int.splitHourMin()` | 총 분 → `Pair<Int, Int>` | 첫 값은 시간, 둘째는 나머지 분. 음수 입력 정규화 없음 |
| `Int.setAlpha(alpha)` | 색상 Int + 0~1 비율 → 새 색상 Int | 비율을 소수 둘째 자리로 반올림 후 알파로 변환. 기존 알파를 대체하며 범위를 자동 보정하지 않음 |
| `ByteArray.convertBytesToHex()` | 바이트 배열 → `String` | 소문자 hex, 바이트마다 `0x` 접두사, 구분 공백 없음 |
| `ByteArray.convertToBytesToString()` | 바이트 배열 → `String` | 바이트마다 `0x`와 뒤쪽 공백. 마지막에도 공백이 붙음 |

| `ByteArray.crc32()` | 바이트 배열 → unsigned `Long` | CRC-32/ISO-HDLC 체크섬 검증 |

## 사용법

```kotlin
import android.graphics.Color
import com.kjj.extension.value.convertBytesToHex
import com.kjj.extension.value.convertToBytesToString
import com.kjj.extension.value.dpToPx
import com.kjj.extension.value.setAlpha
import com.kjj.extension.value.splitHourMin

val paddingPx: Int = 16.dpToPx().toInt()
val fractionalPx: Float = 12.5f.dpToPx()
val (hours, minutes) = 135.splitHourMin() // 2, 15
val translucentRed: Int = Color.RED.setAlpha(0.5f)

val packet = byteArrayOf(0x01, 0x0a)
val compact = packet.convertBytesToHex()       // "0x010x0a"
val spaced = packet.convertToBytesToString()  // "0x01 0x0a "
```

px 값은 기기 밀도에 따라 달라집니다. `setAlpha`는 0~1 범위로 호출하세요. 범위를 벗어나면 최종 알파 검증에서 예외가 발생할 수 있습니다. ByteArray 함수는 로그 표시용이며 역변환은 아래의 엄격한 로그 파서를 사용합니다. 빈 배열은 빈 문자열을 반환합니다.

## String.parseByteHexLog(): 로그를 바이트로 복원

패킷 로그를 재현하거나 저장한 로그를 검증할 때 사용합니다. 기존 두 ByteArray 로그 함수의 출력 형식을 모두 읽으며 기존 출력 함수는 변경하지 않습니다.

- 시그니처: `fun String.parseByteHexLog(): ByteArray`
- 사전 조건: 위 모듈 의존성과 `com.kjj.extension.value.parseByteHexLog` import. 모듈은 Kotlin 2/JVM 11, 최소 Android SDK 30을 사용합니다. 함수 자체에는 Android 런타임이나 권한이 필요하지 않습니다.
- 매개변수: 별도 인자 없음. 수신 문자열이 파싱 대상입니다.
- 반환값: 입력 순서대로 복원한 새 `ByteArray`. 빈 문자열 또는 허용 공백만 있는 문자열은 빈 배열입니다.
- 문법: 각 토큰은 `0x` 또는 `0X`와 정확히 두 ASCII 16진수 문자(`0-9a-fA-F`)입니다. 토큰은 붙이거나 ASCII 공백, 탭, CR, LF로 구분할 수 있고 앞뒤에도 이 공백을 허용합니다.
- 제한: 접두사 없는 hex, 추가 자릿수, 불완전한 토큰, 쉼표, Unicode 공백·숫자는 `IllegalArgumentException`을 발생시킵니다. 자동 보정이나 부분 결과 반환은 없습니다. `80-ff`는 Kotlin의 음수 Byte로 표현됩니다.

```kotlin
import com.kjj.extension.value.parseByteHexLog

val packet = byteArrayOf(0x01, 0x0a)
check("0x010x0a".parseByteHexLog().contentEquals(packet))
check("0x01 0x0a ".parseByteHexLog().contentEquals(packet))
```

위 두 입력과 결과를 검증하는 단위 테스트를 포함합니다. 현재 오프라인 빌드에서는 AGP 8.7.2 플러그인 캐시가 없어 테스트 실행은 검증하지 못했습니다. 모든 256개 바이트에 대해 기존 두 출력 함수와의 왕복 테스트도 포함합니다.

## ByteArray.crc32(): 캡처 패킷·픽스처 체크섬 검증

저장한 패킷이나 테스트 픽스처가 기대한 바이트인지 CRC-32/ISO-HDLC 체크섬으로 비교할 때 사용합니다. 모든 BLE 프로토콜에 통용되는 체크섬을 의미하지 않습니다.

- 시그니처: `fun ByteArray.crc32(): Long`
- 사전 조건: `implementation(project(":extension"))` 및 아래 import. 모듈은 Kotlin 2/JVM 11, 최소 Android SDK 30을 사용합니다. 함수 자체는 플랫폼 `java.util.zip.CRC32`만 사용하며 Android 권한이 필요하지 않습니다.
- 매개변수: 별도 인자 없이 수신 `ByteArray` 전체를 계산합니다.
- 반환값: unsigned CRC를 담은 `Long` (`0L..0xFFFFFFFFL`). 빈 배열은 `0L`입니다.
- 입력을 변경하지 않으며 호출마다 새 CRC32 인스턴스를 사용하여 호출 간 상태를 공유하지 않습니다.

```kotlin
import com.kjj.extension.value.crc32
import com.kjj.extension.value.parseByteHexLog

check("123456789".toByteArray(Charsets.US_ASCII).crc32() == 0xCBF43926L)
val packet = "0x68 0x65 0x6c 0x6c 0x6f ".parseByteHexLog()
check(packet.crc32() == 0x3610A686L)
check(byteArrayOf().crc32() == 0L)
```

예제 입력과 기대값은 `ByteArrayExtensionTest`에 포함합니다. 테스트 실행 여부는 PR 검증 결과를 참고하세요. CRC32C나 암호학적 해시가 아니며 인증·보안 검증용이 아닙니다. 스트리밍/증분 계산 또는 사용자 지정 다항식은 지원하지 않습니다. 계산 중 다른 스레드에서 입력 배열을 변경하는 사용은 지원하지 않습니다. 기존 로그 직렬화 출력은 변경하지 않습니다.
