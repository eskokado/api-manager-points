package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.builders.CompanyDTOBuilder;
import com.dio.santander.apimanagerpoints.dtos.CompanyDTO;
import com.dio.santander.apimanagerpoints.mappers.CompanyMapper;
import com.dio.santander.apimanagerpoints.models.Company;
import com.dio.santander.apimanagerpoints.repositories.CompanyRepository;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectAlreadyRegisteredException;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {
    private static final Long VALID_COMPANY_ID = 1L;
    private static final Long INVALID_COMPANY_ID = 2L;

    @Mock
    private CompanyRepository companyRepository;

    private CompanyMapper companyMapper = CompanyMapper.INSTANCE;

    @InjectMocks
    private CompanyService companyService;

    @Test
    void whenCompanyInformedIsNotExistingThenItShouldBeCreated() {
        // given
        CompanyDTO expectedCompanyDTO = CompanyDTOBuilder.builder().build().toCompanyDTO();
        Company expectedCompany = companyMapper.toModel(expectedCompanyDTO);

        // when
        when(companyRepository.findById(VALID_COMPANY_ID)).thenReturn(Optional.empty());
        when(companyRepository.save(expectedCompany)).thenReturn(expectedCompany);

        // then
        CompanyDTO createCompanyDTO = companyService.insert(expectedCompanyDTO);

        assertThat(createCompanyDTO, is(equalTo(expectedCompanyDTO)));
    }

    @Test
    void whenAlreadyRegisteredCompanyInformedThenAnExceptionShouldBeThrown() {
        // given
        CompanyDTO expectedCompanyDTO = CompanyDTOBuilder.builder().build().toCompanyDTO();
        Company expectedCompany = companyMapper.toModel(expectedCompanyDTO);

        // when
        when(companyRepository.findById(VALID_COMPANY_ID)).thenReturn(Optional.of(expectedCompany));

        // then
        assertThrows(ObjectAlreadyRegisteredException.class, () -> companyService.insert(expectedCompanyDTO));
    }

    @Test
    void whenValidCompanyIdIsGivenThenReturnAnCompany() {
        // given
        CompanyDTO expectedCompanyDTO = CompanyDTOBuilder.builder().build().toCompanyDTO();
        Company expectedCompany = companyMapper.toModel(expectedCompanyDTO);

        // when
        when(companyRepository.findById(VALID_COMPANY_ID)).thenReturn(Optional.of(expectedCompany));

        // then
        CompanyDTO foundCompanyDTO = companyService.find(VALID_COMPANY_ID);

        assertThat(foundCompanyDTO, is(equalTo(expectedCompanyDTO)));
    }

    @Test
    void whenNotRegisteredCompanyIdGivenThenThrowAnException() {
        // when
        when(companyRepository.findById(INVALID_COMPANY_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> companyService.find(INVALID_COMPANY_ID));
    }

    @Test
    void whenUpdateIsCalledWithValidCompanyGivenThenReturnACompanyUpdated() {
        // given
        CompanyDTO expectedCompanyDTO = CompanyDTOBuilder.builder().build().toCompanyDTO();
        Company expectedCompany = companyMapper.toModel(expectedCompanyDTO);

        // when
        when(companyRepository.findById(VALID_COMPANY_ID)).thenReturn(Optional.of(expectedCompany));
        when(companyRepository.save(expectedCompany)).thenReturn(expectedCompany);

        // then
        CompanyDTO updateCompanyDTO = companyService.update(expectedCompanyDTO);

        assertThat(updateCompanyDTO, is(equalTo(expectedCompanyDTO)));
    }

    @Test
    void whenUpdateIsCalledWithNotExistingCompanyThenAnExceptionShouldBeThrown() {
        // given
        CompanyDTO expectedCompanyDTO = CompanyDTOBuilder.builder().build().toCompanyDTO();

        // when
        when(companyRepository.findById(VALID_COMPANY_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> companyService.update(expectedCompanyDTO));
    }

    @Test
    void whenListCompanyIsCalledThenReturnAListOfCompany() {
        // given
        CompanyDTO expectedCompanyDTO = CompanyDTOBuilder.builder().build().toCompanyDTO();
        Company expectedCompany = companyMapper.toModel(expectedCompanyDTO);

        // when
        when(companyRepository.findAll()).thenReturn(Collections.singletonList(expectedCompany));

        // then
        List<CompanyDTO> listCompanyDTO = companyService.findAll();

        assertThat(listCompanyDTO, is(not(empty())));
        assertThat(listCompanyDTO.get(0), is(equalTo(expectedCompanyDTO)));
    }

    @Test
    void whenListCompanyIsCalledThenReturnAnEmptyListOfCompany() {
        // when
        when(companyRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        List<CompanyDTO> listCompanyDTO = companyService.findAll();

        assertThat(listCompanyDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenACompanyShouldBeDeleted() {
        // given
        CompanyDTO expectedCompanyDTO = CompanyDTOBuilder.builder().build().toCompanyDTO();
        Company expectedCompany = companyMapper.toModel(expectedCompanyDTO);

        //  when
        when(companyRepository.findById(VALID_COMPANY_ID)).thenReturn(Optional.of(expectedCompany));
        doNothing().when(companyRepository).deleteById(VALID_COMPANY_ID);

        // then
        companyService.delete(VALID_COMPANY_ID);

        verify(companyRepository, times(1)).findById(VALID_COMPANY_ID);
        verify(companyRepository, times(1)).deleteById(VALID_COMPANY_ID);
    }

    @Test
    void whenExclusionIsCalledWithNotExistingCompanyThenanExceptionShouldBeThrown() {
        // when
        when(companyRepository.findById(INVALID_COMPANY_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> companyService.delete(INVALID_COMPANY_ID));
    }
}
