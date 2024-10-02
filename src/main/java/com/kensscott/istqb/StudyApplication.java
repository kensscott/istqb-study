package com.kensscott.istqb;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.kensscott.istqb.exam.Exam;
import com.kensscott.istqb.exam.Question;
import com.kensscott.istqb.exam.Result;
import lombok.RequiredArgsConstructor;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class StudyApplication implements Runnable {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    private static final ExamArguments EXAM_ARGUMENTS = new ExamArguments();

    private final String style;
    private final String examName;

    public static void main(String[] args) {
        final JCommander examCommand = JCommander
                .newBuilder()
                .addObject(EXAM_ARGUMENTS)
                .build();
        try {
            examCommand.parse(args);
        } catch (final ParameterException e) {
            System.err.println(" * " + e.getMessage() + "\n");
        }

        StudyApplication app = new StudyApplication(EXAM_ARGUMENTS.getStyleParam(), EXAM_ARGUMENTS.getExamParam());

        try {
            Thread examiner = new Thread(app);
            examiner.start();
            examiner.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        final List<Exam> exams = readExams();
        for (final Exam exam : exams) {
            if (exam.getName().equals(this.examName)) {
                try {
                    Result result = this.processTest(exam);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to process the test " + exam.getName(), e);
                }
            }
        }
    }

    private List<Exam> readExams() {
        final String resourceName = "exams-" + this.style + ".yml";
        final Yaml yaml = new Yaml();
        InputStream fis = StudyApplication.class.getClassLoader().getResourceAsStream(resourceName);
        List<Map<String, Object>> raw = yaml.load(fis);
        return mapToExams(raw);
    }


    private List<Exam> mapToExams(List<Map<String, Object>> raw) {
        final List<Exam> exams = new ArrayList<>();
        for (Map<String, Object> map : raw) {
            final String name = (String) map.get("name");
            @SuppressWarnings({"unchecked", "rawtypes"})
            List<Question> questions = ((List<Map>) map.get("questions")).stream()
                    .map(q -> Question.builder()
                            .id((Integer) q.get("id"))
                            .level((String) q.get("level"))
                            .answers(((List<String>) q.get("answers")).stream().map(a -> com.kensscott.istqb.exam.Option.valueOf(a.trim().toUpperCase())).collect(Collectors.toList()))
                            .build())
                    .collect(Collectors.toList());
            exams.add(Exam.builder().name(name).questions(questions).build());
        }
        return exams;
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

//    private Exam startTest(List<Exam> exams) throws IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        boolean quit = false;
//        while (!quit) {
//            exams.forEach(exam -> System.out.println(exam.getName()));
//            System.out.print("Select an exam to take. X to exit -> ");
//            final String response = reader.readLine().trim().toLowerCase();
//            if (response.equals("x")) {
//                System.out.println("\nQuitting");
//                quit = true;
//                continue;
//            }
//            for (Exam e : exams) {
//                if (e.getName().toLowerCase().equals(response)) {
//                    return e;
//                }
//            }
//            System.out.println("\nInvalid entry. Try again.");
//        }
//        return null;
//    }


}
