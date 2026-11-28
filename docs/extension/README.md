# extension 사용법

[프로젝트 README](../../README.md) · [모듈 소스](../../extension/src/main/java/com/kjj/extension)

Android·Kotlin 확장 함수를 **왜 사용하는지**, 입력·반환값, 호출 예제와 현재 구현의 주의사항을 정리합니다. 이 문서는 `extension` 모듈만 다루며 코드를 변경하지 않습니다.

## 시작하기

저장소 내부의 Android 모듈에서 다음 의존성을 추가합니다. 외부 배포 좌표를 의미하지 않습니다.

```kotlin
dependencies {
    implementation(project(":extension"))
}
```

호출할 확장 함수를 해당 패키지에서 import해야 합니다. 최소 SDK는 30이며, Bluetooth 연결 권한 등 OS별 권한은 호출 측에서 처리해야 합니다. View·Fragment·애니메이션 예제는 메인 스레드에서 실행합니다.

## 주제별 문서

| 문서 | 용도 |
| --- | --- |
| [컬렉션 확장 함수](collections.md) | 원본을 유지하는 리스트 항목 이동, Iterable 중복 식별자 추출·중복 없는 키로 맵 생성 |
| [값 변환](values.md) | dp → px, 분 → 시·분, 색상 알파, ByteArray 로그 및 String.parseByteHexLog 역변환 |
| [Context와 Lifecycle](context-lifecycle.md) | 권한·화면·프로세스 상태, 수명주기 기반 코루틴 |
| [Activity·Fragment·Navigation](components.md) | 시스템 바 색상, 다크 모드, Material 색상, Insets, 백스택 |
| [View와 Snackbar·애니메이션](views.md) | 클릭 간격 제한, 입력 뷰 검색, 메시지, 펼침·접힘·색상 |
| [Bluetooth](bluetooth.md) | 연결·본딩 상태, GATT 검색, 속성·Descriptor, 숨김 API |

각 문서의 API 표는 해당 소스의 모든 최상위 함수를 포함합니다. 같은 이름의 다른 수신 객체·오버로드도 함께 설명합니다.

## 예제 읽는 방법

`binding`, `viewModel`, `navController` 등은 호출 앱에서 준비한 객체입니다. 예제의 로컬 변수는 별도 설명이 없는 한 해당 화면·콜백 안에서 사용합니다. 예제는 사용법 설명이며 Android 기기에서 실행 검증한 샘플 앱이 아닙니다.

## 현재 구현의 중요한 제약

- `findEditChild`는 중첩 ViewGroup에서 잘못 재귀 호출하므로 평면 구조 외에는 사용하지 마세요.
- `repeatOnStartedWithViewOwner`는 이름과 달리 반복 조건에 Fragment lifecycle을 사용합니다. View 상태 수집은 `viewLifecycleOwner.repeatOnStarted`를 권장합니다.
- Bluetooth 속성 검사 함수는 현재 실제 `properties`를 검사하지 않습니다. 지원 여부 판단에는 직접 비트마스크 검사를 사용하세요.
- `isBondedBySsid`는 장치 이름이 아니라 주소 문자열을 검색합니다.

자세한 동작과 대체 호출은 각 문서에 있습니다. 문서화 과정에서 발견한 제약이며, 이번 PR에서 함수 구현을 수정하지 않습니다.
