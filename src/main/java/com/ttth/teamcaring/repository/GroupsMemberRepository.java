package com.ttth.teamcaring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ttth.teamcaring.domain.GroupsMember;

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
