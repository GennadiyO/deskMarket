package com.desk.market.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.desk.market.domain.DeskProperty;
import com.desk.market.domain.*; // for static metamodels
import com.desk.market.repository.DeskPropertyRepository;
import com.desk.market.service.dto.DeskPropertyCriteria;
import com.desk.market.service.dto.DeskPropertyDTO;
import com.desk.market.service.mapper.DeskPropertyMapper;

/**
 * Service for executing complex queries for {@link DeskProperty} entities in the database.
 * The main input is a {@link DeskPropertyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DeskPropertyDTO} or a {@link Page} of {@link DeskPropertyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DeskPropertyQueryService extends QueryService<DeskProperty> {

    private final Logger log = LoggerFactory.getLogger(DeskPropertyQueryService.class);

    private final DeskPropertyRepository deskPropertyRepository;

    private final DeskPropertyMapper deskPropertyMapper;

    public DeskPropertyQueryService(DeskPropertyRepository deskPropertyRepository, DeskPropertyMapper deskPropertyMapper) {
        this.deskPropertyRepository = deskPropertyRepository;
        this.deskPropertyMapper = deskPropertyMapper;
    }

    /**
     * Return a {@link List} of {@link DeskPropertyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DeskPropertyDTO> findByCriteria(DeskPropertyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DeskProperty> specification = createSpecification(criteria);
        return deskPropertyMapper.toDto(deskPropertyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DeskPropertyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DeskPropertyDTO> findByCriteria(DeskPropertyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DeskProperty> specification = createSpecification(criteria);
        return deskPropertyRepository.findAll(specification, page)
            .map(deskPropertyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DeskPropertyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DeskProperty> specification = createSpecification(criteria);
        return deskPropertyRepository.count(specification);
    }

    /**
     * Function to convert {@link DeskPropertyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DeskProperty> createSpecification(DeskPropertyCriteria criteria) {
        Specification<DeskProperty> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DeskProperty_.id));
            }
            if (criteria.getDeskPropertyId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeskPropertyId(), DeskProperty_.deskPropertyId));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), DeskProperty_.type));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildStringSpecification(criteria.getValue(), DeskProperty_.value));
            }
            if (criteria.getDeskId() != null) {
                specification = specification.and(buildSpecification(criteria.getDeskId(),
                    root -> root.join(DeskProperty_.desk, JoinType.LEFT).get(Desk_.id)));
            }
        }
        return specification;
    }
}
