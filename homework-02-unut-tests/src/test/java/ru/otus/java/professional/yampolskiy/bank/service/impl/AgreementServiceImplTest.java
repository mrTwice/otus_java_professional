package ru.otus.java.professional.yampolskiy.bank.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.java.professional.yampolskiy.bank.dao.AgreementDao;
import ru.otus.java.professional.yampolskiy.bank.entity.Agreement;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgreementServiceImplTest {

    @Mock
    private AgreementDao agreementDao;

    @InjectMocks
    private AgreementServiceImpl agreementService;

    @ParameterizedTest
    @MethodSource("provideParametersForAddAgreementsMethod")
    void testAddAgreement(String agreementName, Agreement expectedAgreement) {
        when(agreementDao.save(any(Agreement.class))).thenReturn(expectedAgreement);

        Agreement savedAgreement = agreementService.addAgreement(agreementName);

        assertEquals(expectedAgreement.getName(), savedAgreement.getName());
        verify(agreementDao, times(1)).save(any(Agreement.class));
    }

    @ParameterizedTest
    @MethodSource("provideParametersForFindByNamesMethod")
    void testFindByName(String agreementName, Optional<Agreement> expectedResult) {
        when(agreementDao.findByName(agreementName)).thenReturn(expectedResult);

        Optional<Agreement> foundAgreement = agreementService.findByName(agreementName);

        assertEquals(expectedResult, foundAgreement);
        verify(agreementDao, times(1)).findByName(agreementName);
    }

    private static Stream<Arguments> provideParametersForAddAgreementsMethod() {
        Agreement agreement1 = new Agreement();
        agreement1.setName("Test Agreement 1");

        Agreement agreement2 = new Agreement();
        agreement2.setName("Test Agreement 2");

        return Stream.of(
                Arguments.of("Test Agreement 1", agreement1),
                Arguments.of("Test Agreement 2", agreement2)
        );
    }

    private static Stream<Arguments> provideParametersForFindByNamesMethod() {
        Agreement agreement = new Agreement();
        agreement.setName("Test Agreement");

        return Stream.of(
                Arguments.of("Test Agreement", Optional.of(agreement)),
                Arguments.of("Non-existing Agreement", Optional.empty())
        );
    }
}