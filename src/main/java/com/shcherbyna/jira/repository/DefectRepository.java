package com.shcherbyna.jira.repository;

import com.shcherbyna.jira.domain.Defect;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Defect entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DefectRepository extends JpaRepository<Defect, Long> {
}
