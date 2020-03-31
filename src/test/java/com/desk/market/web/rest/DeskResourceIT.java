package com.desk.market.web.rest;

import com.desk.market.DeskMarketApp;
import com.desk.market.domain.Desk;
import com.desk.market.domain.Photo;
import com.desk.market.domain.Price;
import com.desk.market.domain.DeskProperty;
import com.desk.market.repository.DeskRepository;
import com.desk.market.service.DeskService;
import com.desk.market.service.dto.DeskDTO;
import com.desk.market.service.mapper.DeskMapper;
import com.desk.market.service.dto.DeskCriteria;
import com.desk.market.service.DeskQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.desk.market.domain.enumeration.DeskType;
/**
 * Integration tests for the {@link DeskResource} REST controller.
 */
@SpringBootTest(classes = DeskMarketApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class DeskResourceIT {

    private static final Long DEFAULT_DESK_ID = 1L;
    private static final Long UPDATED_DESK_ID = 2L;
    private static final Long SMALLER_DESK_ID = 1L - 1L;

    private static final DeskType DEFAULT_TYPE = DeskType.LIFTING_DESK_OVERLAY;
    private static final DeskType UPDATED_TYPE = DeskType.LIFTING_DESK;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFICATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFICATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DeskRepository deskRepository;

    @Autowired
    private DeskMapper deskMapper;

    @Autowired
    private DeskService deskService;

    @Autowired
    private DeskQueryService deskQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeskMockMvc;

    private Desk desk;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Desk createEntity(EntityManager em) {
        Desk desk = new Desk()
            .deskId(DEFAULT_DESK_ID)
            .type(DEFAULT_TYPE)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE)
            .modificationDate(DEFAULT_MODIFICATION_DATE);
        return desk;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Desk createUpdatedEntity(EntityManager em) {
        Desk desk = new Desk()
            .deskId(UPDATED_DESK_ID)
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .modificationDate(UPDATED_MODIFICATION_DATE);
        return desk;
    }

    @BeforeEach
    public void initTest() {
        desk = createEntity(em);
    }

    @Test
    @Transactional
    public void createDesk() throws Exception {
        int databaseSizeBeforeCreate = deskRepository.findAll().size();

        // Create the Desk
        DeskDTO deskDTO = deskMapper.toDto(desk);
        restDeskMockMvc.perform(post("/api/desks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deskDTO)))
            .andExpect(status().isCreated());

        // Validate the Desk in the database
        List<Desk> deskList = deskRepository.findAll();
        assertThat(deskList).hasSize(databaseSizeBeforeCreate + 1);
        Desk testDesk = deskList.get(deskList.size() - 1);
        assertThat(testDesk.getDeskId()).isEqualTo(DEFAULT_DESK_ID);
        assertThat(testDesk.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDesk.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDesk.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDesk.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testDesk.getModificationDate()).isEqualTo(DEFAULT_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    public void createDeskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deskRepository.findAll().size();

        // Create the Desk with an existing ID
        desk.setId(1L);
        DeskDTO deskDTO = deskMapper.toDto(desk);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeskMockMvc.perform(post("/api/desks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Desk in the database
        List<Desk> deskList = deskRepository.findAll();
        assertThat(deskList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDeskIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = deskRepository.findAll().size();
        // set the field null
        desk.setDeskId(null);

        // Create the Desk, which fails.
        DeskDTO deskDTO = deskMapper.toDto(desk);

        restDeskMockMvc.perform(post("/api/desks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deskDTO)))
            .andExpect(status().isBadRequest());

        List<Desk> deskList = deskRepository.findAll();
        assertThat(deskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = deskRepository.findAll().size();
        // set the field null
        desk.setName(null);

        // Create the Desk, which fails.
        DeskDTO deskDTO = deskMapper.toDto(desk);

        restDeskMockMvc.perform(post("/api/desks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deskDTO)))
            .andExpect(status().isBadRequest());

        List<Desk> deskList = deskRepository.findAll();
        assertThat(deskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = deskRepository.findAll().size();
        // set the field null
        desk.setCreationDate(null);

        // Create the Desk, which fails.
        DeskDTO deskDTO = deskMapper.toDto(desk);

        restDeskMockMvc.perform(post("/api/desks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deskDTO)))
            .andExpect(status().isBadRequest());

        List<Desk> deskList = deskRepository.findAll();
        assertThat(deskList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDesks() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList
        restDeskMockMvc.perform(get("/api/desks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(desk.getId().intValue())))
            .andExpect(jsonPath("$.[*].deskId").value(hasItem(DEFAULT_DESK_ID.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(DEFAULT_MODIFICATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getDesk() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get the desk
        restDeskMockMvc.perform(get("/api/desks/{id}", desk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(desk.getId().intValue()))
            .andExpect(jsonPath("$.deskId").value(DEFAULT_DESK_ID.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.modificationDate").value(DEFAULT_MODIFICATION_DATE.toString()));
    }


    @Test
    @Transactional
    public void getDesksByIdFiltering() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        Long id = desk.getId();

        defaultDeskShouldBeFound("id.equals=" + id);
        defaultDeskShouldNotBeFound("id.notEquals=" + id);

        defaultDeskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDeskShouldNotBeFound("id.greaterThan=" + id);

        defaultDeskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDeskShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDesksByDeskIdIsEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where deskId equals to DEFAULT_DESK_ID
        defaultDeskShouldBeFound("deskId.equals=" + DEFAULT_DESK_ID);

        // Get all the deskList where deskId equals to UPDATED_DESK_ID
        defaultDeskShouldNotBeFound("deskId.equals=" + UPDATED_DESK_ID);
    }

    @Test
    @Transactional
    public void getAllDesksByDeskIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where deskId not equals to DEFAULT_DESK_ID
        defaultDeskShouldNotBeFound("deskId.notEquals=" + DEFAULT_DESK_ID);

        // Get all the deskList where deskId not equals to UPDATED_DESK_ID
        defaultDeskShouldBeFound("deskId.notEquals=" + UPDATED_DESK_ID);
    }

    @Test
    @Transactional
    public void getAllDesksByDeskIdIsInShouldWork() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where deskId in DEFAULT_DESK_ID or UPDATED_DESK_ID
        defaultDeskShouldBeFound("deskId.in=" + DEFAULT_DESK_ID + "," + UPDATED_DESK_ID);

        // Get all the deskList where deskId equals to UPDATED_DESK_ID
        defaultDeskShouldNotBeFound("deskId.in=" + UPDATED_DESK_ID);
    }

    @Test
    @Transactional
    public void getAllDesksByDeskIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where deskId is not null
        defaultDeskShouldBeFound("deskId.specified=true");

        // Get all the deskList where deskId is null
        defaultDeskShouldNotBeFound("deskId.specified=false");
    }

    @Test
    @Transactional
    public void getAllDesksByDeskIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where deskId is greater than or equal to DEFAULT_DESK_ID
        defaultDeskShouldBeFound("deskId.greaterThanOrEqual=" + DEFAULT_DESK_ID);

        // Get all the deskList where deskId is greater than or equal to UPDATED_DESK_ID
        defaultDeskShouldNotBeFound("deskId.greaterThanOrEqual=" + UPDATED_DESK_ID);
    }

    @Test
    @Transactional
    public void getAllDesksByDeskIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where deskId is less than or equal to DEFAULT_DESK_ID
        defaultDeskShouldBeFound("deskId.lessThanOrEqual=" + DEFAULT_DESK_ID);

        // Get all the deskList where deskId is less than or equal to SMALLER_DESK_ID
        defaultDeskShouldNotBeFound("deskId.lessThanOrEqual=" + SMALLER_DESK_ID);
    }

    @Test
    @Transactional
    public void getAllDesksByDeskIdIsLessThanSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where deskId is less than DEFAULT_DESK_ID
        defaultDeskShouldNotBeFound("deskId.lessThan=" + DEFAULT_DESK_ID);

        // Get all the deskList where deskId is less than UPDATED_DESK_ID
        defaultDeskShouldBeFound("deskId.lessThan=" + UPDATED_DESK_ID);
    }

    @Test
    @Transactional
    public void getAllDesksByDeskIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where deskId is greater than DEFAULT_DESK_ID
        defaultDeskShouldNotBeFound("deskId.greaterThan=" + DEFAULT_DESK_ID);

        // Get all the deskList where deskId is greater than SMALLER_DESK_ID
        defaultDeskShouldBeFound("deskId.greaterThan=" + SMALLER_DESK_ID);
    }


    @Test
    @Transactional
    public void getAllDesksByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where type equals to DEFAULT_TYPE
        defaultDeskShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the deskList where type equals to UPDATED_TYPE
        defaultDeskShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDesksByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where type not equals to DEFAULT_TYPE
        defaultDeskShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the deskList where type not equals to UPDATED_TYPE
        defaultDeskShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDesksByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultDeskShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the deskList where type equals to UPDATED_TYPE
        defaultDeskShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDesksByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where type is not null
        defaultDeskShouldBeFound("type.specified=true");

        // Get all the deskList where type is null
        defaultDeskShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllDesksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where name equals to DEFAULT_NAME
        defaultDeskShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the deskList where name equals to UPDATED_NAME
        defaultDeskShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDesksByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where name not equals to DEFAULT_NAME
        defaultDeskShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the deskList where name not equals to UPDATED_NAME
        defaultDeskShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDesksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDeskShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the deskList where name equals to UPDATED_NAME
        defaultDeskShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDesksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where name is not null
        defaultDeskShouldBeFound("name.specified=true");

        // Get all the deskList where name is null
        defaultDeskShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllDesksByNameContainsSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where name contains DEFAULT_NAME
        defaultDeskShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the deskList where name contains UPDATED_NAME
        defaultDeskShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDesksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where name does not contain DEFAULT_NAME
        defaultDeskShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the deskList where name does not contain UPDATED_NAME
        defaultDeskShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllDesksByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where description equals to DEFAULT_DESCRIPTION
        defaultDeskShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the deskList where description equals to UPDATED_DESCRIPTION
        defaultDeskShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDesksByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where description not equals to DEFAULT_DESCRIPTION
        defaultDeskShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the deskList where description not equals to UPDATED_DESCRIPTION
        defaultDeskShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDesksByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDeskShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the deskList where description equals to UPDATED_DESCRIPTION
        defaultDeskShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDesksByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where description is not null
        defaultDeskShouldBeFound("description.specified=true");

        // Get all the deskList where description is null
        defaultDeskShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllDesksByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where description contains DEFAULT_DESCRIPTION
        defaultDeskShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the deskList where description contains UPDATED_DESCRIPTION
        defaultDeskShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllDesksByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where description does not contain DEFAULT_DESCRIPTION
        defaultDeskShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the deskList where description does not contain UPDATED_DESCRIPTION
        defaultDeskShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllDesksByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where creationDate equals to DEFAULT_CREATION_DATE
        defaultDeskShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the deskList where creationDate equals to UPDATED_CREATION_DATE
        defaultDeskShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllDesksByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultDeskShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the deskList where creationDate not equals to UPDATED_CREATION_DATE
        defaultDeskShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllDesksByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultDeskShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the deskList where creationDate equals to UPDATED_CREATION_DATE
        defaultDeskShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllDesksByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where creationDate is not null
        defaultDeskShouldBeFound("creationDate.specified=true");

        // Get all the deskList where creationDate is null
        defaultDeskShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDesksByModificationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where modificationDate equals to DEFAULT_MODIFICATION_DATE
        defaultDeskShouldBeFound("modificationDate.equals=" + DEFAULT_MODIFICATION_DATE);

        // Get all the deskList where modificationDate equals to UPDATED_MODIFICATION_DATE
        defaultDeskShouldNotBeFound("modificationDate.equals=" + UPDATED_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllDesksByModificationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where modificationDate not equals to DEFAULT_MODIFICATION_DATE
        defaultDeskShouldNotBeFound("modificationDate.notEquals=" + DEFAULT_MODIFICATION_DATE);

        // Get all the deskList where modificationDate not equals to UPDATED_MODIFICATION_DATE
        defaultDeskShouldBeFound("modificationDate.notEquals=" + UPDATED_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllDesksByModificationDateIsInShouldWork() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where modificationDate in DEFAULT_MODIFICATION_DATE or UPDATED_MODIFICATION_DATE
        defaultDeskShouldBeFound("modificationDate.in=" + DEFAULT_MODIFICATION_DATE + "," + UPDATED_MODIFICATION_DATE);

        // Get all the deskList where modificationDate equals to UPDATED_MODIFICATION_DATE
        defaultDeskShouldNotBeFound("modificationDate.in=" + UPDATED_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    public void getAllDesksByModificationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        // Get all the deskList where modificationDate is not null
        defaultDeskShouldBeFound("modificationDate.specified=true");

        // Get all the deskList where modificationDate is null
        defaultDeskShouldNotBeFound("modificationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDesksByPhotosIsEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);
        Photo photos = PhotoResourceIT.createEntity(em);
        em.persist(photos);
        em.flush();
        desk.addPhotos(photos);
        deskRepository.saveAndFlush(desk);
        Long photosId = photos.getId();

        // Get all the deskList where photos equals to photosId
        defaultDeskShouldBeFound("photosId.equals=" + photosId);

        // Get all the deskList where photos equals to photosId + 1
        defaultDeskShouldNotBeFound("photosId.equals=" + (photosId + 1));
    }


    @Test
    @Transactional
    public void getAllDesksByPricesIsEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);
        Price prices = PriceResourceIT.createEntity(em);
        em.persist(prices);
        em.flush();
        desk.addPrices(prices);
        deskRepository.saveAndFlush(desk);
        Long pricesId = prices.getId();

        // Get all the deskList where prices equals to pricesId
        defaultDeskShouldBeFound("pricesId.equals=" + pricesId);

        // Get all the deskList where prices equals to pricesId + 1
        defaultDeskShouldNotBeFound("pricesId.equals=" + (pricesId + 1));
    }


    @Test
    @Transactional
    public void getAllDesksByPropertiesIsEqualToSomething() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);
        DeskProperty properties = DeskPropertyResourceIT.createEntity(em);
        em.persist(properties);
        em.flush();
        desk.addProperties(properties);
        deskRepository.saveAndFlush(desk);
        Long propertiesId = properties.getId();

        // Get all the deskList where properties equals to propertiesId
        defaultDeskShouldBeFound("propertiesId.equals=" + propertiesId);

        // Get all the deskList where properties equals to propertiesId + 1
        defaultDeskShouldNotBeFound("propertiesId.equals=" + (propertiesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDeskShouldBeFound(String filter) throws Exception {
        restDeskMockMvc.perform(get("/api/desks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(desk.getId().intValue())))
            .andExpect(jsonPath("$.[*].deskId").value(hasItem(DEFAULT_DESK_ID.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].modificationDate").value(hasItem(DEFAULT_MODIFICATION_DATE.toString())));

        // Check, that the count call also returns 1
        restDeskMockMvc.perform(get("/api/desks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDeskShouldNotBeFound(String filter) throws Exception {
        restDeskMockMvc.perform(get("/api/desks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDeskMockMvc.perform(get("/api/desks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDesk() throws Exception {
        // Get the desk
        restDeskMockMvc.perform(get("/api/desks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDesk() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        int databaseSizeBeforeUpdate = deskRepository.findAll().size();

        // Update the desk
        Desk updatedDesk = deskRepository.findById(desk.getId()).get();
        // Disconnect from session so that the updates on updatedDesk are not directly saved in db
        em.detach(updatedDesk);
        updatedDesk
            .deskId(UPDATED_DESK_ID)
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .modificationDate(UPDATED_MODIFICATION_DATE);
        DeskDTO deskDTO = deskMapper.toDto(updatedDesk);

        restDeskMockMvc.perform(put("/api/desks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deskDTO)))
            .andExpect(status().isOk());

        // Validate the Desk in the database
        List<Desk> deskList = deskRepository.findAll();
        assertThat(deskList).hasSize(databaseSizeBeforeUpdate);
        Desk testDesk = deskList.get(deskList.size() - 1);
        assertThat(testDesk.getDeskId()).isEqualTo(UPDATED_DESK_ID);
        assertThat(testDesk.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDesk.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDesk.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDesk.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testDesk.getModificationDate()).isEqualTo(UPDATED_MODIFICATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingDesk() throws Exception {
        int databaseSizeBeforeUpdate = deskRepository.findAll().size();

        // Create the Desk
        DeskDTO deskDTO = deskMapper.toDto(desk);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeskMockMvc.perform(put("/api/desks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Desk in the database
        List<Desk> deskList = deskRepository.findAll();
        assertThat(deskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDesk() throws Exception {
        // Initialize the database
        deskRepository.saveAndFlush(desk);

        int databaseSizeBeforeDelete = deskRepository.findAll().size();

        // Delete the desk
        restDeskMockMvc.perform(delete("/api/desks/{id}", desk.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Desk> deskList = deskRepository.findAll();
        assertThat(deskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
