package com.shcherbyna.jira.service.impl;

import com.shcherbyna.jira.service.StoryService;
import com.shcherbyna.jira.domain.Story;
import com.shcherbyna.jira.repository.StoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Story}.
 */
@Service
@Transactional
public class StoryServiceImpl implements StoryService {

    private final Logger log = LoggerFactory.getLogger(StoryServiceImpl.class);

    private final StoryRepository storyRepository;

    public StoryServiceImpl(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    /**
     * Save a story.
     *
     * @param story the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Story save(Story story) {
        log.debug("Request to save Story : {}", story);
        return storyRepository.save(story);
    }

    /**
     * Get all the stories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Story> findAll(Pageable pageable) {
        log.debug("Request to get all Stories");
        return storyRepository.findAll(pageable);
    }

    /**
     * Get one story by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Story> findOne(Long id) {
        log.debug("Request to get Story : {}", id);
        return storyRepository.findById(id);
    }

    /**
     * Delete the story by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Story : {}", id);
        storyRepository.deleteById(id);
    }
}
