package com.beyond.infinity.demo.repository;

import com.beyond.infinity.demo.model.DuplicateRequirements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequirementDuplicateRepository extends JpaRepository<DuplicateRequirements,String> {

    DuplicateRequirements findByProjectIdIgnoreCase(@Param("projectId") String projectId);

}
