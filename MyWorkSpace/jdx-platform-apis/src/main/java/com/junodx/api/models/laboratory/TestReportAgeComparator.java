package com.junodx.api.models.laboratory;

import java.util.Comparator;

public class TestReportAgeComparator implements Comparator<TestReport> {
    public int compare(TestReport report1, TestReport report2) {
        return report1.getAge() - report2.getAge();
    }
}
