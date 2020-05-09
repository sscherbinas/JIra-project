package com.shcherbyna.jira.service.impl;

import com.shcherbyna.jira.service.JiraUserService;
import com.shcherbyna.jira.domain.JiraUser;
import com.shcherbyna.jira.repository.JiraUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link JiraUser}.
 */
@Service
@Transactional
public class JiraUserServiceImpl implements JiraUserService {

    private final Logger log = LoggerFactory.getLogger(JiraUserServiceImpl.class);

    private final JiraUserRepository jiraUserRepository;

    public JiraUserServiceImpl(JiraUserRepository jiraUserRepository) {
        this.jiraUserRepository = jiraUserRepository;
    }

    /**
     * Save a jiraUser.
     *
     * @param jiraUser the entity to save.
     * @return the persisted entity.
     */
    @Override
    public JiraUser save(JiraUser jiraUser) {
        log.debug("Request to save JiraUser : {}", jiraUser);
        return jiraUserRepository.save(jiraUser);
    }

    /**
     * Get all the jiraUsers.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<JiraUser> findAll() {
        log.debug("Request to get all JiraUsers");
        return jiraUserRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the jiraUsers with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<JiraUser> findAllWithEagerRelationships(Pageable pageable) {
        return jiraUserRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one jiraUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<JiraUser> findOne(Long id) {
        log.debug("Request to get JiraUser : {}", id);
        return jiraUserRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the jiraUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete JiraUser : {}", id);
        jiraUserRepository.deleteById(id);
    }
}
