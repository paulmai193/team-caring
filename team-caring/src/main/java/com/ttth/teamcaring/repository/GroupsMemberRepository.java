package com.ttth.teamcaring.repository;

import com.ttth.teamcaring.domain.GroupsMember;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the GroupsMember entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GroupsMemberRepository extends JpaRepository<GroupsMember, Long> {
    @Query("select distinct groups_member from GroupsMember groups_member left join fetch groups_member.members")
    List<GroupsMember> findAllWithEagerRelationships();

    @Query("select groups_member from GroupsMember groups_member left join fetch groups_member.members where groups_member.id =:id")
    GroupsMember findOneWithEagerRelationships(@Param("id") Long id);

}
