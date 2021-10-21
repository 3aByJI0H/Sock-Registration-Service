package org.vetirdoit.sock.registration.repositories;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.vetirdoit.sock.registration.domain.entities.QSockType;
import org.vetirdoit.sock.registration.domain.entities.SockType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomSockTypeQueryDslRepositoryImpl implements CustomSockTypeQueryDslRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public long calcTotalQuantity(Predicate predicate) {

        JPAQuery<Integer> query = new JPAQuery<>(em);
        var sockType = QSockType.sockType;
        Integer totalQuantity = query.from(sockType)
                .where(predicate)
                .select(sockType.quantity.sum())
                .fetchOne();

        return (totalQuantity != null) ? totalQuantity : 0;
    }
}
