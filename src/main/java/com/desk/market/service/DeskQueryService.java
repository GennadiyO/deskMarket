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

import com.desk.market.domain.Desk;
import com.desk.market.domain.*; // for static metamodels
import com.desk.market.repository.DeskRepository;
import com.desk.market.service.dto.DeskCriteria;
import com.desk.market.service.dto.DeskDTO;
import com.desk.market.service.mapper.DeskMapper;

/**
 * Service for executing complex queries for {@link Desk} entities in the database.
 * The main input is a {@link DeskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DeskDTO} or a {@link Page} of {@link DeskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DeskQueryService extends QueryService<Desk> {

    private final Logger log = LoggerFactory.getLogger(DeskQueryService.class);

    private final DeskRepository deskRepository;

    private final DeskMapper deskMapper;

    public DeskQueryService(DeskRepository deskRepository, DeskMapper deskMapper) {
        this.deskRepository = deskRepository;
        this.deskMapper = deskMapper;
    }

    /**
     * Return a {@link List} of {@link DeskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DeskDTO> findByCriteria(DeskCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Desk> specification = createSpecification(criteria);
        return deskMapper.toDto(deskRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DeskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DeskDTO> findByCriteria(DeskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Desk> specification = createSpecification(criteria);
        return deskRepository.findAll(specification, page)
            .map(deskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DeskCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Desk> specification = createSpecification(criteria);
        return deskRepository.count(specification);
    }

    /**
     * Function to convert {@link DeskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Desk> createSpecification(DeskCriteria criteria) {
        Specification<Desk> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Desk_.id));
            }
            if (criteria.getDeskId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDeskId(), Desk_.deskId));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Desk_.type));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Desk_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Desk_.description));
            }
            if (criteria.getCreationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreationDate(), Desk_.creationDate));
            }
            if (criteria.getModificationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModificationDate(), Desk_.modificationDate));
            }
            if (criteria.getPhotosId() != null) {
                specification = specification.and(buildSpecification(criteria.getPhotosId(),
                    root -> root.join(Desk_.photos, JoinType.LEFT).get(Photo_.id)));
            }
            if (criteria.getPricesId() != null) {
                specification = specification.and(buildSpecification(criteria.getPricesId(),
                    root -> root.join(Desk_.prices, JoinType.LEFT).get(Price_.id)));
            }
            if (criteria.getPropertiesId() != null) {
                specification = specification.and(buildSpecification(criteria.getPropertiesId(),
                    root -> root.join(Desk_.properties, JoinType.LEFT).get(DeskProperty_.id)));
            }
        }
        return specification;
    }
}
