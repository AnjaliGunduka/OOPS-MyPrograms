package com.junodx.api.services.lab;

import com.junodx.api.dto.models.laboratory.LaboratoryReviewGroupDto;
import com.junodx.api.dto.models.laboratory.LaboratoryReviewStatisticsDto;
import com.junodx.api.models.laboratory.types.ReportConfiguration;
import com.junodx.api.services.ServiceBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LaboratoryReviewStatisticsService extends ServiceBase {

    @Autowired
    private LaboratoryService laboratoryService;

 //   @Autowired
 //   private LaboratoryReviewStatisticsRepository laboratoryReviewStatisticsRepository;

    public Optional<LaboratoryReviewStatisticsDto> getLaboratoryStatisticsByLaboratory(String id) {
        //return laboratoryReviewStatisticsRepository.findLaboratoryReviewStatisticsByLaboratoryId(id);
        return null;
    }

    /*
    public List<LaboratoryReviewStatistics> getAllStatisticsForAllLaboratorties() {
        return laboratoryReviewStatisticsRepository.findAll();
    }



    public LaboratoryReviewStatistics saveLaboratoryReviewStatistics(LaboratoryReviewStatistics stats, UserDetailsImpl user) throws JdxServiceException {
        stats.setMeta(buildMeta(user));


        if (stats != null) {
            if (stats.getLaboratory() == null)
                throw new JdxServiceException("Laboratory must be set before adding stats");
            else {
                Optional<Laboratory> lab = laboratoryService.getLaboratory(stats.getLaboratory().getId());
                if (lab.isPresent())
                    stats.setLaboratory((Laboratory) lab.get());
                else
                    throw new JdxServiceException("Laboratory must be set before adding stats");
            }

      //      Laboratory lab = stats.getLaboratory();

      //      stats.setLaboratory(laboratoryService.saveLaboratory(lab, user));

            stats = laboratoryReviewStatisticsRepository.save(stats);
        //}

        return stats;
    }

    public LaboratoryReviewStatistics updateLaboratoryStatistics(LaboratoryReviewStatistics stats, UserDetailsImpl user) throws JdxServiceException {
        if (stats != null) {

            if(stats.getLaboratory() == null)
                throw new JdxServiceException("Laboratory must be set before adding stats");
            else {
                Optional<Laboratory> lab = laboratoryService.getLaboratory(stats.getLaboratory().getId());
                if(lab.isPresent())
                    stats.setLaboratory((Laboratory) lab.get());
                else
                    throw new JdxServiceException("Laboratory must be set before adding stats");
            }



            Optional<LaboratoryReviewStatistics> updateO = laboratoryReviewStatisticsRepository.findLaboratoryReviewStatisticsByLaboratoryId(stats.getLaboratoryId());
            if (updateO.isPresent()) {
                LaboratoryReviewStatistics stat = updateO.get();
              //  stat.setLaboratory(stats.getLaboratory());
                stat.setReview(stats.getReview());
                stat.setLaboratoryId(stats.getLaboratoryId());
                for(LaboratoryReviewGroup group : stat.getReview())
                    group.setLaboratoryReviewStatistics(stat);

                stat.setMeta(updateMeta(stat.getMeta(), user));
            //    Laboratory lab = stats.getLaboratory();

           //     stats.setLaboratory(laboratoryService.saveLaboratory(lab, user));

                stats = laboratoryReviewStatisticsRepository.save(stat);




                return stats;
            }
        }
        return saveLaboratoryReviewStatistics(stats, user);
    }
                */
}
