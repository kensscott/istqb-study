package com.kensscott.istqb;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private long start;
    private long end;
    private final List<Question> questions = new ArrayList<>();

    public void start() {
        this.start = System.currentTimeMillis();
    }

    public void record(final Question question, List<Option> selections) {
        question.setGuessed(selections);
        if (question.getAnswers().contains(selections.get(0))) {
            if (questions.size() > 1) {
                if (question.getAnswers().contains(selections.get(1))) {
                    question.setPass(true);
                }

            }
        }
        questions.add(question);
    }

    public void stop() {
        this.end = System.currentTimeMillis();
    }
}
