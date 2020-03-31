package com.desk.market.service.impl;

import com.desk.market.service.DeskService;
import com.desk.market.domain.Desk;
import com.desk.market.repository.DeskRepository;
import com.desk.market.service.dto.DeskDTO;
import com.desk.market.service.mapper.DeskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Desk}.
 */
@Service
@Transactional
public class DeskServiceImpl implements DeskService {

    private final Logger log = LoggerFactory.getLogger(DeskServiceImpl.class);

    private final DeskRepository deskRepository;

    private final DeskMapper deskMapper;

    public DeskServiceImpl(DeskRepository deskRepository, DeskMapper deskMapper) {
        this.deskRepository = deskRepository;
        this.deskMapper = deskMapper;
    }

    /**
     * Save a desk.
     *
     * @param deskDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DeskDTO save(DeskDTO deskDTO) {
        log.debug("Request to save Desk : {}", deskDTO);
        Desk desk = deskMapper.toEntity(deskDTO);
        desk = deskRepository.save(desk);
        return deskMapper.toDto(desk);
    }

    /**
     * Get all the desks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DeskDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Desks");
        return deskRepository.findAll(pageable)
            .map(deskMapper::toDto);
    }

    /**
     * Get one desk by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DeskDTO> findOne(Long id) {
        log.debug("Request to get Desk : {}", id);
        return deskRepository.findById(id)
            .map(deskMapper::toDto);
    }

    /**
     * Delete the desk by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Desk : {}", id);
        deskRepository.deleteById(id);
    }
}
