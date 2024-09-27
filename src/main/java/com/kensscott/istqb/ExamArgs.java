package com.kensscott.istqb;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.kensscott.istqb.exam.Exam;
import com.kensscott.istqb.exam.Question;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class ExamArgs {

    private final ExamSelector examSelector;

    @Parameter(
            names = { "--source", "-s" },
            description = "The source of the practice exam. (istqb | asbqb)",
            arity = 1,
            required = true,
            order = 0,
            validateWith = SourceValidation.class)
    private String styleParam;

    @Parameter(
            names = { "--exam", "-e" },
            description = "Which exam should be used.",
            arity = 1,
            required = true,
            order = 1)
    private String examParam;



    @Parameter(
            names = { "--help", "-h" },
            help = true,
            order = 2)
    private boolean helpParam;

    public static class SourceValidation implements IParameterValidator {
        @Override
        public void validate(String name, String value) throws ParameterException {
            if (name.equals("--source") || name.equals("-s")) {
                if (!List.of("istqb", "astqb").contains(value.toLowerCase())) {
                    throw new ParameterException("Invalid value for the exam source. (ISTQB|ASTQB)");
                }

                exams = readExams(value);
            }
        }
    }

    public static class ExamNameValidation implements IParameterValidator {
        @Override
        public void validate(String name, String value) throws ParameterException {
            if (name.equals("--exam") || name.equals("-e")) {
                for (Exam exam : exams) {
                    if (exam.getName().equalsIgnoreCase(value)) {
                        selectedExam = exam;
                    }
                }
            }
        }
    }

    private static List<Exam> readExams(final String style) {
        final String resourceName = "exams-" + style + ".yml";
        final Yaml yaml = new Yaml();
        InputStream fis = ExamSelector.class.getClassLoader().getResourceAsStream(resourceName);
        List<Map<String, Object>> raw = yaml.load(fis);
        return mapToExams(raw);
    }


    private static List<Exam> mapToExams(List<Map<String, Object>> raw) {
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

}
