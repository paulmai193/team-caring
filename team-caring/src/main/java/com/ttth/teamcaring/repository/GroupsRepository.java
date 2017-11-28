package com.ttth.teamcaring.repository;

import com.ttth.teamcaring.domain.Groups;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Groups entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupsRepository extends JpaRepository<Groups, Long> {
    @Query("select distinct groups from Groups groups left join fetch groups.members")
    List<Groups> findAllWithEagerRelationships();

    @Query("select groups from Groups groups left join fetch groups.members where groups.id =:id")
    Groups findOneWithEagerRelationships(@Param("id") Long id);

}
