package com.kensscott.istqb;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.kensscott.istqb.exam.Exam;
import com.kensscott.istqb.exam.Question;
import com.kensscott.istqb.exam.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Data
public class ExamSelector implements Runnable {

    private final String[] args;
    private final List<Exam> exams = new ArrayList<>();
    private Exam selectedExam;

    @Override
    public void run() {
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

}
