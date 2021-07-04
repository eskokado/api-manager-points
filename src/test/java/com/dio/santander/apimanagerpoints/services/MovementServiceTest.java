package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.builders.MovementDTOBuilder;
import com.dio.santander.apimanagerpoints.dtos.MovementDTO;
import com.dio.santander.apimanagerpoints.mappers.MovementMapper;
import com.dio.santander.apimanagerpoints.models.Movement;
import com.dio.santander.apimanagerpoints.repositories.MovementRepository;
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
public class MovementServiceTest {
    private static final Long VALID_MOVEMENT_ID = 1L;
    private static final Long VALID_USER_ID = 1L;
    private static final Long INVALID_MOVEMENT_ID = 2L;
    private static final Long INVALID_USER_ID = 2L;

    @Mock
    private MovementRepository movementRepository;

    private MovementMapper movementMapper = MovementMapper.INSTANCE;

    @InjectMocks
    private MovementService movementService;

    @Test
    void whenMovementInformedIsNotExistingThenItShouldBeCreated() {
        // given
        MovementDTO expectedMovementDTO = MovementDTOBuilder.builder().build().toMovementDTO();
        Movement expectedMovement = movementMapper.toModel(expectedMovementDTO);

        // when
        when(movementRepository.findByPK(VALID_MOVEMENT_ID, VALID_USER_ID)).thenReturn(null);
        when(movementRepository.save(expectedMovement)).thenReturn(expectedMovement);

        // then
        MovementDTO createMovementDTO = movementService.insert(expectedMovementDTO);

        assertThat(createMovementDTO, is(equalTo(expectedMovementDTO)));
    }

    @Test
    void whenAlreadyRegisteredMovementInformedThenAnExceptionShouldBeThrown() {
        // given
        MovementDTO expectedMovementDTO = MovementDTOBuilder.builder().build().toMovementDTO();
        Movement expectedMovement = movementMapper.toModel(expectedMovementDTO);

        // when
        when(movementRepository.findByPK(VALID_MOVEMENT_ID, VALID_USER_ID)).thenReturn(expectedMovement);

        // then
        assertThrows(ObjectAlreadyRegisteredException.class, () -> movementService.insert(expectedMovementDTO));
    }

    @Test
    void whenValidMovementIdIsGivenThenReturnAnMovement() {
        // given
        MovementDTO expectedMovementDTO = MovementDTOBuilder.builder().build().toMovementDTO();
        Movement expectedMovement = movementMapper.toModel(expectedMovementDTO);

        // when
        when(movementRepository.findByPK(VALID_MOVEMENT_ID, VALID_USER_ID)).thenReturn(expectedMovement);

        // then
        MovementDTO foundMovementDTO = movementService.find(VALID_MOVEMENT_ID, VALID_USER_ID);

        assertThat(foundMovementDTO, is(equalTo(expectedMovementDTO)));
    }

    @Test
    void whenNotRegisteredMovementIdGivenThenThrowAnException() {
        // when
        when(movementRepository.findByPK(INVALID_MOVEMENT_ID, INVALID_USER_ID)).thenReturn(null);

        // then
        assertThrows(ObjectNotFoundException.class, () -> movementService.find(INVALID_MOVEMENT_ID, INVALID_USER_ID));
    }

    @Test
    void whenUpdateIsCalledWithValidMovementGivenThenReturnAMovementUpdated() {
        // given
        MovementDTO expectedMovementDTO = MovementDTOBuilder.builder().build().toMovementDTO();
        Movement expectedMovement = movementMapper.toModel(expectedMovementDTO);

        // when
        when(movementRepository.findByPK(VALID_MOVEMENT_ID, VALID_USER_ID)).thenReturn(expectedMovement);
        when(movementRepository.save(expectedMovement)).thenReturn(expectedMovement);

        // then
        MovementDTO updateMovementDTO = movementService.update(expectedMovementDTO);

        assertThat(updateMovementDTO, is(equalTo(expectedMovementDTO)));
    }

    @Test
    void whenUpdateIsCalledWithNotExistingMovementThenAnExceptionShouldBeThrown() {
        // given
        MovementDTO expectedMovementDTO = MovementDTOBuilder.builder().build().toMovementDTO();

        // when
        when(movementRepository.findByPK(VALID_MOVEMENT_ID, VALID_USER_ID)).thenReturn(null);

        // then
        assertThrows(ObjectNotFoundException.class, () -> movementService.update(expectedMovementDTO));
    }

    @Test
    void whenListMovementIsCalledThenReturnAListOfMovement() {
        // given
        MovementDTO expectedMovementDTO = MovementDTOBuilder.builder().build().toMovementDTO();
        Movement expectedMovement = movementMapper.toModel(expectedMovementDTO);

        // when
        when(movementRepository.findAll()).thenReturn(Collections.singletonList(expectedMovement));

        // then
        List<MovementDTO> listMovementDTO = movementService.findAll();

        assertThat(listMovementDTO, is(not(empty())));
        assertThat(listMovementDTO.get(0), is(equalTo(expectedMovementDTO)));
    }

    @Test
    void whenListMovementIsCalledThenReturnAnEmptyListOfMovement() {
        // when
        when(movementRepository.findAll()).thenReturn(Collections.emptyList());

        // then
        List<MovementDTO> listMovementDTO = movementService.findAll();

        assertThat(listMovementDTO, is(empty()));
    }

    @Test
    void whenExclusionIsCalledWithValidIdThenAMovementShouldBeDeleted() {
        // given
        MovementDTO expectedMovementDTO = MovementDTOBuilder.builder().build().toMovementDTO();
        Movement expectedMovement = movementMapper.toModel(expectedMovementDTO);

        //  when
        when(movementRepository.findByPK(VALID_MOVEMENT_ID, VALID_USER_ID)).thenReturn(expectedMovement);
        doNothing().when(movementRepository).delete(expectedMovement);

        // then
        movementService.delete(VALID_MOVEMENT_ID, VALID_USER_ID);

        verify(movementRepository, times(2)).findByPK(VALID_MOVEMENT_ID, VALID_USER_ID);
        verify(movementRepository, times(1)).delete(expectedMovement);
    }

    @Test
    void whenExclusionIsCalledWithNotExistingMovementThenAnExceptionShouldBeThrown() {
        // when
        when(movementRepository.findByPK(INVALID_MOVEMENT_ID, INVALID_USER_ID)).thenThrow(ObjectNotFoundException.class);

        // then
        assertThrows(ObjectNotFoundException.class, () -> movementService.delete(INVALID_MOVEMENT_ID, INVALID_USER_ID));
    }
}
