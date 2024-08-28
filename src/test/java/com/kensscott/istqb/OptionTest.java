package com.kensscott.istqb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.kensscott.istqb.Option.fromString;

public class OptionTest {

    @Test
    public void testParseOptions() {
        final String input = "  c ,  d  ";
        final List<Option> options = fromString(input);
        Assertions.assertEquals(2, options.size());
    }

}
