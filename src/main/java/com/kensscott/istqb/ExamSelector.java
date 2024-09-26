package com.kensscott.istqb;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.kensscott.istqb.exam.Exam;
import com.kensscott.istqb.exam.Question;
import com.kensscott.istqb.exam.Result;
import lombok.AllArgsConstructor;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
public class ExamSelector implements Runnable {

    private final String[] args;

    @Override
    public void run() {
        final ExamArgs examArgs = new ExamArgs();
        final JCommander examCommand = JCommander
                .newBuilder()
                .addObject(examArgs)
                .build();
        try {
            examCommand.parse(args);
        } catch (final ParameterException e) {
           System.err.println(" * " + e.getMessage() + "\n");
        }

//        if (args.length > 1) throw new RuntimeException("Invalid args");
//        String style = args.length == 1 ? args[0] : "istqb";
//        System.out.println(style);
//        final StudyApplication app = new StudyApplication();
//        try {
//            final Exam exam = app.startTest(app.readExams(style));
//            if (exam != null) {
//                final String resultFile = "result"
//                        + style
//                        + "-exam-"
//                        + exam.getName()
//                        + "-"
//                        + sdf1.format(new Timestamp(System.currentTimeMillis()))
//                        + ".txt";
//                final Result result = app.processTest(exam);
//                final BufferedWriter out = new BufferedWriter(new FileWriter(resultFile));
//                out.write(result.toString());
//                out.close();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
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
