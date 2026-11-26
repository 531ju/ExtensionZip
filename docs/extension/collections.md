# 컬렉션 중복 키 검사

## 왜 사용하는가

사용자 ID, 목록 항목 식별자 등에서 중복 키를 찾아 입력 검증이나 오류 표시를 할 때 사용합니다. `groupBy`로 모든 항목을 보관하지 않고 한 번 순회합니다.

## API 및 준비

저장소 모듈 의존성 `implementation(project(":extension"))`을 추가합니다. Android 권한은 필요하지 않습니다.

```kotlin
import com.kjj.extension.value.duplicateKeysBy

fun <T, K> Iterable<T>.duplicateKeysBy(selector: (T) -> K): Set<K>
```

- 수신 객체: 유한한 `Iterable<T>`.
- `selector`: 각 원소에서 키를 추출하며 원소마다 정확히 한 번 호출됩니다.
- 반환: 두 번 이상 등장한 키를 하나씩 담은 새 집합. 두 번째 등장 시점 순서를 유지합니다.

## 사용 예제

```kotlin
import com.kjj.extension.value.duplicateKeysBy

val repeated = listOf("a", "b", "b", "a", "a").duplicateKeysBy { it }
check(repeated.toList() == listOf("b", "a"))
```

위 예제의 결과는 단위 테스트로 검증합니다. 빈 입력과 중복 없는 입력은 빈 집합이며 null 키도 정상 처리합니다. 키 비교는 `equals`/`hashCode`를 사용하므로 순회 도중 키를 변경하지 마세요. selector 예외는 그대로 전달합니다. 원본은 변경하지 않으며 평균 시간 O(n), 공간 O(k) (서로 다른 키 수)입니다. 무한 입력에는 사용하지 마세요.
