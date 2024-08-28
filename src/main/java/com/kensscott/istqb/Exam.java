package com.kensscott.istqb;

import lombok.*;

import java.util.List;

@Builder
@Data
@RequiredArgsConstructor
public class Exam {
    private final String name;
    private final List<Question> questions;
}
