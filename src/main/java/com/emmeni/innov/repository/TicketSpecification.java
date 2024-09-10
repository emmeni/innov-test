package com.emmeni.innov.repository;

import org.springframework.data.jpa.domain.Specification;

import com.emmeni.innov.domain.Ticket;

public class TicketSpecification {

    public static Specification<Ticket> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join("user").get("id"), userId);
    }

}
