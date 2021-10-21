package org.vetirdoit.sock.registration.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.vetirdoit.sock.registration.domain.Color;
import org.vetirdoit.sock.registration.domain.entities.QSockType;
import org.vetirdoit.sock.registration.domain.entities.SockType;
import org.vetirdoit.sock.registration.services.SockRegistrationService;
import org.vetirdoit.sock.registration.services.SockRegistrationService.BiPredicate;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SockRepositoryTest {

    SockRepository sockRepository;
    SockRegistrationService sockRegistrationService;

    @Autowired
    public SockRepositoryTest(SockRepository sockRepository) {
        this.sockRepository = sockRepository;
        this.sockRegistrationService = new SockRegistrationService(sockRepository);
    }

    @Test
    @Sql(scripts = {"/sql/createSockTypes.sql"})
    void countSockTypesWhenCottonPartHasSomeRestriction() {

        var operations=   List.of(BiPredicate.MORE_THAN, BiPredicate.EQUAL, BiPredicate.EQUAL, BiPredicate.LESS_THAN);
        var compareValues = List.of(-1, -1, 100, 50);
        var actualValues = calcResults(operations, compareValues);
        var expectedValues = List.of(15L, 0L, 6L, 4L);
        assertThat(actualValues).isEqualTo(expectedValues);
    }

    private List<Long> calcResults(List<BiPredicate> operations, List<Integer> compareValues) {
        List<Long> results = new ArrayList<>();
        for (int i = 0; i < Math.min(operations.size(), compareValues.size()); i++) {
            results.add(
                    sockRegistrationService.countRequiredSocks(
                            Color.BLUE,
                            operations.get(i),
                            compareValues.get(i)
                    )
            );
        }
        return results;
    }

    @Test
    @Sql(scripts = {"/sql/createSockTypes.sql"})
    void updateEntityQuery() throws Exception {
        SockType sockType = sockRepository.findById(1L).orElseThrow(Exception::new);
        assertThat(sockType.getQuantity()).isEqualTo(1);
        sockType.setQuantity(100);
        sockRepository.save(sockType);
        SockType updatedSockType = sockRepository.findById(1L).orElseThrow(Exception::new);
        assertThat(sockType.getQuantity()).isEqualTo(100);
    }

    @Test
    void insertEntityQuery() throws Exception {
        SockType sockType = SockType.createSockType(Color.BLUE, 13, 10);
        sockRepository.save(sockType);
    }

}