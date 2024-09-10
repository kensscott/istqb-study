package com.kensscott.istqb;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.kensscott.istqb.Option.fromString;
import static org.assertj.core.api.Assertions.assertThat;

public class OptionTest {

    @ParameterizedTest
    @ValueSource(strings = {"c,d", "c ,d", " c,d", " c    ,      d   "})
    public void testParseOptions(final String input) {
        final List<Option> expected = List.of(Option.C, Option.D);
        final List<Option> options = fromString(input);
        assertThat(options).hasSameElementsAs(expected);
    }

}
