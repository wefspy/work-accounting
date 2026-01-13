package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.MilestoneEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MilestoneEvaluationRepository extends JpaRepository<MilestoneEvaluation, Long> {
    List<MilestoneEvaluation> findByProjectMilestone_ProjectTeam_Id(Long projectTeamId);
    List<MilestoneEvaluation> findByProjectMilestone_Id(Long projectMilestoneId);

    List<MilestoneEvaluation> findByProjectMilestone_ProjectTeam_TeamId(Long teamId);

    List<MilestoneEvaluation> findByProjectMilestone_ProjectTeam_TeamIdAndProjectMilestone_ProjectTeam_ProjectId(Long teamId, Long projectId);
    
    @Query("SELECT AVG(me.score) FROM MilestoneEvaluation me " +
           "JOIN me.projectMilestone pm " +
           "JOIN pm.projectTeam pt " +
           "WHERE pt.team.id = :teamId")
    BigDecimal getAverageScoreByTeamId(@Param("teamId") Long teamId);

    @Query("SELECT AVG(me.score) FROM MilestoneEvaluation me " +
           "JOIN me.projectMilestone pm " +
           "JOIN pm.projectTeam pt " +
           "WHERE pt.team.id IN :teamIds")
    BigDecimal getAverageScoreByTeamIds(@Param("teamIds") List<Long> teamIds);

    @Query("SELECT AVG(me.score) FROM MilestoneEvaluation me " +
           "JOIN me.projectMilestone pm " +
           "WHERE pm.projectTeam.id IN :projectTeamIds")
    BigDecimal getAverageScoreByProjectTeamIds(@Param("projectTeamIds") List<Long> projectTeamIds);

    @Query("SELECT AVG(me.score) FROM MilestoneEvaluation me " +
           "JOIN me.projectMilestone pm " +
           "WHERE pm.projectTeam.id = :projectTeamId")
    BigDecimal getAverageScoreByProjectTeamId(@Param("projectTeamId") Long projectTeamId);

    List<MilestoneEvaluation> findByProjectMilestoneId(Long projectMilestoneId);
}
