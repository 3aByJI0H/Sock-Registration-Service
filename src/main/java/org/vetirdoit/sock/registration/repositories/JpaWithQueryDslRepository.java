package org.vetirdoit.sock.registration.repositories;

import com.querydsl.core.types.dsl.EntityPathBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface JpaWithQueryDslRepository<T, P extends EntityPathBase<T>, ID extends Serializable>
    extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T> {
}
