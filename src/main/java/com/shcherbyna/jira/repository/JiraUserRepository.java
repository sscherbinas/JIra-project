package com.shcherbyna.jira.repository;

import com.shcherbyna.jira.domain.JiraUser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the JiraUser entity.
 */
@Repository
public interface JiraUserRepository extends JpaRepository<JiraUser, Long> {

    @Query(value = "select distinct jiraUser from JiraUser jiraUser left join fetch jiraUser.projects",
        countQuery = "select count(distinct jiraUser) from JiraUser jiraUser")
    Page<JiraUser> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct jiraUser from JiraUser jiraUser left join fetch jiraUser.projects")
    List<JiraUser> findAllWithEagerRelationships();

    @Query("select jiraUser from JiraUser jiraUser left join fetch jiraUser.projects where jiraUser.id =:id")
    Optional<JiraUser> findOneWithEagerRelationships(@Param("id") Long id);
}
