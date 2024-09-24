package com.kensscott.istqb;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Result {

    private long start;
    private long end;
    private final Exam exam;
    private final List<Question> questions;

    public Result(final Exam exam) {
        this.exam = exam;
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
        final String lineTemplate = "Question %02d: %-10s  (%-10s) %s\n";
        final StringBuilder sb = new StringBuilder("Exam " + exam.getName() + "\n");

        float passed = 0;
        float failed = 0;
        for (final Question question : questions) {
            if (question.isPassed()) passed++;
            if (!question.isPassed()) failed++;
            sb.append(String.format(lineTemplate,
                            question.getId(),
                            question.getGuessed(),
                            question.getAnswers(),
                            question.isPassed() ? "PASS" : "INCORRECT"));
        }
        sb.append("Total correct: ")
                .append(passed)
                .append(" / ")
                .append(passed + failed)
                .append(" = ")
                .append(passed / (passed + failed) * 100)
                .append("%\n")
                .append("Time taken: ")
                .append(timeTaken());

        return sb.toString();
    }

    private String timeTaken() {
        final Duration duration = Duration.of(this.end - this.start, ChronoUnit.MILLIS);
        return duration.toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }
}
