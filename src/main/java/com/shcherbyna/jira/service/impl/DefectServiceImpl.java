package com.shcherbyna.jira.service.impl;

import com.shcherbyna.jira.service.DefectService;
import com.shcherbyna.jira.domain.Defect;
import com.shcherbyna.jira.repository.DefectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Defect}.
 */
@Service
@Transactional
public class DefectServiceImpl implements DefectService {

    private final Logger log = LoggerFactory.getLogger(DefectServiceImpl.class);

    private final DefectRepository defectRepository;

    public DefectServiceImpl(DefectRepository defectRepository) {
        this.defectRepository = defectRepository;
    }

    /**
     * Save a defect.
     *
     * @param defect the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Defect save(Defect defect) {
        log.debug("Request to save Defect : {}", defect);
        return defectRepository.save(defect);
    }

    /**
     * Get all the defects.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Defect> findAll(Pageable pageable) {
        log.debug("Request to get all Defects");
        return defectRepository.findAll(pageable);
    }

    /**
     * Get one defect by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Defect> findOne(Long id) {
        log.debug("Request to get Defect : {}", id);
        return defectRepository.findById(id);
    }

    /**
     * Delete the defect by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Defect : {}", id);
        defectRepository.deleteById(id);
    }
}
