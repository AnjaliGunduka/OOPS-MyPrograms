package com.junodx.api.repositories.lab;

import com.junodx.api.controllers.payloads.ReportConfigurationPayload;
import com.junodx.api.controllers.payloads.SampleIdListPayload;
import com.junodx.api.models.laboratory.Kit;
import com.junodx.api.models.laboratory.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TestRunRepository extends JpaRepository<TestRun, String> {
    List<TestRun> findTestRunsByIdIn(List<String> ids);
    Optional<TestRun> findTestRunByKit(Kit kit);
    Optional<TestRun> findTestRunByKit_Id(String id);
    Optional<TestRun> findTestRunByKit_IdAndBatch_Id(String kId, String bId);
    List<TestRun> findTestRunsByLaboratoryOrder_Id(String id);
    Optional<TestRun> findTestRunByLimsReportId(String id);

    List<TestRun> findTestRunsByKitIn(List<Kit> kitIds);

}
