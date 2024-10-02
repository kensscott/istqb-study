package com.kensscott.istqb;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ExamArguments {

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
            }
        }
    }

}
