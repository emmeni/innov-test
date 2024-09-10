package com.emmeni.innov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.emmeni.innov.domain.base.IPersistent;

@NoRepositoryBean
public interface PersistentRepository<P extends IPersistent> extends JpaRepository<P, Long>, JpaSpecificationExecutor<P> {

}
