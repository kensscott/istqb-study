package com.kensscott.istqb;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.kensscott.istqb.exam.Exam;
import com.kensscott.istqb.exam.Result;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.IntStream;

public class StudyApplication implements Runnable, IParameterValidator {

    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    public static void main(String[] args) {
        try {
            StudyApplication app = new StudyApplication();
            Thread examiner = new Thread(app);
            examiner.start();
            examiner.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        final ExamArgs examArgs = new ExamArgs(this);
        final JCommander examCommand = JCommander
                .newBuilder()
                .addObject(examArgs)
                .build();
        try {
            examCommand.parse(args);
        } catch (final ParameterException e) {
            System.err.println(" * " + e.getMessage() + "\n");
        }


    }

    @Override
    public void validate(String name, String value) throws ParameterException {
        if (name.equals("--source") || name.equals("-s")) {
            if (!List.of("istqb", "astqb").contains(value.toLowerCase())) {
                throw new ParameterException("Invalid value for the exam source. (ISTQB|ASTQB)");
            }

            exams = readExams(value);
        }
    }

    private Result processTest(Exam exam) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        final Result result = new Result(exam);
        System.out.println("Taking test " + exam.getName() + ". Press Enter key to begin...");
        reader.readLine();
        result.start();
        exam.getQuestions().forEach(question -> {
            int optionLimit = question.getAnswers().size();
            IntStream.range(0, optionLimit).forEach(index -> {
                final String which = ((index == 0) ? "first " : "second ");
                System.out.print("Enter "
                        + ((optionLimit == 1) ? "" : which)
                        + "selection for question "
                        + question.getId()
                        + ": ");
                try {
                    final String response = reader.readLine().trim().toUpperCase();
                    if (!response.isEmpty()) {
                        result.recordSelection(question.getId(), com.kensscott.istqb.exam.Option.valueOf(response.charAt(0)));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        result.stop();
        return result;
    }

    private Exam startTest(List<Exam> exams) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean quit = false;
        while (!quit) {
            exams.forEach(exam -> System.out.println(exam.getName()));
            System.out.print("Select an exam to take. X to exit -> ");
            final String response = reader.readLine().trim().toLowerCase();
            if (response.equals("x")) {
                System.out.println("\nQuitting");
                quit = true;
                continue;
            }
            for (Exam e : exams) {
                if (e.getName().toLowerCase().equals(response)) {
                    return e;
                }
            }
            System.out.println("\nInvalid entry. Try again.");
        }
        return null;
    }


}
