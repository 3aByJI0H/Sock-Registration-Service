package org.vetirdoit.sock.registration.services;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.NumberPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vetirdoit.sock.registration.domain.Color;
import org.vetirdoit.sock.registration.domain.entities.QSockType;
import org.vetirdoit.sock.registration.domain.entities.SockType;
import org.vetirdoit.sock.registration.repositories.SockRepository;
import org.vetirdoit.sock.registration.services.exceptions.InvalidOperationException;


@Service
public class SockRegistrationService {

    private final SockRepository sockRepository;

    @Autowired
    public SockRegistrationService(SockRepository sockRepository) {
        this.sockRepository = sockRepository;
    }

    @Transactional(readOnly = true)
    public long countRequiredSocks(Color color, BiPredicate operation, int cottonPartValue) {
        Predicate predicate = QSockType.sockType.color.eq(color)
                .and( operation.eval(QSockType.sockType.cottonPart, cottonPartValue) );
        return sockRepository.calcTotalQuantity(predicate);
    }
    @Transactional
    public void registerOutgoingSocks(SockType outgoingSockType) throws InvalidOperationException {

        SockType sockType = sockRepository.findOne(isEqualTo(outgoingSockType))
                .orElseThrow( () -> new InvalidOperationException("No such sock type!") );

        int newQuantity = sockType.getQuantity() - outgoingSockType.getQuantity();
        if (newQuantity < 0) {
            throw new InvalidOperationException("You have registered too many socks!");
        } else if (newQuantity == 0) {
            sockRepository.delete(sockType);
        } else {
            sockType.setQuantity( newQuantity );
            sockRepository.save(sockType);
        }
    }

    @Transactional
    public void registerIncomingSocks(SockType incomingSockType) {

        sockRepository.findOne(isEqualTo(incomingSockType)).ifPresentOrElse(
                        sockType -> {
                            sockType.setQuantity( sockType.getQuantity() + incomingSockType.getQuantity() );
                            sockRepository.save(sockType);
                            },
                        () -> sockRepository.save( incomingSockType )
        );
    }

    public Predicate isEqualTo (SockType sockType) {
        return QSockType.sockType.color.eq(sockType.getColor())
                .and( QSockType.sockType.cottonPart.eq(sockType.getCottonPart()) );
    }

    public enum BiPredicate {
        MORE_THAN{
            @Override
            public <T extends Number & Comparable<?>> Predicate eval(NumberPath<T> field, T value) {
                return field.gt(value);
            }
        },
        LESS_THAN{
            @Override
            public <T extends Number & Comparable<?>> Predicate eval(NumberPath<T> field, T value) {
                return field.lt(value);
            }
        },
        EQUAL{
            @Override
            public <T extends Number & Comparable<?>> Predicate eval(NumberPath<T> field, T value) {
                return field.eq(value);
            }
        },
        /**/;

        public abstract  <T extends Number & Comparable<?>> Predicate eval(NumberPath<T> field, T value);
    }
}
