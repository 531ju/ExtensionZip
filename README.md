# ExtensionZip

Android 개발에서 반복해서 사용하는 Kotlin 확장 함수와 공통 UI 기반 클래스를 모아 둔 프로젝트입니다. View 기반 UI와 Jetpack Compose용 코드를 모듈별로 관리합니다.

## 모듈 구성

| 모듈 | 설명 | 주요 구성 |
| --- | --- | --- |
| [`base`](base/src/main/java/com/kjj/base) | View 기반 공통 UI 클래스 | Dialog, Fragment, RecyclerView Adapter·ViewHolder, Paging Adapter |
| [`base-compose`](base-compose/src/main/java/com/kjj/base_compose) | Jetpack Compose용 공통 화면 | `BaseScreen`, `BaseBottomSheetDialogScreen` |
| [`extension`](extension/src/main/java/com/kjj/extension) | Android·Kotlin 확장 함수 | 컴포넌트, 값 변환, View, Bluetooth, Context, Lifecycle |
| [`app`](app) | Android 애플리케이션 모듈 | `MainActivity`, Compose 테마 |

`base`와 `base-compose`는 `extension` 모듈에 의존합니다.

## 주요 기능

### base

- **Dialog**: `BaseAlertDialog`, `BaseBottomSheetDialog`, `BaseFullScreenDialog`, `TopRoundBottomSheetDialog`
- **Fragment**: `BaseFragment`
- **RecyclerView**: `BaseAdapter`, `BasePagingAdapter`, `BaseAdapterModel`, `BaseViewHolder`, `EmptyViewHolder`

### base-compose

- **화면**: `BaseScreen`
- **바텀시트**: `BaseBottomSheetDialogScreen`

### extension

- **컴포넌트**: `ActivityExtension`, `FragmentExtension`, `NavigationExtension`, `SnackbarExtension`
- **값 처리**: `ByteArrayExtension`, `FloatExtension`, `IntExtension`
- **View**: `ViewAnimationExtension`, `ViewExtension`
- **기타**: `BleExtension`, `ContextExtension`, `LifecycleExtension`

## 개발 환경

저장소의 Gradle 설정 기준입니다.

| 항목 | 버전 |
| --- | --- |
| Kotlin | 2.0.0 |
| Android Gradle Plugin | 8.7.2 |
| Gradle Wrapper | 8.11.1 |
| Android compile SDK | 35 |
| Android 최소 SDK | 30 (Android 11) |
| Java·Kotlin 바이트코드 타깃 | 11 |

빌드 실행에는 JDK 17이 필요합니다. 바이트코드 타깃 11과 Gradle 실행에 사용하는 JDK 버전은 별개입니다.

## 시작하기

1. 저장소를 복제합니다.

   ```bash
   git clone https://github.com/531ju/ExtensionZip.git
   cd ExtensionZip
   ```

2. Android Studio에서 프로젝트를 열고 Gradle JDK를 17로 설정합니다.
3. Android SDK Platform 35를 준비하고, 로컬 SDK 경로를 Android Studio 또는 `local.properties`의 `sdk.dir`로 설정합니다. `local.properties`는 커밋하지 않습니다.
4. Gradle 동기화 후 필요한 모듈을 빌드합니다.

   ```bash
   # macOS / Linux
   ./gradlew :extension:assembleDebug :base:assembleDebug :base-compose:assembleDebug
   ```

   ```powershell
   # Windows
   .\gradlew.bat :extension:assembleDebug :base:assembleDebug :base-compose:assembleDebug
   ```

## 프로젝트 내부에서 모듈 사용

이 저장소 안의 Android 모듈에서 필요한 의존성을 `build.gradle.kts`에 추가합니다.

```kotlin
dependencies {
    implementation(project(":extension"))
    implementation(project(":base"))         // View 기반 공통 UI가 필요한 경우
    implementation(project(":base-compose")) // Compose 공통 화면이 필요한 경우
}
```

위 설정은 `settings.gradle.kts`에 포함된 로컬 모듈을 참조하는 방식입니다.

### 확장 함수 예제

```kotlin
import com.kjj.extension.value.dpToPx
import com.kjj.extension.value.splitHourMin

val paddingPx: Float = 16.dpToPx()
val (hour, minute) = 135.splitHourMin() // 2시간 15분
```

## 테스트

Android SDK와 JDK를 설정한 환경에서 JVM 단위 테스트를 실행할 수 있습니다.

```bash
./gradlew testDebugUnitTest
```

기기 테스트는 API 30 이상의 에뮬레이터 또는 Android 기기를 연결한 뒤 실행합니다.

```bash
./gradlew connectedDebugAndroidTest
```
