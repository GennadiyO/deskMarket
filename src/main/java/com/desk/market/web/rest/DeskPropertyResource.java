package com.desk.market.web.rest;

import com.desk.market.service.DeskPropertyService;
import com.desk.market.web.rest.errors.BadRequestAlertException;
import com.desk.market.service.dto.DeskPropertyDTO;
import com.desk.market.service.dto.DeskPropertyCriteria;
import com.desk.market.service.DeskPropertyQueryService;

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
 * REST controller for managing {@link com.desk.market.domain.DeskProperty}.
 */
@RestController
@RequestMapping("/api")
public class DeskPropertyResource {

    private final Logger log = LoggerFactory.getLogger(DeskPropertyResource.class);

    private static final String ENTITY_NAME = "deskProperty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeskPropertyService deskPropertyService;

    private final DeskPropertyQueryService deskPropertyQueryService;

    public DeskPropertyResource(DeskPropertyService deskPropertyService, DeskPropertyQueryService deskPropertyQueryService) {
        this.deskPropertyService = deskPropertyService;
        this.deskPropertyQueryService = deskPropertyQueryService;
    }

    /**
     * {@code POST  /desk-properties} : Create a new deskProperty.
     *
     * @param deskPropertyDTO the deskPropertyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deskPropertyDTO, or with status {@code 400 (Bad Request)} if the deskProperty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/desk-properties")
    public ResponseEntity<DeskPropertyDTO> createDeskProperty(@Valid @RequestBody DeskPropertyDTO deskPropertyDTO) throws URISyntaxException {
        log.debug("REST request to save DeskProperty : {}", deskPropertyDTO);
        if (deskPropertyDTO.getId() != null) {
            throw new BadRequestAlertException("A new deskProperty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeskPropertyDTO result = deskPropertyService.save(deskPropertyDTO);
        return ResponseEntity.created(new URI("/api/desk-properties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /desk-properties} : Updates an existing deskProperty.
     *
     * @param deskPropertyDTO the deskPropertyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deskPropertyDTO,
     * or with status {@code 400 (Bad Request)} if the deskPropertyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deskPropertyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/desk-properties")
    public ResponseEntity<DeskPropertyDTO> updateDeskProperty(@Valid @RequestBody DeskPropertyDTO deskPropertyDTO) throws URISyntaxException {
        log.debug("REST request to update DeskProperty : {}", deskPropertyDTO);
        if (deskPropertyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DeskPropertyDTO result = deskPropertyService.save(deskPropertyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deskPropertyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /desk-properties} : get all the deskProperties.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deskProperties in body.
     */
    @GetMapping("/desk-properties")
    public ResponseEntity<List<DeskPropertyDTO>> getAllDeskProperties(DeskPropertyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DeskProperties by criteria: {}", criteria);
        Page<DeskPropertyDTO> page = deskPropertyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /desk-properties/count} : count all the deskProperties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/desk-properties/count")
    public ResponseEntity<Long> countDeskProperties(DeskPropertyCriteria criteria) {
        log.debug("REST request to count DeskProperties by criteria: {}", criteria);
        return ResponseEntity.ok().body(deskPropertyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /desk-properties/:id} : get the "id" deskProperty.
     *
     * @param id the id of the deskPropertyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deskPropertyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/desk-properties/{id}")
    public ResponseEntity<DeskPropertyDTO> getDeskProperty(@PathVariable Long id) {
        log.debug("REST request to get DeskProperty : {}", id);
        Optional<DeskPropertyDTO> deskPropertyDTO = deskPropertyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deskPropertyDTO);
    }

    /**
     * {@code DELETE  /desk-properties/:id} : delete the "id" deskProperty.
     *
     * @param id the id of the deskPropertyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/desk-properties/{id}")
    public ResponseEntity<Void> deleteDeskProperty(@PathVariable Long id) {
        log.debug("REST request to delete DeskProperty : {}", id);
        deskPropertyService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
