package com.kensscott.istqb;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class Question {
    private final int id;
    private final String level;
    private final List<Option> answers;
    private List<Option> guessed = new ArrayList<>();
    private boolean pass = false;
}
