package com.kensscott.istqb;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.kensscott.istqb.exam.Exam;
import com.kensscott.istqb.exam.Question;
import com.kensscott.istqb.exam.Result;
import lombok.RequiredArgsConstructor;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        if (EXAM_ARGUMENTS.isHelpParam()) {
            examCommand.usage();
            System.exit(0);
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
                    final String resultFile = "result-exam"
                            + exam.getName()
                            + "-"
                            + SIMPLE_DATE_FORMAT.format(new Timestamp(System.currentTimeMillis()))
                            + ".txt";
                    final Result result = this.processTest(exam);
                    final BufferedWriter out = new BufferedWriter(new FileWriter(resultFile));
                    out.write(result.toString());
                    out.close();
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
        for (int index = 0; index < exam.getQuestions().size(); index++) {
            final Question question = exam.getQuestions().get(index);
            int optionLimit = question.getAnswers().size();
            for (int optionNum = 0; optionNum < optionLimit; optionNum++) {
                final String which = ((optionNum == 0) ? "first " : "second ");
                System.out.print("Enter "
                        + ((optionLimit == 1) ? "" : which)
                        + "selection for question "
                        + question.getId()
                        + " (or - to go back to previous question): ");
                try {
                    final String response = reader.readLine().trim().toUpperCase();
                    if (response.equals("-")) {
                        index -= 2;
                        break;
                    }
                    if (!response.isEmpty()) {
                        result.recordSelection(question.getId(), com.kensscott.istqb.exam.Option.valueOf(response.charAt(0)));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        result.stop();
        return result;
    }

}
