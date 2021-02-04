package com.gitee.cs_liwei.annocache.sample.api.model;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TestModel {

    private String testField1;

    private long testField2;

    private List<String> testField3;

    private LocalDateTime testField4;

    private List<InnerModel> testField5;

    @Data
    public static class InnerModel{
        private String innerField;
    }

}
