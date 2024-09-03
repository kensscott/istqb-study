package com.kensscott.istqb;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Option {
    A, B, C, D, E, UNDEFINED;

    private final char value;

    Option() {
        this.value = this.name().charAt(0);
    }

    public static Option valueOf(final char value) {
        for (Option opt : List.of(values())) {
            if (opt.value == value) {
                return opt;
            }
        }
        return UNDEFINED;
    }

    public static List<Option> fromString(final String value) {
        final String[] values = value.split(",");
        return Arrays.stream(values)
                .map(String::trim)
                .map(String::toUpperCase)
                .map(Option::valueOf)
                .collect(Collectors.toList());
    }
}
