package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.dtos.BankOfHourDTO;
import com.dio.santander.apimanagerpoints.mappers.BankOfHourMapper;
import com.dio.santander.apimanagerpoints.models.BankOfHour;
import com.dio.santander.apimanagerpoints.models.WorkDay;
import com.dio.santander.apimanagerpoints.repositories.BankOfHourRepository;
import com.dio.santander.apimanagerpoints.services.exceptions.DataIntegrityException;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectAlreadyRegisteredException;
import com.dio.santander.apimanagerpoints.services.exceptions.ObjectNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BankOfHourService {
    private final BankOfHourRepository bankOfHourRepository;
    private final BankOfHourMapper bankOfHourMapper = BankOfHourMapper.INSTANCE;

    public BankOfHourDTO insert(BankOfHourDTO objDto) {
        verifyIfIsAlreadyRegistered(
                objDto.getId().getBankOfHourId(),
                objDto.getId().getMovementId(),
                objDto.getId().getUserId());
        BankOfHour objToSave = bankOfHourMapper.toModel(objDto);
        BankOfHour objSaved = bankOfHourRepository.save(objToSave);
        return bankOfHourMapper.toDto(objSaved);
    }

    public BankOfHourDTO update(BankOfHourDTO objDto) {
        find(objDto.getId().getBankOfHourId(),
                objDto.getId().getMovementId(),
                objDto.getId().getUserId());
        BankOfHour objToSave = bankOfHourMapper.toModel(objDto);
        BankOfHour objSaved = bankOfHourRepository.save(objToSave);
        return bankOfHourMapper.toDto(objSaved);
    }

    public List<BankOfHourDTO> findAll() {
        return bankOfHourRepository.findAll().stream().map(bankOfHourMapper::toDto).collect(Collectors.toList());
    }

    public BankOfHourDTO find(Long bankOfHourId, Long movementId, Long userId) {
        BankOfHour obj = bankOfHourRepository.findByPK(bankOfHourId, movementId, userId);
        if (obj == null) {
            throw new ObjectNotFoundException(
                    "Objeto não encontrado! " +
                            "BankOfHourId: " + bankOfHourId +
                            ", MovementId: " + movementId +
                            ", UserId: " + userId +
                            ", Tipo: " + WorkDay.class.getName()
            );
        }
        return bankOfHourMapper.toDto(obj);
    }

    public void delete(Long bankOfHourId, Long movementId, Long userId) {
        find(bankOfHourId, movementId, userId);
        BankOfHour obj = bankOfHourRepository.findByPK(bankOfHourId, movementId, userId);
        try {
            bankOfHourRepository.delete(obj);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir um banco de hora que possui dependências");
        }
    }

    public Page<BankOfHourDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),
                orderBy);
        return bankOfHourRepository.findAll(pageRequest).map(bankOfHourMapper::toDto);
    }

    private void verifyIfIsAlreadyRegistered(Long bankOfHourId, Long movementId, Long userId) {
        BankOfHour obj = bankOfHourRepository.findByPK(bankOfHourId, movementId, userId);
        if (obj != null) {
            throw new ObjectAlreadyRegisteredException(
                    "Objeto existente! " +
                            "BankOfHourId: " + bankOfHourId +
                            ", MovementId: " + movementId +
                            ", UserId: " + userId +
                            ", Tipo: " + WorkDay.class.getName()
            );
        }
    }
}
