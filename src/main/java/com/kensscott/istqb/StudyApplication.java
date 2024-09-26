package com.kensscott.istqb;

import java.text.SimpleDateFormat;

public class StudyApplication {

    private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

    public static void main(String[] args) {
        try {
            ExamSelector examSelector = new ExamSelector(args);
            Thread examiner = new Thread(examSelector);
            examiner.start();
            examiner.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
