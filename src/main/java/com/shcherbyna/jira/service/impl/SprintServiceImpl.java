package com.shcherbyna.jira.service.impl;

import com.shcherbyna.jira.service.SprintService;
import com.shcherbyna.jira.domain.Sprint;
import com.shcherbyna.jira.repository.SprintRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Sprint}.
 */
@Service
@Transactional
public class SprintServiceImpl implements SprintService {

    private final Logger log = LoggerFactory.getLogger(SprintServiceImpl.class);

    private final SprintRepository sprintRepository;

    public SprintServiceImpl(SprintRepository sprintRepository) {
        this.sprintRepository = sprintRepository;
    }

    /**
     * Save a sprint.
     *
     * @param sprint the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Sprint save(Sprint sprint) {
        log.debug("Request to save Sprint : {}", sprint);
        return sprintRepository.save(sprint);
    }

    /**
     * Get all the sprints.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Sprint> findAll() {
        log.debug("Request to get all Sprints");
        return sprintRepository.findAll();
    }

    /**
     * Get one sprint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Sprint> findOne(Long id) {
        log.debug("Request to get Sprint : {}", id);
        return sprintRepository.findById(id);
    }

    /**
     * Delete the sprint by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sprint : {}", id);
        sprintRepository.deleteById(id);
    }
}
