package com.dio.santander.apimanagerpoints.services;

import com.dio.santander.apimanagerpoints.dtos.BankOfHourDTO;
import com.dio.santander.apimanagerpoints.mappers.BankOfHourMapper;
import com.dio.santander.apimanagerpoints.models.BankOfHour;
import com.dio.santander.apimanagerpoints.repositories.BankOfHourRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BankOfHourService {
    private final BankOfHourRepository bankOfHourRepository;
    private final BankOfHourMapper bankOfHourMapper = BankOfHourMapper.INSTANCE;

    @Transactional
    public BankOfHourDTO insert(BankOfHourDTO objDto) {
        BankOfHour objToSave = bankOfHourMapper.toModel(objDto);
        BankOfHour objSaved = bankOfHourRepository.save(objToSave);
        return bankOfHourMapper.toDto(objSaved);
    }

    public Page<BankOfHourDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {

        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction),
                orderBy);
        return bankOfHourRepository.findAll(pageRequest).map(bankOfHourMapper::toDto);
    }
}
