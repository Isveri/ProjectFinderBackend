package com.example.project.utils;

import com.example.project.domain.*;
import com.example.project.model.SearchCriteria;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class GroupRoomSpecification implements Specification<GroupRoom> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<GroupRoom> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.isTrue(root.get("open")));
        if(Objects.nonNull(criteria.getCategoryId())){
            Join<Category,GroupRoom> groupCategory = root.join("category");
            predicates.add(criteriaBuilder.equal(groupCategory.get("id"), criteria.getCategoryId()));
        }

        if(Objects.nonNull(criteria.getGameId())){
            Join<Game,GroupRoom> groupGame = root.join("game");
            predicates.add(criteriaBuilder.equal(groupGame.get("id"), criteria.getGameId()));
        }

        if(Objects.nonNull(criteria.getRoleId())){
            Join<TakenInGameRole, GroupRoom> groupInGameRoles = root.join("takenInGameRoles");
            Join<InGameRole,TakenInGameRole> inGameRole = groupInGameRoles.join("inGameRole");
            predicates.add(criteriaBuilder.notEqual(inGameRole.get("id"), criteria.getRoleId()));
        }

        if(Objects.nonNull(criteria.getCityName())){
            predicates.add(criteriaBuilder.equal(root.<String>get("city"), criteria.getCityName()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
