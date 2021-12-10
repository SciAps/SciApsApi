package com.sciaps;

import java.util.List;

public class ZAnalysisResult {
    public String mode;
    public long timestamp;                  // unix timestamp of test result
    public long durationMs;                 // test duration in milliseconds
    public double latitude;                 // current latitude at time of test
    public double longitude;                // current latitude at time of test
    public String base;                     //
    public String modelName;                //
    public List<ZChemInfo> chemistry;
    public String firstGradeMatch;
    public double firstGradeMatchScore;
    public String secondGradeMatch;
    public double secondGradeMatchScore;
    public String thirdGradeMatch;
    public double thirdGradeMatchScore;
    public String gradeLibraryName;
}
