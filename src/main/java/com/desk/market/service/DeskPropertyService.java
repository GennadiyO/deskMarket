package com.desk.market.service;

import com.desk.market.service.dto.DeskPropertyDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.desk.market.domain.DeskProperty}.
 */
public interface DeskPropertyService {

    /**
     * Save a deskProperty.
     *
     * @param deskPropertyDTO the entity to save.
     * @return the persisted entity.
     */
    DeskPropertyDTO save(DeskPropertyDTO deskPropertyDTO);

    /**
     * Get all the deskProperties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeskPropertyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" deskProperty.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeskPropertyDTO> findOne(Long id);

    /**
     * Delete the "id" deskProperty.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
