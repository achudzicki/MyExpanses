package com.chudzick.expanses.services.analysis;

import com.chudzick.expanses.domain.analysis.AverageExpanse;

import java.util.List;

public interface AnalysisService {

    List<AverageExpanse> getAverageExpansesList();
}
