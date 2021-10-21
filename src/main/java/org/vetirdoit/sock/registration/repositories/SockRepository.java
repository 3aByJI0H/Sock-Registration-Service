package org.vetirdoit.sock.registration.repositories;

import org.springframework.stereotype.Repository;
import org.vetirdoit.sock.registration.domain.entities.QSockType;
import org.vetirdoit.sock.registration.domain.entities.SockType;

@Repository
public interface SockRepository extends JpaWithQueryDslRepository<SockType, QSockType, Long>,
    CustomSockTypeQueryDslRepository {

}
