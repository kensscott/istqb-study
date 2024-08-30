package com.kensscott.istqb;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Result {

    private long start;
    private long end;
    private final List<Question> questions;

    public Result(final Exam exam) {
        this.questions = new ArrayList<>(exam.getQuestions());
    }

    public void start() {
        this.start = System.currentTimeMillis();
    }

    public void recordSelection(final int questionId, Option selection) {
        this.questions.stream().filter(q -> q.getId() == questionId).findFirst().ifPresent(q -> { q.getGuessed().add(selection); });
    }

    public void stop() {
        this.end = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Exam result\n");

        int passed = 0;
        int failed = 0;
        for (final Question question : questions) {
            if (question.isPassed()) passed++;
            if (!question.isPassed()) failed++;
            final String correctAnswers = question.getAnswers().stream()
                    .map(Option::toString)
                    .collect(joining(","));
            final String guessedAnswers = question.getGuessed().stream()
                    .map(Option::toString)
                    .collect(joining(","));
            sb.append("Correct anwer(s): ")
                    .append(correctAnswers)
                    .append(" Guessed answer(s): ")
                    .append(guessedAnswers)
                    .append("\n");
        }

        return sb.toString();
    }
}
