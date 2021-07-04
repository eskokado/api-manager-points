package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.builders.BankOfHourDTOBuilder;
import com.dio.santander.apimanagerpoints.dtos.BankOfHourDTO;
import com.dio.santander.apimanagerpoints.mappers.BankOfHourMapper;
import com.dio.santander.apimanagerpoints.models.BankOfHour;
import com.dio.santander.apimanagerpoints.repositories.BankOfHourRepository;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectAlreadyRegisteredException;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankOfHourServiceTest {
    private static final Long VALID_BANK_OF_HOUR_ID = 1L;
    private static final Long VALID_MOVEMENT_ID = 1L;
    private static final Long VALID_USER_ID = 1L;
    private static final Long INVALID_BANK_OF_HOUR_ID = 2L;
    private static final Long INVALID_MOVEMENT_ID = 2L;
    private static final Long INVALID_USER_ID = 2L;

    @Mock
    private BankOfHourRepository bankOfHourRepository;

    private BankOfHourMapper bankOfHourMapper = BankOfHourMapper.INSTANCE;

    @InjectMocks
    private BankOfHourService bankOfHourService;

    @Test
    void whenBankOfHourInformedIsNotExistingThenItShouldBeCreated() {
        // given
        BankOfHourDTO expectedBankOfHourDTO = BankOfHourDTOBuilder.builder().build().toBankOfHourDTO();
        BankOfHour expectedBankOfHour = bankOfHourMapper.toModel(expectedBankOfHourDTO);

        // when
        when(bankOfHourRepository.findByPK(VALID_BANK_OF_HOUR_ID, VALID_MOVEMENT_ID, VALID_USER_ID)).thenReturn(null);
        when(bankOfHourRepository.save(expectedBankOfHour)).thenReturn(expectedBankOfHour);

        // then
        BankOfHourDTO createBankOfHourDTO = bankOfHourService.insert(expectedBankOfHourDTO);

        assertThat(createBankOfHourDTO, is(equalTo(expectedBankOfHourDTO)));
    }

    @Test
    void whenAlreadyRegisteredBankOfHourInformedThenAnExceptionShouldBeThrown() {
        // given
        BankOfHourDTO expectedBankOfHourDTO = BankOfHourDTOBuilder.builder().build().toBankOfHourDTO();
        BankOfHour expectedBankOfHour = bankOfHourMapper.toModel(expectedBankOfHourDTO);

        // when
        when(bankOfHourRepository.findByPK(VALID_BANK_OF_HOUR_ID, VALID_MOVEMENT_ID, VALID_USER_ID)).thenReturn(expectedBankOfHour);

        // then
        assertThrows(ObjectAlreadyRegisteredException.class, () -> bankOfHourService.insert(expectedBankOfHourDTO));
    }

    @Test
    void whenValidBankOfHourIdIsGivenThenReturnAnBankOfHour() {
        // given
        BankOfHourDTO expectedBankOfHourDTO = BankOfHourDTOBuilder.builder().build().toBankOfHourDTO();
        BankOfHour expectedBankOfHour = bankOfHourMapper.toModel(expectedBankOfHourDTO);

        // when
        when(bankOfHourRepository.findByPK(VALID_BANK_OF_HOUR_ID, VALID_MOVEMENT_ID, VALID_USER_ID)).thenReturn(expectedBankOfHour);

        // then
        BankOfHourDTO foundBankOfHourDTO = bankOfHourService.find(VALID_BANK_OF_HOUR_ID, VALID_MOVEMENT_ID, VALID_USER_ID);

        assertThat(foundBankOfHourDTO, is(equalTo(expectedBankOfHourDTO)));
    }

    @Test
    void whenNotRegisteredBankOfHourIdGivenThenThrowAnException() {
        // when
        when(bankOfHourRepository.findByPK(INVALID_BANK_OF_HOUR_ID, INVALID_MOVEMENT_ID, INVALID_USER_ID)).thenReturn(null);

        // then
        assertThrows(ObjectNotFoundException.class, () -> bankOfHourService.find(INVALID_BANK_OF_HOUR_ID, INVALID_MOVEMENT_ID, INVALID_USER_ID));
    }

    @Test
    void whenUpdateIsCalledWithValidBankOfHourGivenThenReturnABankOfHourUpdated() {
        // given
        BankOfHourDTO expectedBankOfHourDTO = BankOfHourDTOBuilder.builder().build().toBankOfHourDTO();
        BankOfHour expectedBankOfHour = bankOfHourMapper.toModel(expectedBankOfHourDTO);

        // when
        when(bankOfHourRepository.findByPK(VALID_BANK_OF_HOUR_ID, VALID_MOVEMENT_ID, VALID_USER_ID)).thenReturn(expectedBankOfHour);
        when(bankOfHourRepository.save(expectedBankOfHour)).thenReturn(expectedBankOfHour);

        // then
        BankOfHourDTO updateBankOfHourDTO = bankOfHourService.update(expectedBankOfHourDTO);

        assertThat(updateBankOfHourDTO, is(equalTo(expectedBankOfHourDTO)));
    }

    @Test
    void whenUpdateIsCalledWithNotExistingBankOfHourThenAnExceptionShouldBeThrown() {
        // given
        BankOfHourDTO expectedBankOfHourDTO = BankOfHourDTOBuilder.builder().build().toBankOfHourDTO();

        // when
        when(bankOfHourRepository.findByPK(VALID_BANK_OF_HOUR_ID, VALID_MOVEMENT_ID, VALID_USER_ID)).thenReturn(null);

        // then
        assertThrows(ObjectNotFoundException.class, () -> bankOfHourService.update(expectedBankOfHourDTO));
    }

    @Test
    void whenListBankOfHourIsCalledThenReturnAListOfBankOfHour() {
        // given
        BankOfHourDTO expectedBankOfHourDTO = BankOfHourDTOBuilder.builder().build().toBankOfHourDTO();
        BankOfHour expectedBankOfHour = bankOfHourMapper.toModel(expectedBankOfHourDTO);

        // when
        when(bankOfHourRepository.findAll()).thenReturn(Collections.singletonList(expectedBankOfHour));

        // then
        List<BankOfHourDTO> listBankOfHourDTO = bankOfHourService.findAll();

        assertThat(listBankOfHourDTO, is(not(empty())));
        assertThat(listBankOfHourDTO.get(0), is(equalTo(expectedBankOfHourDTO)));
    }

    @Test
    void whenListBankOfHourIsCalledThenReturnAnEmptyListOfBankOfHour() {
        // when
        when(bankOfHourRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        List<BankOfHourDTO> listBankOfHourDTO = bankOfHourService.findAll();

        assertThat(listBankOfHourDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenABankOfHourShouldBeDeleted() {
        // given
        BankOfHourDTO expectedBankOfHourDTO = BankOfHourDTOBuilder.builder().build().toBankOfHourDTO();
        BankOfHour expectedBankOfHour = bankOfHourMapper.toModel(expectedBankOfHourDTO);

        //  when
        when(bankOfHourRepository.findByPK(VALID_BANK_OF_HOUR_ID, VALID_MOVEMENT_ID, VALID_USER_ID)).thenReturn(expectedBankOfHour);
        doNothing().when(bankOfHourRepository).delete(expectedBankOfHour);

        // then
        bankOfHourService.delete(VALID_BANK_OF_HOUR_ID, VALID_MOVEMENT_ID, VALID_USER_ID);

        verify(bankOfHourRepository, times(2)).findByPK(VALID_BANK_OF_HOUR_ID, VALID_MOVEMENT_ID, VALID_USER_ID);
        verify(bankOfHourRepository, times(1)).delete(expectedBankOfHour);
    }

    @Test
    void whenExclusionIsCalledWithNotExistingBankOfHourThenAnExceptionShouldBeThrown() {
        // when
        when(bankOfHourRepository.findByPK(INVALID_BANK_OF_HOUR_ID, INVALID_MOVEMENT_ID, INVALID_USER_ID)).thenThrow(ObjectNotFoundException.class);

        // then
        assertThrows(ObjectNotFoundException.class, () -> bankOfHourService.delete(INVALID_BANK_OF_HOUR_ID, INVALID_MOVEMENT_ID, INVALID_USER_ID));
    }
}
