# Activity·Fragment·Navigation

[문서 목록](README.md) · 소스: [ActivityExtension](../../extension/src/main/java/com/kjj/extension/component/ActivityExtension.kt), [FragmentExtension](../../extension/src/main/java/com/kjj/extension/component/FragmentExtension.kt), [NavigationExtension](../../extension/src/main/java/com/kjj/extension/component/NavigationExtension.kt)

## 왜 사용하나요?

화면별 시스템 바 색상, Material 테마 색상, 키보드·시스템 영역 패딩과 백스택 확인을 재사용합니다.

| API | 매개변수·반환값 | 호출 조건·특성 |
| --- | --- | --- |
| `Activity.setStatusBarColor(color)` | 실제 색상 Int → Unit | 리소스 ID가 아닌 ColorInt |
| `Activity.setNavigationBarColor(color)` | 실제 색상 Int → Unit | 리소스 ID가 아닌 ColorInt |
| `Activity.isDarkMode()`, `Fragment.isDarkMode()` | Boolean | 현재 uiMode의 night 플래그 |
| `Activity.getMaterialColor(color)` | 테마 attr 리소스 ID → 색상 Int | Material 속성이 테마에서 해석 가능해야 함 |
| `Context.getNavigationBarHeight()` | 픽셀 Int | Android 내부 dimen 리소스 조회, 없으면 0. 현재 보이는 Insets와 다를 수 있음 |
| `Fragment.setStatusBarColorRes(color)` | color 리소스 ID → Unit | 붙어 있는 Activity에 위임 |
| `Fragment.setNavigationBarColorRes(color)` | color 리소스 ID → Unit | 붙어 있는 Activity에 위임 |
| `Fragment.getMaterialColor(colorRes)` | 이름과 달리 attr ID → 색상 Int | requireView 사용: View 생성 후 호출 |
| `Fragment.setupDefaultWindowInsets(top, bottom, includeKeyboardInsets)` | nullable px Int 두 개, Boolean → Unit | top/bottom 기본 null, 키보드 포함 기본 true. null이면 Insets, 숫자면 해당 패딩으로 덮어씀 |
| `NavController.isFragmentInBackStack(destinationId)` | destination Int → Boolean | getBackStackEntry 성공 여부. 실패 예외는 출력 후 false |
| `NavController.isFragmentInBackStack(route)` | route String → Boolean | 같은 방식의 route 오버로드. 이름과 달리 Navigation 목적지 항목 조회 |

## Activity 색상 설정

Activity 내부에서 호출합니다.

```kotlin
import com.kjj.extension.component.getMaterialColor
import com.kjj.extension.component.isDarkMode
import com.kjj.extension.component.setStatusBarColor

val surface = getMaterialColor(com.google.android.material.R.attr.colorSurface)
setStatusBarColor(surface)
val dark = isDarkMode()
```

Android 14 이하는 Window의 barColor를 변경합니다. Android 15 이상 구현은 decorView에 Insets listener를 설정해 배경과 패딩을 바꿉니다. `setStatusBarColor`와 `setNavigationBarColor`를 연달아 호출하면 동일 View의 listener를 서로 대체하고, 각각 설정하는 패딩도 덮어씁니다. 기존 Insets 처리와 충돌할 수 있으므로 두 호출이 독립적으로 합성된다고 가정하지 마세요.

## Fragment Insets·색상

아래 코드는 Fragment의 `onViewCreated`에서 호출합니다.

```kotlin
import com.kjj.extension.component.getMaterialColor
import com.kjj.extension.component.setStatusBarColorRes
import com.kjj.extension.component.setupDefaultWindowInsets

setStatusBarColorRes(android.R.color.black)
val primary = getMaterialColor(com.google.android.material.R.attr.colorPrimary)
setupDefaultWindowInsets(includeKeyboardInsets = true)
// 상단 패딩을 0px로 고정하려면:
// setupDefaultWindowInsets(top = 0, includeKeyboardInsets = true)
```

이 함수는 루트 View의 위·아래 패딩을 변경하며 좌·우 패딩은 유지합니다. 시스템 바·디스플레이 컷아웃과 선택적으로 IME Insets를 합쳐 사용합니다. 이미 설치한 Insets listener를 대체할 수 있습니다. `top`과 `bottom` 숫자는 dp가 아닌 px입니다. Fragment가 분리되었거나 View가 없으면 requireActivity/requireView가 예외를 던질 수 있습니다.

## 백스택 확인

```kotlin
import androidx.navigation.NavController
import com.kjj.extension.component.isFragmentInBackStack

fun hasPreviousScreen(navController: NavController, destinationId: Int, route: String) {
    val byId = navController.isFragmentInBackStack(destinationId)
    val byRoute = navController.isFragmentInBackStack(route)
}
```

호출 앱이 실제 그래프의 ID·route를 전달해야 합니다. 이 함수는 해당 화면으로 이동하거나 pop하지 않습니다.
