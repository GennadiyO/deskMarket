package com.desk.market.repository;

import com.desk.market.domain.DeskProperty;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the DeskProperty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeskPropertyRepository extends JpaRepository<DeskProperty, Long>, JpaSpecificationExecutor<DeskProperty> {
}
