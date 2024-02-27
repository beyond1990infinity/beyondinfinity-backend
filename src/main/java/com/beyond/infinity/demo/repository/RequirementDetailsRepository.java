package com.beyond.infinity.demo.repository;

import com.beyond.infinity.demo.model.RequirementJiraProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequirementDetailsRepository extends JpaRepository<RequirementJiraProject, String> {

    RequirementJiraProject findByProjectIdIgnoreCase(@Param("projectId") String projectId);

}
