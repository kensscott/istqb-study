package com.kensscott.istqb;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.*;

public class Result {

    private long start;
    private long end;
    private Exam exam;
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
        final StringBuilder sb = new StringBuilder("Exam " + exam.getName() + " result in time " + \n");

        float passed = 0;
        float failed = 0;
        for (final Question question : questions) {
            if (question.isPassed()) passed++;
            if (!question.isPassed()) failed++;
            final String correctAnswers = question.getAnswers().stream()
                    .map(Option::toString)
                    .collect(joining(","));
            final String guessedAnswers = question.getGuessed().stream()
                    .map(Option::toString)
                    .collect(joining(","));
            sb.append("Question ")
                    .append(question.getId())
                    .append(": ")
                    .append(question.isPassed() ? "PASS" : "INCORRECT")
                    .append(question.getGuessed())
                    .append(" guessed. Correct answers: ")
                    .append(question.getAnswers())
                    .append("\n");
        }
        sb.append("Total correct: ").append(passed).append(" / ").append(passed + failed).append(" = ").append(passed / (passed + failed) * 100).append("%\n");

        return sb.toString();
    }

    private String timeTaken() {
        Duration.of(this.end - this.start, TemporalUnit.)
    }
}
