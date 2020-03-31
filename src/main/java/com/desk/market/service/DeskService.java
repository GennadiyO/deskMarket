package com.desk.market.service;

import com.desk.market.service.dto.DeskDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.desk.market.domain.Desk}.
 */
public interface DeskService {

    /**
     * Save a desk.
     *
     * @param deskDTO the entity to save.
     * @return the persisted entity.
     */
    DeskDTO save(DeskDTO deskDTO);

    /**
     * Get all the desks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeskDTO> findAll(Pageable pageable);

    /**
     * Get the "id" desk.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeskDTO> findOne(Long id);

    /**
     * Delete the "id" desk.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
