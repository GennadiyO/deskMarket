package com.desk.market.web.rest;

import com.desk.market.service.DeskService;
import com.desk.market.web.rest.errors.BadRequestAlertException;
import com.desk.market.service.dto.DeskDTO;
import com.desk.market.service.dto.DeskCriteria;
import com.desk.market.service.DeskQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.desk.market.domain.Desk}.
 */
@RestController
@RequestMapping("/api")
public class DeskResource {

    private final Logger log = LoggerFactory.getLogger(DeskResource.class);

    private static final String ENTITY_NAME = "desk";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeskService deskService;

    private final DeskQueryService deskQueryService;

    public DeskResource(DeskService deskService, DeskQueryService deskQueryService) {
        this.deskService = deskService;
        this.deskQueryService = deskQueryService;
    }

    /**
     * {@code POST  /desks} : Create a new desk.
     *
     * @param deskDTO the deskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deskDTO, or with status {@code 400 (Bad Request)} if the desk has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/desks")
    public ResponseEntity<DeskDTO> createDesk(@Valid @RequestBody DeskDTO deskDTO) throws URISyntaxException {
        log.debug("REST request to save Desk : {}", deskDTO);
        if (deskDTO.getId() != null) {
            throw new BadRequestAlertException("A new desk cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeskDTO result = deskService.save(deskDTO);
        return ResponseEntity.created(new URI("/api/desks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /desks} : Updates an existing desk.
     *
     * @param deskDTO the deskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deskDTO,
     * or with status {@code 400 (Bad Request)} if the deskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/desks")
    public ResponseEntity<DeskDTO> updateDesk(@Valid @RequestBody DeskDTO deskDTO) throws URISyntaxException {
        log.debug("REST request to update Desk : {}", deskDTO);
        if (deskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DeskDTO result = deskService.save(deskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deskDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /desks} : get all the desks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of desks in body.
     */
    @GetMapping("/desks")
    public ResponseEntity<List<DeskDTO>> getAllDesks(DeskCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Desks by criteria: {}", criteria);
        Page<DeskDTO> page = deskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /desks/count} : count all the desks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/desks/count")
    public ResponseEntity<Long> countDesks(DeskCriteria criteria) {
        log.debug("REST request to count Desks by criteria: {}", criteria);
        return ResponseEntity.ok().body(deskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /desks/:id} : get the "id" desk.
     *
     * @param id the id of the deskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/desks/{id}")
    public ResponseEntity<DeskDTO> getDesk(@PathVariable Long id) {
        log.debug("REST request to get Desk : {}", id);
        Optional<DeskDTO> deskDTO = deskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deskDTO);
    }

    /**
     * {@code DELETE  /desks/:id} : delete the "id" desk.
     *
     * @param id the id of the deskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/desks/{id}")
    public ResponseEntity<Void> deleteDesk(@PathVariable Long id) {
        log.debug("REST request to delete Desk : {}", id);
        deskService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
