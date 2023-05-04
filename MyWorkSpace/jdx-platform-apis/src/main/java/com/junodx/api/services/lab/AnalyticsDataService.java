package com.junodx.api.services.lab;

import com.junodx.api.controllers.lab.AnalyticsDataController;
import com.junodx.api.models.laboratory.TestReport;
import com.junodx.api.repositories.lab.TestReportRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.exceptions.JdxServiceException;
import com.junodx.api.services.infra.AWSS3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AnalyticsDataService extends ServiceBase {

    @Autowired
    private AWSS3Service awss3Service;

    @Autowired
    private TestReportRepository testReportRepository;

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsDataService.class);

    public String getScaCutoffs() throws JdxServiceException {
        return "{\n" +
                "  \"fetal_sex_gap_lb\": 4.4905146005737195e-05,\n" +
                "  \"fetal_sex_gap_ub\": 6.600270359075774e-05\n" +
                "}";
    }

    public String getMaleScaCutoffs() throws JdxServiceException {
        return "{\n" +
                "  \"target_median_y\": 0.0,\n" +
                "  \"target_median_x\": 0.046268574203408845,\n" +
                "  \"XY_slope\": -20.55236151371743,\n" +
                "  \"XY_intercept\": 0.9631628903101979,\n" +
                "  \"XY_ci_ub_99_intercept\": 0.989989764262135,\n" +
                "  \"XY_ci_lb_99_intercept\": 0.9363360163582607,\n" +
                "  \"XY_ci_ub_95_intercept\": 0.9801559122495982,\n" +
                "  \"XY_ci_lb_95_intercept\": 0.9461698683707975,\n" +
                "  \"XY_orth_ci_ub_99\": 0.0013037516335137789,\n" +
                "  \"XY_orth_ci_lb_99\": 9.498751109890799e-06,\n" +
                "  \"XY_orth_ci_ub_95\": 0.0008258390504805321,\n" +
                "  \"XY_orth_ci_lb_95\": 1.431156732104713e-05,\n" +
                "  \"XY_vertical_ci_ub_99\": 0.026826873951937197,\n" +
                "  \"XY_vertical_ci_lb_99\": 0.00019545271674106086,\n" +
                "  \"XY_vertical_ci_ub_95\": 0.016993021939400384,\n" +
                "  \"XY_vertical_ci_lb_95\": 0.00029448447289123737,\n" +
                "  \"XY_x_ci_ub\": 0.04635816569957394,\n" +
                "  \"XY_x_ci_lb\": 0.04149805294801445,\n" +
                "  \"XYY_slope\": -35.400751049253,\n" +
                "  \"XYY_intercept\": 1.6814751634787997,\n" +
                "  \"XYY_orth_ci_ub\": 0.001038157014301164,\n" +
                "  \"XYY_orth_ci_lb\": -0.0006697637214266017,\n" +
                "  \"XYY_vertical_ci_ub\": 0.036766198012987514,\n" +
                "  \"XYY_vertical_ci_lb\": -0.023719596616569562,\n" +
                "  \"XYY_y_ci_ub_intercept\": 1.718241361491787,\n" +
                "  \"XYY_y_ci_lb_intercept\": 1.6447089654658122,\n" +
                "  \"XYY_x_ci_ub\": 0.04584947535234003,\n" +
                "  \"XYY_x_ci_lb\": 0.04078207526488766,\n" +
                "  \"XYY_ci_sig_level\": 0.99,\n" +
                "  \"XXY_intercept\": 0.046269539364456554,\n" +
                "  \"XXY_x_ci_ub_99\": 0.04694635591688098,\n" +
                "  \"XXY_x_ci_lb_99\": 0.04529170961039507,\n" +
                "  \"XXY_x_ci_ub_95\": 0.046800867877070294,\n" +
                "  \"XXY_x_ci_lb_95\": 0.045678943205713696,\n" +
                "  \"XXY_Y_y_intersection\": 1.0038999427260298,\n" +
                "  \"XY_ci_sig_level\": 0.99\n" +
                "}";
    }

    public String getFemaleScaCutoffs() throws JdxServiceException {
        return "{\n" +
                "  \"XX_median\": 0.04628768971219951,\n" +
                "  \"XX_mad\": 0.00023529229167248527,\n" +
                "  \"XX_z_lb\": -4.6,\n" +
                "  \"XX_z_ub\": 4.6,\n" +
                "  \"X0_z_ub\": -6.5,\n" +
                "  \"XXX_z_lb\": 6.5\n" +
                "}";
    }

    /*
    public FileSystemResource getScaReferenceData(ServletOutputStream outputStream) throws JdxServiceException {
        String fileName = "ref_sca_vec_df.csv";
        try {
            //File file = ResourceUtils.getFile(fileName);
            //return Files.copy(file.toPath(), outputStream);
            File file = ResourceUtils.getFile("classpath:static/" + fileName);

            return new FileSystemResource(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new JdxServiceException("Cannot obtain SCA reference data: " + e.getMessage());
        }
    }

     */

    public List<String> getReportRawDataForBatch(String batchId, List<String> excludeIds) throws JdxServiceException {
        List<String> rawRecords = new ArrayList<>();
        rawRecords.add("sample_number,seq_run_number,pipeline_run_id,sca,fetal_sex,read_counts,x_vec,y_vec,y_vec_2,x_z_scores,gender_confidence,sca_confidence,trisomy_13,z_score_chr13,trisomy_18,z_score_chr18,trisomy_21,z_score_chr21,autosome_aneuploidy");

        List<TestReport> reports = testReportRepository.findTestReportsByBatchRunId(batchId);
        if(!reports.isEmpty()){
            for(TestReport report : reports) {
                if(excludeIds.stream().filter(x->x.equals(report.getId())).findAny().isEmpty())
                    rawRecords.add(report.getRawCsvData());
            }
        }
        return rawRecords;
    }


////,sample_number,x_vec,y_vec,y_vec_2,x_z_scores,percent_rank_y_vec,sca,fetal_sex
    public List<String> getScaReferenceData(String batchId, List<String> excludeIds) throws JdxServiceException {
        List<String> rawRecords = new ArrayList<>();
        int count = 0;
        rawRecords.add(",sample_number,x_vec,y_vec,y_vec_2,x_z_scores,percent_rank_y_vec,sca,fetal_sex");

        List<TestReport> reports = testReportRepository.findTestReportsByBatchRunId(batchId);
        if(!reports.isEmpty()){
            for(TestReport report : reports) {
                if(excludeIds.stream().filter(x->x.equals(report.getId())).findAny().isEmpty())
                    rawRecords.add(count++ + "," + report.getRawScaData());
            }
        }
        return rawRecords;
    }
}
