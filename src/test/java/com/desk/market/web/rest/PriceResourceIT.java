package com.desk.market.web.rest;

import com.desk.market.DeskMarketApp;
import com.desk.market.domain.Price;
import com.desk.market.domain.Desk;
import com.desk.market.repository.PriceRepository;
import com.desk.market.service.PriceService;
import com.desk.market.service.dto.PriceDTO;
import com.desk.market.service.mapper.PriceMapper;
import com.desk.market.service.dto.PriceCriteria;
import com.desk.market.service.PriceQueryService;

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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PriceResource} REST controller.
 */
@SpringBootTest(classes = DeskMarketApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PriceResourceIT {

    private static final Long DEFAULT_PRICE_ID = 1L;
    private static final Long UPDATED_PRICE_ID = 2L;
    private static final Long SMALLER_PRICE_ID = 1L - 1L;

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private PriceMapper priceMapper;

    @Autowired
    private PriceService priceService;

    @Autowired
    private PriceQueryService priceQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPriceMockMvc;

    private Price price;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Price createEntity(EntityManager em) {
        Price price = new Price()
            .priceId(DEFAULT_PRICE_ID)
            .creationDate(DEFAULT_CREATION_DATE)
            .price(DEFAULT_PRICE);
        // Add required entity
        Desk desk;
        if (TestUtil.findAll(em, Desk.class).isEmpty()) {
            desk = DeskResourceIT.createEntity(em);
            em.persist(desk);
            em.flush();
        } else {
            desk = TestUtil.findAll(em, Desk.class).get(0);
        }
        price.setDesk(desk);
        return price;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Price createUpdatedEntity(EntityManager em) {
        Price price = new Price()
            .priceId(UPDATED_PRICE_ID)
            .creationDate(UPDATED_CREATION_DATE)
            .price(UPDATED_PRICE);
        // Add required entity
        Desk desk;
        if (TestUtil.findAll(em, Desk.class).isEmpty()) {
            desk = DeskResourceIT.createUpdatedEntity(em);
            em.persist(desk);
            em.flush();
        } else {
            desk = TestUtil.findAll(em, Desk.class).get(0);
        }
        price.setDesk(desk);
        return price;
    }

    @BeforeEach
    public void initTest() {
        price = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrice() throws Exception {
        int databaseSizeBeforeCreate = priceRepository.findAll().size();

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);
        restPriceMockMvc.perform(post("/api/prices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(priceDTO)))
            .andExpect(status().isCreated());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeCreate + 1);
        Price testPrice = priceList.get(priceList.size() - 1);
        assertThat(testPrice.getPriceId()).isEqualTo(DEFAULT_PRICE_ID);
        assertThat(testPrice.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPrice.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createPriceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = priceRepository.findAll().size();

        // Create the Price with an existing ID
        price.setId(1L);
        PriceDTO priceDTO = priceMapper.toDto(price);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriceMockMvc.perform(post("/api/prices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(priceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPriceIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRepository.findAll().size();
        // set the field null
        price.setPriceId(null);

        // Create the Price, which fails.
        PriceDTO priceDTO = priceMapper.toDto(price);

        restPriceMockMvc.perform(post("/api/prices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(priceDTO)))
            .andExpect(status().isBadRequest());

        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRepository.findAll().size();
        // set the field null
        price.setCreationDate(null);

        // Create the Price, which fails.
        PriceDTO priceDTO = priceMapper.toDto(price);

        restPriceMockMvc.perform(post("/api/prices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(priceDTO)))
            .andExpect(status().isBadRequest());

        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = priceRepository.findAll().size();
        // set the field null
        price.setPrice(null);

        // Create the Price, which fails.
        PriceDTO priceDTO = priceMapper.toDto(price);

        restPriceMockMvc.perform(post("/api/prices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(priceDTO)))
            .andExpect(status().isBadRequest());

        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrices() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList
        restPriceMockMvc.perform(get("/api/prices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(price.getId().intValue())))
            .andExpect(jsonPath("$.[*].priceId").value(hasItem(DEFAULT_PRICE_ID.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }
    
    @Test
    @Transactional
    public void getPrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get the price
        restPriceMockMvc.perform(get("/api/prices/{id}", price.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(price.getId().intValue()))
            .andExpect(jsonPath("$.priceId").value(DEFAULT_PRICE_ID.intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }


    @Test
    @Transactional
    public void getPricesByIdFiltering() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        Long id = price.getId();

        defaultPriceShouldBeFound("id.equals=" + id);
        defaultPriceShouldNotBeFound("id.notEquals=" + id);

        defaultPriceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPriceShouldNotBeFound("id.greaterThan=" + id);

        defaultPriceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPriceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPricesByPriceIdIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where priceId equals to DEFAULT_PRICE_ID
        defaultPriceShouldBeFound("priceId.equals=" + DEFAULT_PRICE_ID);

        // Get all the priceList where priceId equals to UPDATED_PRICE_ID
        defaultPriceShouldNotBeFound("priceId.equals=" + UPDATED_PRICE_ID);
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where priceId not equals to DEFAULT_PRICE_ID
        defaultPriceShouldNotBeFound("priceId.notEquals=" + DEFAULT_PRICE_ID);

        // Get all the priceList where priceId not equals to UPDATED_PRICE_ID
        defaultPriceShouldBeFound("priceId.notEquals=" + UPDATED_PRICE_ID);
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIdIsInShouldWork() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where priceId in DEFAULT_PRICE_ID or UPDATED_PRICE_ID
        defaultPriceShouldBeFound("priceId.in=" + DEFAULT_PRICE_ID + "," + UPDATED_PRICE_ID);

        // Get all the priceList where priceId equals to UPDATED_PRICE_ID
        defaultPriceShouldNotBeFound("priceId.in=" + UPDATED_PRICE_ID);
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where priceId is not null
        defaultPriceShouldBeFound("priceId.specified=true");

        // Get all the priceList where priceId is null
        defaultPriceShouldNotBeFound("priceId.specified=false");
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where priceId is greater than or equal to DEFAULT_PRICE_ID
        defaultPriceShouldBeFound("priceId.greaterThanOrEqual=" + DEFAULT_PRICE_ID);

        // Get all the priceList where priceId is greater than or equal to UPDATED_PRICE_ID
        defaultPriceShouldNotBeFound("priceId.greaterThanOrEqual=" + UPDATED_PRICE_ID);
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where priceId is less than or equal to DEFAULT_PRICE_ID
        defaultPriceShouldBeFound("priceId.lessThanOrEqual=" + DEFAULT_PRICE_ID);

        // Get all the priceList where priceId is less than or equal to SMALLER_PRICE_ID
        defaultPriceShouldNotBeFound("priceId.lessThanOrEqual=" + SMALLER_PRICE_ID);
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIdIsLessThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where priceId is less than DEFAULT_PRICE_ID
        defaultPriceShouldNotBeFound("priceId.lessThan=" + DEFAULT_PRICE_ID);

        // Get all the priceList where priceId is less than UPDATED_PRICE_ID
        defaultPriceShouldBeFound("priceId.lessThan=" + UPDATED_PRICE_ID);
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where priceId is greater than DEFAULT_PRICE_ID
        defaultPriceShouldNotBeFound("priceId.greaterThan=" + DEFAULT_PRICE_ID);

        // Get all the priceList where priceId is greater than SMALLER_PRICE_ID
        defaultPriceShouldBeFound("priceId.greaterThan=" + SMALLER_PRICE_ID);
    }


    @Test
    @Transactional
    public void getAllPricesByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where creationDate equals to DEFAULT_CREATION_DATE
        defaultPriceShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the priceList where creationDate equals to UPDATED_CREATION_DATE
        defaultPriceShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPricesByCreationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where creationDate not equals to DEFAULT_CREATION_DATE
        defaultPriceShouldNotBeFound("creationDate.notEquals=" + DEFAULT_CREATION_DATE);

        // Get all the priceList where creationDate not equals to UPDATED_CREATION_DATE
        defaultPriceShouldBeFound("creationDate.notEquals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPricesByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultPriceShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the priceList where creationDate equals to UPDATED_CREATION_DATE
        defaultPriceShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPricesByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where creationDate is not null
        defaultPriceShouldBeFound("creationDate.specified=true");

        // Get all the priceList where creationDate is null
        defaultPriceShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price equals to DEFAULT_PRICE
        defaultPriceShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the priceList where price equals to UPDATED_PRICE
        defaultPriceShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price not equals to DEFAULT_PRICE
        defaultPriceShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the priceList where price not equals to UPDATED_PRICE
        defaultPriceShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultPriceShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the priceList where price equals to UPDATED_PRICE
        defaultPriceShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price is not null
        defaultPriceShouldBeFound("price.specified=true");

        // Get all the priceList where price is null
        defaultPriceShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price is greater than or equal to DEFAULT_PRICE
        defaultPriceShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the priceList where price is greater than or equal to UPDATED_PRICE
        defaultPriceShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price is less than or equal to DEFAULT_PRICE
        defaultPriceShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the priceList where price is less than or equal to SMALLER_PRICE
        defaultPriceShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price is less than DEFAULT_PRICE
        defaultPriceShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the priceList where price is less than UPDATED_PRICE
        defaultPriceShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void getAllPricesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        // Get all the priceList where price is greater than DEFAULT_PRICE
        defaultPriceShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the priceList where price is greater than SMALLER_PRICE
        defaultPriceShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }


    @Test
    @Transactional
    public void getAllPricesByDeskIsEqualToSomething() throws Exception {
        // Get already existing entity
        Desk desk = price.getDesk();
        priceRepository.saveAndFlush(price);
        Long deskId = desk.getId();

        // Get all the priceList where desk equals to deskId
        defaultPriceShouldBeFound("deskId.equals=" + deskId);

        // Get all the priceList where desk equals to deskId + 1
        defaultPriceShouldNotBeFound("deskId.equals=" + (deskId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPriceShouldBeFound(String filter) throws Exception {
        restPriceMockMvc.perform(get("/api/prices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(price.getId().intValue())))
            .andExpect(jsonPath("$.[*].priceId").value(hasItem(DEFAULT_PRICE_ID.intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));

        // Check, that the count call also returns 1
        restPriceMockMvc.perform(get("/api/prices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPriceShouldNotBeFound(String filter) throws Exception {
        restPriceMockMvc.perform(get("/api/prices?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPriceMockMvc.perform(get("/api/prices/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPrice() throws Exception {
        // Get the price
        restPriceMockMvc.perform(get("/api/prices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        int databaseSizeBeforeUpdate = priceRepository.findAll().size();

        // Update the price
        Price updatedPrice = priceRepository.findById(price.getId()).get();
        // Disconnect from session so that the updates on updatedPrice are not directly saved in db
        em.detach(updatedPrice);
        updatedPrice
            .priceId(UPDATED_PRICE_ID)
            .creationDate(UPDATED_CREATION_DATE)
            .price(UPDATED_PRICE);
        PriceDTO priceDTO = priceMapper.toDto(updatedPrice);

        restPriceMockMvc.perform(put("/api/prices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(priceDTO)))
            .andExpect(status().isOk());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
        Price testPrice = priceList.get(priceList.size() - 1);
        assertThat(testPrice.getPriceId()).isEqualTo(UPDATED_PRICE_ID);
        assertThat(testPrice.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPrice.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingPrice() throws Exception {
        int databaseSizeBeforeUpdate = priceRepository.findAll().size();

        // Create the Price
        PriceDTO priceDTO = priceMapper.toDto(price);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceMockMvc.perform(put("/api/prices")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(priceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Price in the database
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePrice() throws Exception {
        // Initialize the database
        priceRepository.saveAndFlush(price);

        int databaseSizeBeforeDelete = priceRepository.findAll().size();

        // Delete the price
        restPriceMockMvc.perform(delete("/api/prices/{id}", price.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Price> priceList = priceRepository.findAll();
        assertThat(priceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
