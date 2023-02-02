package com.sciaps;

import java.util.List;

public class AnalysisResult {
    public String mode;                     // Analysis mode used to produce this result
    public long timestamp;                  // unix timestamp of the test result
    public long durationMs;                 // test duration in milliseconds
    public double latitude;                 // current latitude at time of test
    public double longitude;                // current latitude at time of test
    public String base;                     // Base name
    public String modelName;                // Model name
    public List<ChemInfo> chemistry;        // Elemental data
    public String firstGradeMatch;          // Grade name of closest match
    public double firstGradeMatchScore;     // Grade match score of closest match
    public String secondGradeMatch;         // Grade name of second closest match
    public double secondGradeMatchScore;    // Grade match score of second closest match
    public String thirdGradeMatch;          // Grade name of third closest match
    public double thirdGradeMatchScore;     // Grade match score of third closest match
    public String gradeLibraryName;         // Name of grade library used
}
