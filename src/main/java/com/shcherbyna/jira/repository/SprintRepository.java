package com.shcherbyna.jira.repository;

import com.shcherbyna.jira.domain.Sprint;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Sprint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long>, JpaSpecificationExecutor<Sprint> {
}
