package com.kensscott.istqb;

import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudyApplication {

    public static void main(String[] args) {

        final StudyApplication app = new StudyApplication();
        try {
            Exam exam = app.startTest(app.readExams());
            if (exam != null) {
                processTest(exam);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        int questions = a.getAnswers().size();
//        for (int index = 0; index < questions; index++) {
//            System.out.printf("Answer for question %d:%n", index + 1);
//            guesses.add(reader.readLine());
//        };
//
//        int correct = 0;
//        for (int index = 0; index < questions; index++) {
//            if (a.getAnswers().get(index).equals(guesses.get(index))) {
//                correct++;
//            }
//        }
//        System.out.printf("Score = %d / %d (%02f)%n", correct, questions, (correct * 1f / questions) * 100.0);
    }

    private List<Exam> readExams() {
        final Yaml yaml = new Yaml();
        InputStream fis = StudyApplication.class.getClassLoader().getResourceAsStream("exams.yml");
        List<Map<String, Object>> raw = yaml.load(fis);
        return mapToExams(raw);
    }

    private void processTest(Exam exam) {
        System.out.println("Taking test " + exam.getName() + ". Press Enter key to begin...");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        final long start = System.currentTimeMillis();
        exam.getQuestions().forEach(question -> {

        });
        final long duration = System.currentTimeMillis() - start;


    }

    private Exam startTest(List<Exam> exams) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean quit = false;
        while (!quit) {
            System.out.print("Select an exam to take. (A, B, C, D) or X to exit -> ");
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

    private List<Exam> mapToExams(List<Map<String, Object>> raw) {
        final List<Exam> exams = new ArrayList<>();
        for (Map<String, Object> map : raw) {
            final String name = (String) map.get("name");
            @SuppressWarnings({"unchecked", "rawtypes"})
            List<Question> questions = ((List<Map>) map.get("questions")).stream()
                    .map(q -> Question.builder()
                            .id((Integer) q.get("id"))
                            .level((String) q.get("level"))
                            .answers(((List<String>) q.get("answers")).stream().map(a -> Option.valueOf(a.trim().toUpperCase())).collect(Collectors.toList()))
                            .build())
                    .collect(Collectors.toList());
            exams.add(Exam.builder().name(name).questions(questions).build());
        }
        return exams;
    }
}
