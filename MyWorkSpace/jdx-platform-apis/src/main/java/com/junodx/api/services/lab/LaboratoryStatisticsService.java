package com.junodx.api.services.lab;

import com.junodx.api.models.core.types.IntervalType;
import com.junodx.api.models.laboratory.Laboratory;
import com.junodx.api.models.laboratory.LaboratoryStatistics;
import com.junodx.api.repositories.lab.LaboratoryStatisticsRepository;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.exceptions.JdxServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class LaboratoryStatisticsService extends ServiceBase {

    @Autowired
    private LaboratoryStatisticsRepository laboratoryStatisticsRepository;

    @Autowired
    private LaboratoryService laboratoryService;

    public Optional<LaboratoryStatistics> getLaboratoryStatisticsByLaboratory(String id, UserDetailsImpl user) {
        //return laboratoryStatisticsRepository.findLaboratoryStatisticsByLaboratory_Id(id);
        return Optional.ofNullable(generateFakeData(1L, user));
    }

    public List<LaboratoryStatistics> getAllStatisticsForAllLaboratorties(UserDetailsImpl user) {
        return laboratoryStatisticsRepository.findAll();
        //List<LaboratoryStatistics> stats = new ArrayList<>();
        //stats.add(generateFakeData(1L, user));

        //return stats;
    }

    public LaboratoryStatistics saveLaboratoryStatistics(LaboratoryStatistics stats, UserDetailsImpl user) throws JdxServiceException {
        if(user != null)
            stats.setMeta(buildMeta(user));
        if(stats != null && stats.getLaboratory() != null){
            Optional<Laboratory> lab = laboratoryService.getLaboratory(stats.getLaboratory().getId());
            if(lab.isPresent())
                stats.setLaboratory(lab.get());
            else
                throw new JdxServiceException("Cannot save laboratory statistics because lab it references is not present");
        }
        else
            throw new JdxServiceException("Laboratory referenced for stats not found");

        return laboratoryStatisticsRepository.save(stats);
    }

    public LaboratoryStatistics updateLaboratoryStatistics(String laboratoryId, LaboratoryStatistics stats, UserDetailsImpl user) {
        if (stats != null) {
            Optional<LaboratoryStatistics> updateO = laboratoryStatisticsRepository.findLaboratoryStatisticsByLaboratory_Id(laboratoryId);
            if (updateO.isPresent()) {
                LaboratoryStatistics update = updateO.get();
                update.setInterval(stats.getInterval());
                update.setIntervalType(stats.getIntervalType());
                update.setRecentlyAutomaticallySignedOut(stats.getRecentlyAutomaticallySignedOut());
                update.setRecentlyManuallySignedOut(stats.getRecentlyManuallySignedOut());
                update.setRecentlySignedOut(stats.getRecentlySignedOut());
                update.setResultsAwaitingInvestigation(stats.getResultsAwaitingInvestigation());
                update.setRunsToBeApproved(stats.getRunsToBeApproved());
                update.setResultsAwaitingSignOutConfirmation(stats.getResultsAwaitingSignOutConfirmation());
                update.setUpcomingResults(stats.getUpcomingResults());

                update.setMeta(updateMeta(update.getMeta(), user));

                return laboratoryStatisticsRepository.save(update);
            } else
                return saveLaboratoryStatistics(stats, user);
        }
        else
            return null;
    }

    public LaboratoryStatistics generateFakeData(Long id, UserDetailsImpl user){
        LaboratoryStatistics stats = new LaboratoryStatistics();
        stats.setId(id);
        stats.setInterval(3);
        stats.setIntervalType(IntervalType.DAYS);
        stats.setRecentlyAutomaticallySignedOut(210);
        stats.setRecentlyManuallySignedOut(56);
        stats.setRecentlySignedOut(50);
        stats.setResultsAwaitingInvestigation(30);
        stats.setResultsAwaitingSignOutConfirmation(40);
        stats.setRunsToBeApproved(3);
        stats.setUpcomingResults(96);

        Laboratory lab = new Laboratory();
        lab.setId(UUID.randomUUID().toString());
        lab.setName("Juno - San Diego");
        stats.setLaboratory(lab);

        stats.setMeta(buildMeta(user));

        return stats;
    }
}
