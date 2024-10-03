package com.kensscott.istqb.exam;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class Question {
    private final int id;
    private final List<Option> answers;
    private final List<Option> guessed = new ArrayList<>();


    public boolean isPassed() {
        boolean result = answers.size() == guessed.size();
        if (result) {
            for (Option answer : answers) {
                result = guessed.contains(answer);
                if (!result) break;
            }
        }
        return result;
    }
}
