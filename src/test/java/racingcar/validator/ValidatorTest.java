package racingcar.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import racingcar.domain.Cars;
import racingcar.domain.Name;
import racingcar.service.TryCount;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static racingcar.exception.ExceptionMessage.*;

class ValidatorTest {

    @ParameterizedTest
    @DisplayName("잘못된 시도 횟수가 검증되어야 한다.")
    @ValueSource(ints = {-1, -2, -3})
    void validate_illegalTryCount(int input) {
        // expected
        assertThatThrownBy(() -> new TryCount(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ILLEGAL_TRY_COUNT.getMessage());
    }

    @ParameterizedTest
    @DisplayName("정상적인 시도 횟수가 검증되어야 한다.")
    @ValueSource(ints = {0, 1, 2, 3, Integer.MAX_VALUE})
    void validate_tryCount(int input) {
        // expected
        assertThatCode(() -> new TryCount(input))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @DisplayName("중복된 자동차 이름이 검증되어야 한다.")
    @MethodSource("makeDuplicateNames")
    void validate_duplicateCarNames(String[] input) {
        // expected
        assertThatThrownBy(() -> new Cars(List.of(input)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(DUPLICATE_CAR_NAME.getMessage());
    }

    public static Stream<Arguments> makeDuplicateNames() {
        return Stream.of(
                Arguments.of((Object) new String[]{"car1", "car2", "car1"}),
                Arguments.of((Object) new String[]{"a", "b", "b"}),
                Arguments.of((Object) new String[]{"1", "1", "1"})
        );
    }

    @ParameterizedTest
    @DisplayName("중복되지 않는 자동차 이름이 검증되어야 한다.")
    @MethodSource("makeNoDuplicateNames")
    void validate_noDuplicateCarNames(String[] input) {
        // expected
        assertThatCode(() -> new Cars(List.of(input)))
                .doesNotThrowAnyException();
    }

    public static Stream<Arguments> makeNoDuplicateNames() {
        return Stream.of(
                Arguments.of((Object) new String[]{"car1", "car2", "car3"}),
                Arguments.of((Object) new String[]{"a", "b", "c"}),
                Arguments.of((Object) new String[]{"1", "2", "3"})
        );
    }

    @ParameterizedTest
    @DisplayName("공백인 자동차 이름이 검증되어야 한다.")
    @ValueSource(strings = {" ", "", "\n", "    "})
    void validate_blankCarName(String input) {
        // expected
        assertThatThrownBy(() -> new Name(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(BLANK_CAR_NAME.getMessage());
    }

    @ParameterizedTest
    @DisplayName("6글자 이상인 자동차 이름이 검증되어야 한다.")
    @ValueSource(strings = {"asgeiwsje", "fjeiwk", "djskfjq", "qqqqqqqqq"})
    void validate_outOfLengthCarName(String input) {
        // expected
        assertThatThrownBy(() -> new Name(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OUT_OF_CAR_NAME_LENGTH.getMessage());
    }
}
