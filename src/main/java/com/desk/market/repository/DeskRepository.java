package com.desk.market.repository;

import com.desk.market.domain.Desk;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Desk entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeskRepository extends JpaRepository<Desk, Long>, JpaSpecificationExecutor<Desk> {
}
