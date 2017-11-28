package com.ttth.teamcaring.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Optional;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ttth.teamcaring.domain.CustomUser;
import com.ttth.teamcaring.domain.User;
import com.ttth.teamcaring.repository.CustomUserRepository;
import com.ttth.teamcaring.repository.UserRepository;
import com.ttth.teamcaring.repository.search.CustomUserSearchRepository;
import com.ttth.teamcaring.security.SecurityUtils;
import com.ttth.teamcaring.service.CustomUserService;
import com.ttth.teamcaring.service.dto.CustomUserDTO;
import com.ttth.teamcaring.service.mapper.CustomUserMapper;

/**
 * Service Implementation for managing CustomUser.
 *
 * @author Dai Mai
 */
@Service
@Transactional
public class CustomUserServiceImpl implements CustomUserService{

    /** The log. */
    private final Logger log = LoggerFactory.getLogger(CustomUserServiceImpl.class);

    /** The custom user repository. */
    private final CustomUserRepository customUserRepository;

    /** The custom user mapper. */
    private final CustomUserMapper customUserMapper;

    /** The custom user search repository. */
    private final CustomUserSearchRepository customUserSearchRepository;
    
    /** The user repository. */
    @Inject
    private UserRepository userRepository;

    /**
     * Instantiates a new custom user service impl.
     *
     * @param customUserRepository the custom user repository
     * @param customUserMapper the custom user mapper
     * @param customUserSearchRepository the custom user search repository
     */
    public CustomUserServiceImpl(CustomUserRepository customUserRepository, CustomUserMapper customUserMapper, CustomUserSearchRepository customUserSearchRepository) {
        this.customUserRepository = customUserRepository;
        this.customUserMapper = customUserMapper;
        this.customUserSearchRepository = customUserSearchRepository;
    }

    /**
     * Save a customUser.
     *
     * @param customUserDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CustomUserDTO save(CustomUserDTO customUserDTO) {
        log.debug("Request to save CustomUser : {}", customUserDTO);
        CustomUser customUser = customUserMapper.toEntity(customUserDTO);
        customUser = customUserRepository.save(customUser);
        CustomUserDTO result = customUserMapper.toDto(customUser);
        customUserSearchRepository.save(customUser);
        return result;
    }

    /**
     *  Get all the customUsers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomUserDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomUsers");
        return customUserRepository.findAll(pageable)
            .map(customUserMapper::toDto);
    }    

    /* (non-Javadoc)
	 * @see com.ttth.teamcaring.service.CustomUserService#findOneByCurrentAuthorize()
	 */
	@Override
	public Optional<CustomUserDTO> findOneByCurrentAuthorize() {
		String login = SecurityUtils.getCurrentUserLogin();
		return this.findOneByUserLogin(login);
	}

	/* (non-Javadoc)
	 * @see com.ttth.teamcaring.service.CustomUserService#findOneByUserLogin(java.lang.String)
	 */
	@Override
	public Optional<CustomUserDTO> findOneByUserLogin(String login) {
		Optional<User> optUser = this.userRepository.findOneByLogin(login);
		if (optUser.isPresent()) {
			return this.findOneByUserId(optUser.get().getId());
		} else {
			return Optional.empty();
		}
	}

	/* (non-Javadoc)
	 * @see com.ttth.teamcaring.service.CustomUserService#findOneByUserId(java.lang.Long)
	 */
	@Override
	public Optional<CustomUserDTO> findOneByUserId(Long userId) {
		Optional<CustomUser> optCustomUser = this.customUserRepository.findOneByUserId(userId);
		if (optCustomUser.isPresent()) {
			return Optional.ofNullable(customUserMapper.toDto(optCustomUser.get()));
		} else {
			return Optional.empty();
		}		
	}

	/**
     *  Get one customUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CustomUserDTO findOne(Long id) {
        log.debug("Request to get CustomUser : {}", id);
        CustomUser customUser = customUserRepository.findOne(id);
        return customUserMapper.toDto(customUser);
    }

    /**
     *  Delete the  customUser by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomUser : {}", id);
        customUserRepository.delete(id);
        customUserSearchRepository.delete(id);
    }

    /**
     * Search for the customUser corresponding to the query.
     *
     *  @param query the query of the search
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomUserDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomUsers for query {}", query);
        Page<CustomUser> result = customUserSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(customUserMapper::toDto);
    }
}
