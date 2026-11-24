# Context와 Lifecycle

[문서 목록](README.md) · 소스: [ContextExtension](../../extension/src/main/java/com/kjj/extension/ContextExtension.kt), [LifecycleExtension](../../extension/src/main/java/com/kjj/extension/LifecycleExtension.kt)

## 왜 사용하나요?

권한·화면 상태 확인과 화면이 활성화된 동안의 Flow 수집 코드를 공통화합니다. 상태 조회는 권한 요청이나 서비스 시작을 대신하지 않습니다.

| API | 입력·반환값 | 동작·제약 |
| --- | --- | --- |
| `Context.getScreenWidthDP()` | Context → 화면 너비 `Float` | 시스템 픽셀 너비 / 밀도. 현재 창·멀티윈도우의 실제 콘텐츠 너비와 다를 수 있음 |
| `isServiceRunning(context, serviceClass)` | Context, 서비스 Class → Boolean | `getRunningServices` 결과의 클래스 이름 비교. deprecated API이며 최신 Android에서 타 앱 서비스 조회 제한 |
| `isAppInForeground(context)` | Context → Boolean | 메인 패키지 이름과 같은 프로세스의 FOREGROUND 중요도 확인. 멀티프로세스·화면 가시성의 완전한 판정이 아님 |
| `Context.hasPermission(permissionType)` | 권한 문자열 → Boolean | 현재 부여 여부만 확인. 요청 UI를 띄우지 않음 |
| `LifecycleOwner.repeatOnStarted(block)` | suspend CoroutineScope 블록 → Unit | lifecycleScope에 작업 시작, STARTED 이상에서 반복 실행. 아래 상태로 내려가면 블록 취소, 재진입 시 다시 실행 |
| `Fragment.repeatOnStartedWithViewOwner(block)` | suspend CoroutineScope 블록 → Unit | 코루틴 scope는 viewLifecycleOwner이나 반복 상태 기준은 Fragment lifecycle. 이름과 다른 부분에 주의 |

## 권한·상태 조회

```kotlin
import android.Manifest
import android.content.Context
import com.kjj.extension.getScreenWidthDP
import com.kjj.extension.hasPermission
import com.kjj.extension.isAppInForeground
import com.kjj.extension.isServiceRunning

fun inspectContext(context: Context, ownServiceClass: Class<*>) {
    val widthDp = context.getScreenWidthDP()
    val cameraGranted = context.hasPermission(Manifest.permission.CAMERA)
    val foreground = isAppInForeground(context)
    val running = isServiceRunning(context, ownServiceClass)
    // 결과를 UI나 서비스 제어 정책에 사용합니다.
}
```

서비스의 실행 여부와 서비스에 안전하게 명령을 보낼 수 있는지는 별개입니다. 호출 직후 상태가 바뀔 수 있습니다.

## Fragment에서 Flow 수집

`Fragment.onViewCreated` 내부에서 호출하는 예입니다.

```kotlin
import com.kjj.extension.repeatOnStarted

viewLifecycleOwner.repeatOnStarted {
    viewModel.uiState.collect { state ->
        // 현재 View의 UI를 갱신합니다.
    }
}
```

`viewModel.uiState`는 앱이 제공하는 Flow입니다. 여러 Flow를 동시에 수집하려면 블록 안에서 각각 `launch { ... }`를 사용하세요. 순차적인 무한 collect 뒤의 코드는 실행되지 않습니다.

`repeatOnStartedWithViewOwner`를 사용하려면 `com.kjj.extension.repeatOnStartedWithViewOwner`를 import하고 View 생성 이후에 호출해야 합니다. 현재 구현의 혼합 lifecycle 기준 때문에 View UI 수집에는 위의 `viewLifecycleOwner.repeatOnStarted` 방식을 우선 사용하세요. 두 함수 모두 Job을 반환하지 않아 반환값으로 직접 취소할 수 없고, 반복 등록하면 별도 작업이 계속 생성됩니다.
