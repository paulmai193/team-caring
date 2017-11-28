package com.ttth.teamcaring.web.rest;

import com.ttth.teamcaring.TeamCaringApp;

import com.ttth.teamcaring.domain.Icon;
import com.ttth.teamcaring.repository.IconRepository;
import com.ttth.teamcaring.service.IconService;
import com.ttth.teamcaring.repository.search.IconSearchRepository;
import com.ttth.teamcaring.service.dto.IconDTO;
import com.ttth.teamcaring.service.mapper.IconMapper;
import com.ttth.teamcaring.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.ttth.teamcaring.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the IconResource REST controller.
 *
 * @see IconResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TeamCaringApp.class)
public class IconResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private IconRepository iconRepository;

    @Autowired
    private IconMapper iconMapper;

    @Autowired
    private IconService iconService;

    @Autowired
    private IconSearchRepository iconSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIconMockMvc;

    private Icon icon;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IconResource iconResource = new IconResource(iconService);
        this.restIconMockMvc = MockMvcBuilders.standaloneSetup(iconResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Icon createEntity(EntityManager em) {
        Icon icon = new Icon()
            .name(DEFAULT_NAME);
        return icon;
    }

    @Before
    public void initTest() {
        iconSearchRepository.deleteAll();
        icon = createEntity(em);
    }

    @Test
    @Transactional
    public void createIcon() throws Exception {
        int databaseSizeBeforeCreate = iconRepository.findAll().size();

        // Create the Icon
        IconDTO iconDTO = iconMapper.toDto(icon);
        restIconMockMvc.perform(post("/api/icons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iconDTO)))
            .andExpect(status().isCreated());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeCreate + 1);
        Icon testIcon = iconList.get(iconList.size() - 1);
        assertThat(testIcon.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Icon in Elasticsearch
        Icon iconEs = iconSearchRepository.findOne(testIcon.getId());
        assertThat(iconEs).isEqualToComparingFieldByField(testIcon);
    }

    @Test
    @Transactional
    public void createIconWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = iconRepository.findAll().size();

        // Create the Icon with an existing ID
        icon.setId(1L);
        IconDTO iconDTO = iconMapper.toDto(icon);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIconMockMvc.perform(post("/api/icons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iconDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllIcons() throws Exception {
        // Initialize the database
        iconRepository.saveAndFlush(icon);

        // Get all the iconList
        restIconMockMvc.perform(get("/api/icons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(icon.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getIcon() throws Exception {
        // Initialize the database
        iconRepository.saveAndFlush(icon);

        // Get the icon
        restIconMockMvc.perform(get("/api/icons/{id}", icon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(icon.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIcon() throws Exception {
        // Get the icon
        restIconMockMvc.perform(get("/api/icons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIcon() throws Exception {
        // Initialize the database
        iconRepository.saveAndFlush(icon);
        iconSearchRepository.save(icon);
        int databaseSizeBeforeUpdate = iconRepository.findAll().size();

        // Update the icon
        Icon updatedIcon = iconRepository.findOne(icon.getId());
        updatedIcon
            .name(UPDATED_NAME);
        IconDTO iconDTO = iconMapper.toDto(updatedIcon);

        restIconMockMvc.perform(put("/api/icons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iconDTO)))
            .andExpect(status().isOk());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeUpdate);
        Icon testIcon = iconList.get(iconList.size() - 1);
        assertThat(testIcon.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Icon in Elasticsearch
        Icon iconEs = iconSearchRepository.findOne(testIcon.getId());
        assertThat(iconEs).isEqualToComparingFieldByField(testIcon);
    }

    @Test
    @Transactional
    public void updateNonExistingIcon() throws Exception {
        int databaseSizeBeforeUpdate = iconRepository.findAll().size();

        // Create the Icon
        IconDTO iconDTO = iconMapper.toDto(icon);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIconMockMvc.perform(put("/api/icons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(iconDTO)))
            .andExpect(status().isCreated());

        // Validate the Icon in the database
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIcon() throws Exception {
        // Initialize the database
        iconRepository.saveAndFlush(icon);
        iconSearchRepository.save(icon);
        int databaseSizeBeforeDelete = iconRepository.findAll().size();

        // Get the icon
        restIconMockMvc.perform(delete("/api/icons/{id}", icon.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean iconExistsInEs = iconSearchRepository.exists(icon.getId());
        assertThat(iconExistsInEs).isFalse();

        // Validate the database is empty
        List<Icon> iconList = iconRepository.findAll();
        assertThat(iconList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchIcon() throws Exception {
        // Initialize the database
        iconRepository.saveAndFlush(icon);
        iconSearchRepository.save(icon);

        // Search the icon
        restIconMockMvc.perform(get("/api/_search/icons?query=id:" + icon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(icon.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Icon.class);
        Icon icon1 = new Icon();
        icon1.setId(1L);
        Icon icon2 = new Icon();
        icon2.setId(icon1.getId());
        assertThat(icon1).isEqualTo(icon2);
        icon2.setId(2L);
        assertThat(icon1).isNotEqualTo(icon2);
        icon1.setId(null);
        assertThat(icon1).isNotEqualTo(icon2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IconDTO.class);
        IconDTO iconDTO1 = new IconDTO();
        iconDTO1.setId(1L);
        IconDTO iconDTO2 = new IconDTO();
        assertThat(iconDTO1).isNotEqualTo(iconDTO2);
        iconDTO2.setId(iconDTO1.getId());
        assertThat(iconDTO1).isEqualTo(iconDTO2);
        iconDTO2.setId(2L);
        assertThat(iconDTO1).isNotEqualTo(iconDTO2);
        iconDTO1.setId(null);
        assertThat(iconDTO1).isNotEqualTo(iconDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(iconMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(iconMapper.fromId(null)).isNull();
    }
}
