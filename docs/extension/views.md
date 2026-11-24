# View·Snackbar·애니메이션

[문서 목록](README.md) · 소스: [ViewExtension](../../extension/src/main/java/com/kjj/extension/view/ViewExtension.kt), [ViewAnimationExtension](../../extension/src/main/java/com/kjj/extension/view/ViewAnimationExtension.kt), [SnackbarExtension](../../extension/src/main/java/com/kjj/extension/component/SnackbarExtension.kt)

## 왜 사용하나요?

연속 탭으로 인한 중복 동작, 반복적인 Snackbar 표시, View 표시·크기·색상 전환 코드를 줄입니다. Android View가 생성된 뒤 메인 스레드에서 호출하세요.

| API | 매개변수·반환값 | 동작·제약 |
| --- | --- | --- |
| `View.onSingleClick(action)` | 콜백 → Unit | 마지막 허용 클릭 이후 400ms 초과일 때 실행. 기존 클릭 listener 대체 |
| `View.getMaterialColor(colorRes)` | attr ID → 색상 Int | 매개변수 이름과 달리 color 리소스가 아닌 테마 속성 |
| `ViewGroup.findEditChild()` | 첫 EditText? | 중첩 ViewGroup 처리에 잘못된 자기 재귀가 있어 무한 재귀 위험 |
| `View.showSnackBar(msg, dismissToSwipe)` | 문자열, Boolean 기본 true → Unit | LENGTH_SHORT, 최대 10줄. true면 자체 가로 스와이프 처리 추가 |
| `Snackbar.swipeOut(delaX)` | 가로 이동량 Float → Unit | 부호에 따른 방향으로 이동·투명도 250ms 전환 후 dismiss |
| `Snackbar.swipeIn(deltaX)` | Float → Unit | 위치 0, alpha 1로 250ms 복귀. 현재 구현에서 유한 deltaX의 크기는 동작을 바꾸지 않음 |
| `View.fadeOut(animationEndListener)` | nullable 콜백 기본 null → Unit | alpha 1 → 0, 종료 시 alpha가 0이면 GONE |
| `View.fadeIn(animationEndListener)` | nullable 콜백 기본 null → Unit | 먼저 VISIBLE·alpha 0, 이후 alpha 1 |
| `View.expand(duration)` | 밀리초 Long 기본 100 → Unit | 부모 너비로 측정, 높이 0에서 목표 높이까지 펼치고 마지막 WRAP_CONTENT |
| `View.collapse(duration)` | 밀리초 Long 기본 100 → Unit | 현재 measuredHeight를 줄인 뒤 GONE |
| `View.getCurrentBackgroundColor()` | 색상 Int? | 배경이 ColorDrawable이면 색상, 그 외 null |
| `View.changeColorAnimation(animDuration, endColor)` | 밀리초 Long, color 리소스 ID → Unit | 현재 단색 배경이면 색상 전환, 아니면 즉시 배경색 설정 |

## 클릭과 메시지

화면의 `binding`이 유효한 시점에 호출합니다.

```kotlin
import com.kjj.extension.view.onSingleClick
import com.kjj.extension.view.showSnackBar

binding.saveButton.onSingleClick {
    binding.root.showSnackBar("저장을 요청했습니다.")
}
binding.root.showSnackBar("안내 메시지", dismissToSwipe = false)
```

400ms 간격 제한은 네트워크 요청 완료까지 잠그는 기능이 아닙니다. 요청 중 중복 방지는 별도 상태로 처리해야 합니다. `showSnackBar`는 Snackbar 객체를 반환하지 않습니다. true인 경우 너비 절반 초과 이동 또는 빠른 가로 이동으로 dismiss하며, false는 이 함수의 커스텀 터치 처리를 추가하지 않는다는 뜻입니다.

Snackbar 객체를 직접 관리할 때:

```kotlin
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.kjj.extension.component.swipeIn
import com.kjj.extension.component.swipeOut

fun restoreSnackbar(snackbar: Snackbar) {
    snackbar.swipeIn(0f)
}
fun dismissSnackbarToRight(snackbar: Snackbar) {
    snackbar.swipeOut(1f) // 오른쪽 방향. 왼쪽은 음수
}
```

Snackbar를 표시하고 레이아웃이 끝나 너비가 정해진 뒤 사용하세요. 이 함수들은 자체적으로 제스처를 감지하지 않습니다.

## 애니메이션

```kotlin
import android.view.View
import com.kjj.extension.view.changeColorAnimation
import com.kjj.extension.view.collapse
import com.kjj.extension.view.expand
import com.kjj.extension.view.fadeIn
import com.kjj.extension.view.fadeOut
import com.kjj.extension.view.getCurrentBackgroundColor

fun showPanel(panel: View) {
    panel.fadeIn()
}
fun hidePanel(panel: View, onHidden: () -> Unit) {
    panel.fadeOut { onHidden() }
}
fun expandPanel(panel: View) {
    panel.expand(duration = 200L)
}
fun collapsePanel(panel: View) {
    panel.collapse(duration = 200L)
}
fun recolorPanel(panel: View) {
    val current: Int? = panel.getCurrentBackgroundColor()
    panel.changeColorAnimation(200L, android.R.color.holo_blue_light)
}
```

- fade 시간은 시스템의 `config_mediumAnimTime`입니다. ViewPropertyAnimator의 기존 listener·애니메이션과 간섭할 수 있습니다.
- expand는 부모가 View여야 하며 부모 너비가 결정된 뒤 호출해야 합니다. collapse도 측정 이후에 사용하세요.
- `changeColorAnimation`의 `endColor`에는 `Color.BLUE` 같은 실제 색상 Int가 아닌 color 리소스 ID를 전달합니다. 내부의 시작/끝 동일성 비교는 색상 Int와 리소스 ID를 비교하므로 동일 색상 애니메이션이 생략된다고 보장하지 않습니다.
- 함수들은 Animator/Job 핸들을 반환하지 않습니다. 중첩 실행·취소 정책은 호출 측에서 관리하세요.

## 입력 뷰 검색과 Material 색상

```kotlin
import android.view.View
import com.kjj.extension.view.getMaterialColor

fun resolvePrimary(view: View): Int =
    view.getMaterialColor(com.google.android.material.R.attr.colorPrimary)
```

`findEditChild`는 입력 뷰를 탐색하려는 함수이며, 정상 반환 시 EditText 또는 null입니다. 하지만 중첩 ViewGroup을 만나면 자식 대신 같은 부모를 재귀 호출하고 반환값도 전달하지 않습니다. 현재는 평면 구조에만 한정하고, 일반적인 중첩 레이아웃에서는 ViewBinding 또는 `findViewById`를 사용하세요.
