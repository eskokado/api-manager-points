package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.builders.LocationDTOBuilder;
import com.dio.santander.apimanagerpoints.dtos.LocationDTO;
import com.dio.santander.apimanagerpoints.mappers.LocationMapper;
import com.dio.santander.apimanagerpoints.models.Location;
import com.dio.santander.apimanagerpoints.repositories.LocationRepository;
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
public class LocationServiceTest {
    private static final Long VALID_LOCATION_ID = 1L;
    private static final Long INVALID_LOCATION_ID = 2L;

    @Mock
    private LocationRepository locationRepository;

    private LocationMapper locationMapper = LocationMapper.INSTANCE;

    @InjectMocks
    private LocationService locationService;

    @Test
    void whenLocationInformedIsNotExistingThenItShouldBeCreated() {
        // given
        LocationDTO expectedLocationDTO = LocationDTOBuilder.builder().build().toLocationDTO();
        Location expectedLocation = locationMapper.toModel(expectedLocationDTO);

        // when
        when(locationRepository.findById(VALID_LOCATION_ID)).thenReturn(Optional.empty());
        when(locationRepository.save(expectedLocation)).thenReturn(expectedLocation);

        // then
        LocationDTO createLocationDTO = locationService.insert(expectedLocationDTO);

        assertThat(createLocationDTO, is(equalTo(expectedLocationDTO)));
    }

    @Test
    void whenAlreadyRegisteredLocationInformedThenAnExceptionShouldBeThrown() {
        // given
        LocationDTO expectedLocationDTO = LocationDTOBuilder.builder().build().toLocationDTO();
        Location expectedLocation = locationMapper.toModel(expectedLocationDTO);

        // when
        when(locationRepository.findById(VALID_LOCATION_ID)).thenReturn(Optional.of(expectedLocation));

        // then
        assertThrows(ObjectAlreadyRegisteredException.class, () -> locationService.insert(expectedLocationDTO));
    }

    @Test
    void whenValidLocationIdIsGivenThenReturnAnLocation() {
        // given
        LocationDTO expectedLocationDTO = LocationDTOBuilder.builder().build().toLocationDTO();
        Location expectedLocation = locationMapper.toModel(expectedLocationDTO);

        // when
        when(locationRepository.findById(VALID_LOCATION_ID)).thenReturn(Optional.of(expectedLocation));

        // then
        LocationDTO foundLocationDTO = locationService.find(VALID_LOCATION_ID);

        assertThat(foundLocationDTO, is(equalTo(expectedLocationDTO)));
    }

    @Test
    void whenNotRegisteredLocationIdGivenThenThrowAnException() {
        // when
        when(locationRepository.findById(INVALID_LOCATION_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> locationService.find(INVALID_LOCATION_ID));
    }

    @Test
    void whenUpdateIsCalledWithValidLocationGivenThenReturnALocationUpdated() {
        // given
        LocationDTO expectedLocationDTO = LocationDTOBuilder.builder().build().toLocationDTO();
        Location expectedLocation = locationMapper.toModel(expectedLocationDTO);

        // when
        when(locationRepository.findById(VALID_LOCATION_ID)).thenReturn(Optional.of(expectedLocation));
        when(locationRepository.save(expectedLocation)).thenReturn(expectedLocation);

        // then
        LocationDTO updateLocationDTO = locationService.update(expectedLocationDTO);

        assertThat(updateLocationDTO, is(equalTo(expectedLocationDTO)));
    }

    @Test
    void whenUpdateIsCalledWithNotExistingLocationThenAnExceptionShouldBeThrown() {
        // given
        LocationDTO expectedLocationDTO = LocationDTOBuilder.builder().build().toLocationDTO();

        // when
        when(locationRepository.findById(VALID_LOCATION_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> locationService.update(expectedLocationDTO));
    }

    @Test
    void whenListLocationIsCalledThenReturnAListOfLocation() {
        // given
        LocationDTO expectedLocationDTO = LocationDTOBuilder.builder().build().toLocationDTO();
        Location expectedLocation = locationMapper.toModel(expectedLocationDTO);

        // when
        when(locationRepository.findAll()).thenReturn(Collections.singletonList(expectedLocation));

        // then
        List<LocationDTO> listLocationDTO = locationService.findAll();

        assertThat(listLocationDTO, is(not(empty())));
        assertThat(listLocationDTO.get(0), is(equalTo(expectedLocationDTO)));
    }

    @Test
    void whenListLocationIsCalledThenReturnAnEmptyListOfLocation() {
        // when
        when(locationRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        List<LocationDTO> listLocationDTO = locationService.findAll();

        assertThat(listLocationDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenALocationShouldBeDeleted() {
        // given
        LocationDTO expectedLocationDTO = LocationDTOBuilder.builder().build().toLocationDTO();
        Location expectedLocation = locationMapper.toModel(expectedLocationDTO);

        //  when
        when(locationRepository.findById(VALID_LOCATION_ID)).thenReturn(Optional.of(expectedLocation));
        doNothing().when(locationRepository).deleteById(VALID_LOCATION_ID);

        // then
        locationService.delete(VALID_LOCATION_ID);

        verify(locationRepository, times(1)).findById(VALID_LOCATION_ID);
        verify(locationRepository, times(1)).deleteById(VALID_LOCATION_ID);
    }

    @Test
    void whenExclusionIsCalledWithNotExistingLocationThenanExceptionShouldBeThrown() {
        // when
        when(locationRepository.findById(INVALID_LOCATION_ID)).thenReturn(Optional.empty());

        // then
        assertThrows(ObjectNotFoundException.class, () -> locationService.delete(INVALID_LOCATION_ID));
    }
}
