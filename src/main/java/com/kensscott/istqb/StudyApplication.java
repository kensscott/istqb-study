package com.kensscott.istqb;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.List;

public class StudyApplication {

    public static void main(String[] args) throws IOException {
        final Yaml yaml = new Yaml();
        InputStream fis = StudyApplication.class.getClassLoader().getResourceAsStream("exams.yml");
        final List<Exam> exams = yaml.load(fis);
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));



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

}
