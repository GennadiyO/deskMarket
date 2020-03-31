package com.desk.market.service.impl;

import com.desk.market.service.DeskPropertyService;
import com.desk.market.domain.DeskProperty;
import com.desk.market.repository.DeskPropertyRepository;
import com.desk.market.service.dto.DeskPropertyDTO;
import com.desk.market.service.mapper.DeskPropertyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link DeskProperty}.
 */
@Service
@Transactional
public class DeskPropertyServiceImpl implements DeskPropertyService {

    private final Logger log = LoggerFactory.getLogger(DeskPropertyServiceImpl.class);

    private final DeskPropertyRepository deskPropertyRepository;

    private final DeskPropertyMapper deskPropertyMapper;

    public DeskPropertyServiceImpl(DeskPropertyRepository deskPropertyRepository, DeskPropertyMapper deskPropertyMapper) {
        this.deskPropertyRepository = deskPropertyRepository;
        this.deskPropertyMapper = deskPropertyMapper;
    }

    /**
     * Save a deskProperty.
     *
     * @param deskPropertyDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DeskPropertyDTO save(DeskPropertyDTO deskPropertyDTO) {
        log.debug("Request to save DeskProperty : {}", deskPropertyDTO);
        DeskProperty deskProperty = deskPropertyMapper.toEntity(deskPropertyDTO);
        deskProperty = deskPropertyRepository.save(deskProperty);
        return deskPropertyMapper.toDto(deskProperty);
    }

    /**
     * Get all the deskProperties.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DeskPropertyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DeskProperties");
        return deskPropertyRepository.findAll(pageable)
            .map(deskPropertyMapper::toDto);
    }

    /**
     * Get one deskProperty by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DeskPropertyDTO> findOne(Long id) {
        log.debug("Request to get DeskProperty : {}", id);
        return deskPropertyRepository.findById(id)
            .map(deskPropertyMapper::toDto);
    }

    /**
     * Delete the deskProperty by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DeskProperty : {}", id);
        deskPropertyRepository.deleteById(id);
    }
}
