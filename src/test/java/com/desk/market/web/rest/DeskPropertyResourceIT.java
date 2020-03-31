package com.desk.market.web.rest;

import com.desk.market.DeskMarketApp;
import com.desk.market.domain.DeskProperty;
import com.desk.market.domain.Desk;
import com.desk.market.repository.DeskPropertyRepository;
import com.desk.market.service.DeskPropertyService;
import com.desk.market.service.dto.DeskPropertyDTO;
import com.desk.market.service.mapper.DeskPropertyMapper;
import com.desk.market.service.dto.DeskPropertyCriteria;
import com.desk.market.service.DeskPropertyQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.desk.market.domain.enumeration.DeskPropertyType;
/**
 * Integration tests for the {@link DeskPropertyResource} REST controller.
 */
@SpringBootTest(classes = DeskMarketApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class DeskPropertyResourceIT {

    private static final Long DEFAULT_DESK_PROPERTY_ID = 1L;
    private static final Long UPDATED_DESK_PROPERTY_ID = 2L;
    private static final Long SMALLER_DESK_PROPERTY_ID = 1L - 1L;

    private static final DeskPropertyType DEFAULT_TYPE = DeskPropertyType.LENGTH;
    private static final DeskPropertyType UPDATED_TYPE = DeskPropertyType.WIDTH;

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private DeskPropertyRepository deskPropertyRepository;

    @Autowired
    private DeskPropertyMapper deskPropertyMapper;

    @Autowired
    private DeskPropertyService deskPropertyService;

    @Autowired
    private DeskPropertyQueryService deskPropertyQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeskPropertyMockMvc;

    private DeskProperty deskProperty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeskProperty createEntity(EntityManager em) {
        DeskProperty deskProperty = new DeskProperty()
            .deskPropertyId(DEFAULT_DESK_PROPERTY_ID)
            .type(DEFAULT_TYPE)
            .value(DEFAULT_VALUE);
        // Add required entity
        Desk desk;
        if (TestUtil.findAll(em, Desk.class).isEmpty()) {
            desk = DeskResourceIT.createEntity(em);
            em.persist(desk);
            em.flush();
        } else {
            desk = TestUtil.findAll(em, Desk.class).get(0);
        }
        deskProperty.setDesk(desk);
        return deskProperty;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeskProperty createUpdatedEntity(EntityManager em) {
        DeskProperty deskProperty = new DeskProperty()
            .deskPropertyId(UPDATED_DESK_PROPERTY_ID)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE);
        // Add required entity
        Desk desk;
        if (TestUtil.findAll(em, Desk.class).isEmpty()) {
            desk = DeskResourceIT.createUpdatedEntity(em);
            em.persist(desk);
            em.flush();
        } else {
            desk = TestUtil.findAll(em, Desk.class).get(0);
        }
        deskProperty.setDesk(desk);
        return deskProperty;
    }

    @BeforeEach
    public void initTest() {
        deskProperty = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeskProperty() throws Exception {
        int databaseSizeBeforeCreate = deskPropertyRepository.findAll().size();

        // Create the DeskProperty
        DeskPropertyDTO deskPropertyDTO = deskPropertyMapper.toDto(deskProperty);
        restDeskPropertyMockMvc.perform(post("/api/desk-properties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deskPropertyDTO)))
            .andExpect(status().isCreated());

        // Validate the DeskProperty in the database
        List<DeskProperty> deskPropertyList = deskPropertyRepository.findAll();
        assertThat(deskPropertyList).hasSize(databaseSizeBeforeCreate + 1);
        DeskProperty testDeskProperty = deskPropertyList.get(deskPropertyList.size() - 1);
        assertThat(testDeskProperty.getDeskPropertyId()).isEqualTo(DEFAULT_DESK_PROPERTY_ID);
        assertThat(testDeskProperty.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDeskProperty.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createDeskPropertyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deskPropertyRepository.findAll().size();

        // Create the DeskProperty with an existing ID
        deskProperty.setId(1L);
        DeskPropertyDTO deskPropertyDTO = deskPropertyMapper.toDto(deskProperty);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeskPropertyMockMvc.perform(post("/api/desk-properties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deskPropertyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DeskProperty in the database
        List<DeskProperty> deskPropertyList = deskPropertyRepository.findAll();
        assertThat(deskPropertyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDeskPropertyIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = deskPropertyRepository.findAll().size();
        // set the field null
        deskProperty.setDeskPropertyId(null);

        // Create the DeskProperty, which fails.
        DeskPropertyDTO deskPropertyDTO = deskPropertyMapper.toDto(deskProperty);

        restDeskPropertyMockMvc.perform(post("/api/desk-properties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deskPropertyDTO)))
            .andExpect(status().isBadRequest());

        List<DeskProperty> deskPropertyList = deskPropertyRepository.findAll();
        assertThat(deskPropertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = deskPropertyRepository.findAll().size();
        // set the field null
        deskProperty.setType(null);

        // Create the DeskProperty, which fails.
        DeskPropertyDTO deskPropertyDTO = deskPropertyMapper.toDto(deskProperty);

        restDeskPropertyMockMvc.perform(post("/api/desk-properties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deskPropertyDTO)))
            .andExpect(status().isBadRequest());

        List<DeskProperty> deskPropertyList = deskPropertyRepository.findAll();
        assertThat(deskPropertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = deskPropertyRepository.findAll().size();
        // set the field null
        deskProperty.setValue(null);

        // Create the DeskProperty, which fails.
        DeskPropertyDTO deskPropertyDTO = deskPropertyMapper.toDto(deskProperty);

        restDeskPropertyMockMvc.perform(post("/api/desk-properties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deskPropertyDTO)))
            .andExpect(status().isBadRequest());

        List<DeskProperty> deskPropertyList = deskPropertyRepository.findAll();
        assertThat(deskPropertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDeskProperties() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList
        restDeskPropertyMockMvc.perform(get("/api/desk-properties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deskProperty.getId().intValue())))
            .andExpect(jsonPath("$.[*].deskPropertyId").value(hasItem(DEFAULT_DESK_PROPERTY_ID.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
    
    @Test
    @Transactional
    public void getDeskProperty() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get the deskProperty
        restDeskPropertyMockMvc.perform(get("/api/desk-properties/{id}", deskProperty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deskProperty.getId().intValue()))
            .andExpect(jsonPath("$.deskPropertyId").value(DEFAULT_DESK_PROPERTY_ID.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }


    @Test
    @Transactional
    public void getDeskPropertiesByIdFiltering() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        Long id = deskProperty.getId();

        defaultDeskPropertyShouldBeFound("id.equals=" + id);
        defaultDeskPropertyShouldNotBeFound("id.notEquals=" + id);

        defaultDeskPropertyShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDeskPropertyShouldNotBeFound("id.greaterThan=" + id);

        defaultDeskPropertyShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDeskPropertyShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDeskPropertiesByDeskPropertyIdIsEqualToSomething() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where deskPropertyId equals to DEFAULT_DESK_PROPERTY_ID
        defaultDeskPropertyShouldBeFound("deskPropertyId.equals=" + DEFAULT_DESK_PROPERTY_ID);

        // Get all the deskPropertyList where deskPropertyId equals to UPDATED_DESK_PROPERTY_ID
        defaultDeskPropertyShouldNotBeFound("deskPropertyId.equals=" + UPDATED_DESK_PROPERTY_ID);
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByDeskPropertyIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where deskPropertyId not equals to DEFAULT_DESK_PROPERTY_ID
        defaultDeskPropertyShouldNotBeFound("deskPropertyId.notEquals=" + DEFAULT_DESK_PROPERTY_ID);

        // Get all the deskPropertyList where deskPropertyId not equals to UPDATED_DESK_PROPERTY_ID
        defaultDeskPropertyShouldBeFound("deskPropertyId.notEquals=" + UPDATED_DESK_PROPERTY_ID);
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByDeskPropertyIdIsInShouldWork() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where deskPropertyId in DEFAULT_DESK_PROPERTY_ID or UPDATED_DESK_PROPERTY_ID
        defaultDeskPropertyShouldBeFound("deskPropertyId.in=" + DEFAULT_DESK_PROPERTY_ID + "," + UPDATED_DESK_PROPERTY_ID);

        // Get all the deskPropertyList where deskPropertyId equals to UPDATED_DESK_PROPERTY_ID
        defaultDeskPropertyShouldNotBeFound("deskPropertyId.in=" + UPDATED_DESK_PROPERTY_ID);
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByDeskPropertyIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where deskPropertyId is not null
        defaultDeskPropertyShouldBeFound("deskPropertyId.specified=true");

        // Get all the deskPropertyList where deskPropertyId is null
        defaultDeskPropertyShouldNotBeFound("deskPropertyId.specified=false");
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByDeskPropertyIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where deskPropertyId is greater than or equal to DEFAULT_DESK_PROPERTY_ID
        defaultDeskPropertyShouldBeFound("deskPropertyId.greaterThanOrEqual=" + DEFAULT_DESK_PROPERTY_ID);

        // Get all the deskPropertyList where deskPropertyId is greater than or equal to UPDATED_DESK_PROPERTY_ID
        defaultDeskPropertyShouldNotBeFound("deskPropertyId.greaterThanOrEqual=" + UPDATED_DESK_PROPERTY_ID);
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByDeskPropertyIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where deskPropertyId is less than or equal to DEFAULT_DESK_PROPERTY_ID
        defaultDeskPropertyShouldBeFound("deskPropertyId.lessThanOrEqual=" + DEFAULT_DESK_PROPERTY_ID);

        // Get all the deskPropertyList where deskPropertyId is less than or equal to SMALLER_DESK_PROPERTY_ID
        defaultDeskPropertyShouldNotBeFound("deskPropertyId.lessThanOrEqual=" + SMALLER_DESK_PROPERTY_ID);
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByDeskPropertyIdIsLessThanSomething() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where deskPropertyId is less than DEFAULT_DESK_PROPERTY_ID
        defaultDeskPropertyShouldNotBeFound("deskPropertyId.lessThan=" + DEFAULT_DESK_PROPERTY_ID);

        // Get all the deskPropertyList where deskPropertyId is less than UPDATED_DESK_PROPERTY_ID
        defaultDeskPropertyShouldBeFound("deskPropertyId.lessThan=" + UPDATED_DESK_PROPERTY_ID);
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByDeskPropertyIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where deskPropertyId is greater than DEFAULT_DESK_PROPERTY_ID
        defaultDeskPropertyShouldNotBeFound("deskPropertyId.greaterThan=" + DEFAULT_DESK_PROPERTY_ID);

        // Get all the deskPropertyList where deskPropertyId is greater than SMALLER_DESK_PROPERTY_ID
        defaultDeskPropertyShouldBeFound("deskPropertyId.greaterThan=" + SMALLER_DESK_PROPERTY_ID);
    }


    @Test
    @Transactional
    public void getAllDeskPropertiesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where type equals to DEFAULT_TYPE
        defaultDeskPropertyShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the deskPropertyList where type equals to UPDATED_TYPE
        defaultDeskPropertyShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where type not equals to DEFAULT_TYPE
        defaultDeskPropertyShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the deskPropertyList where type not equals to UPDATED_TYPE
        defaultDeskPropertyShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultDeskPropertyShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the deskPropertyList where type equals to UPDATED_TYPE
        defaultDeskPropertyShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where type is not null
        defaultDeskPropertyShouldBeFound("type.specified=true");

        // Get all the deskPropertyList where type is null
        defaultDeskPropertyShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where value equals to DEFAULT_VALUE
        defaultDeskPropertyShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the deskPropertyList where value equals to UPDATED_VALUE
        defaultDeskPropertyShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByValueIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where value not equals to DEFAULT_VALUE
        defaultDeskPropertyShouldNotBeFound("value.notEquals=" + DEFAULT_VALUE);

        // Get all the deskPropertyList where value not equals to UPDATED_VALUE
        defaultDeskPropertyShouldBeFound("value.notEquals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByValueIsInShouldWork() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultDeskPropertyShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the deskPropertyList where value equals to UPDATED_VALUE
        defaultDeskPropertyShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where value is not null
        defaultDeskPropertyShouldBeFound("value.specified=true");

        // Get all the deskPropertyList where value is null
        defaultDeskPropertyShouldNotBeFound("value.specified=false");
    }
                @Test
    @Transactional
    public void getAllDeskPropertiesByValueContainsSomething() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where value contains DEFAULT_VALUE
        defaultDeskPropertyShouldBeFound("value.contains=" + DEFAULT_VALUE);

        // Get all the deskPropertyList where value contains UPDATED_VALUE
        defaultDeskPropertyShouldNotBeFound("value.contains=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllDeskPropertiesByValueNotContainsSomething() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        // Get all the deskPropertyList where value does not contain DEFAULT_VALUE
        defaultDeskPropertyShouldNotBeFound("value.doesNotContain=" + DEFAULT_VALUE);

        // Get all the deskPropertyList where value does not contain UPDATED_VALUE
        defaultDeskPropertyShouldBeFound("value.doesNotContain=" + UPDATED_VALUE);
    }


    @Test
    @Transactional
    public void getAllDeskPropertiesByDeskIsEqualToSomething() throws Exception {
        // Get already existing entity
        Desk desk = deskProperty.getDesk();
        deskPropertyRepository.saveAndFlush(deskProperty);
        Long deskId = desk.getId();

        // Get all the deskPropertyList where desk equals to deskId
        defaultDeskPropertyShouldBeFound("deskId.equals=" + deskId);

        // Get all the deskPropertyList where desk equals to deskId + 1
        defaultDeskPropertyShouldNotBeFound("deskId.equals=" + (deskId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeskPropertyShouldBeFound(String filter) throws Exception {
        restDeskPropertyMockMvc.perform(get("/api/desk-properties?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deskProperty.getId().intValue())))
            .andExpect(jsonPath("$.[*].deskPropertyId").value(hasItem(DEFAULT_DESK_PROPERTY_ID.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));

        // Check, that the count call also returns 1
        restDeskPropertyMockMvc.perform(get("/api/desk-properties/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeskPropertyShouldNotBeFound(String filter) throws Exception {
        restDeskPropertyMockMvc.perform(get("/api/desk-properties?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeskPropertyMockMvc.perform(get("/api/desk-properties/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDeskProperty() throws Exception {
        // Get the deskProperty
        restDeskPropertyMockMvc.perform(get("/api/desk-properties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeskProperty() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        int databaseSizeBeforeUpdate = deskPropertyRepository.findAll().size();

        // Update the deskProperty
        DeskProperty updatedDeskProperty = deskPropertyRepository.findById(deskProperty.getId()).get();
        // Disconnect from session so that the updates on updatedDeskProperty are not directly saved in db
        em.detach(updatedDeskProperty);
        updatedDeskProperty
            .deskPropertyId(UPDATED_DESK_PROPERTY_ID)
            .type(UPDATED_TYPE)
            .value(UPDATED_VALUE);
        DeskPropertyDTO deskPropertyDTO = deskPropertyMapper.toDto(updatedDeskProperty);

        restDeskPropertyMockMvc.perform(put("/api/desk-properties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deskPropertyDTO)))
            .andExpect(status().isOk());

        // Validate the DeskProperty in the database
        List<DeskProperty> deskPropertyList = deskPropertyRepository.findAll();
        assertThat(deskPropertyList).hasSize(databaseSizeBeforeUpdate);
        DeskProperty testDeskProperty = deskPropertyList.get(deskPropertyList.size() - 1);
        assertThat(testDeskProperty.getDeskPropertyId()).isEqualTo(UPDATED_DESK_PROPERTY_ID);
        assertThat(testDeskProperty.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDeskProperty.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingDeskProperty() throws Exception {
        int databaseSizeBeforeUpdate = deskPropertyRepository.findAll().size();

        // Create the DeskProperty
        DeskPropertyDTO deskPropertyDTO = deskPropertyMapper.toDto(deskProperty);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeskPropertyMockMvc.perform(put("/api/desk-properties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deskPropertyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DeskProperty in the database
        List<DeskProperty> deskPropertyList = deskPropertyRepository.findAll();
        assertThat(deskPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDeskProperty() throws Exception {
        // Initialize the database
        deskPropertyRepository.saveAndFlush(deskProperty);

        int databaseSizeBeforeDelete = deskPropertyRepository.findAll().size();

        // Delete the deskProperty
        restDeskPropertyMockMvc.perform(delete("/api/desk-properties/{id}", deskProperty.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeskProperty> deskPropertyList = deskPropertyRepository.findAll();
        assertThat(deskPropertyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
