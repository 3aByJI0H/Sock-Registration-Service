package org.vetirdoit.sock.registration.repositories;

import com.querydsl.core.types.Predicate;

public interface CustomSockTypeQueryDslRepository {
    long calcTotalQuantity(Predicate predicate);
}
