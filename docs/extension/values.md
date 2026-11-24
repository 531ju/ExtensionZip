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

px 값은 기기 밀도에 따라 달라집니다. `setAlpha`는 0~1 범위로 호출하세요. 범위를 벗어나면 최종 알파 검증에서 예외가 발생할 수 있습니다. ByteArray 함수는 로그 표시용이며 문자열을 역변환하는 파서는 제공하지 않습니다. 빈 배열은 빈 문자열을 반환합니다.
