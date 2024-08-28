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

    public static List<Option> fromString(final String value) {
        final String[] values = value.split(",");
        return Arrays.stream(values)
                .map(String::trim)
                .map(v ->
                v.isEmpty() ?
                        UNDEFINED :
                        Arrays.stream(Option.values())
                                .filter(o -> o.value == value.charAt(0)).
                                findFirst().orElse(UNDEFINED)).collect(Collectors.toList());
    }
}