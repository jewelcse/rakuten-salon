package com.bjit.salon.staff.service.serviceImpl;

import com.bjit.salon.staff.service.dto.request.StaffCreateDto;
import com.bjit.salon.staff.service.dto.request.StaffUpdateDto;
import com.bjit.salon.staff.service.dto.response.StaffResponseDto;
import com.bjit.salon.staff.service.entity.Staff;
import com.bjit.salon.staff.service.exception.StaffNotFoundException;
import com.bjit.salon.staff.service.mapper.StaffMapper;
import com.bjit.salon.staff.service.repository.StaffRepository;
import com.bjit.salon.staff.service.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StaffServiceImpl implements StaffService {

    private static final Logger logger = LoggerFactory.getLogger(StaffServiceImpl.class);

    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;
    @Override
    public void createNewStaff(StaffCreateDto staffCreateDto) {
        logger.info("adding new staff with " + staffCreateDto.toString());
        // todo: add new staff role to the user since now this user belong to staff
        staffRepository.save(staffMapper.toStaff(staffCreateDto));
        logger.info("added new staff completed");
    }

    @Override
    public void updateStaff(StaffUpdateDto staffUpdateDto) {
        logger.info("updating staff with " + staffUpdateDto.toString());
        Optional<Staff> staff = staffRepository.findById(staffUpdateDto.getId());
        if (staff.isEmpty()){
            throw new StaffNotFoundException("staff not found for id: " + staffUpdateDto.getId());
        }
        Staff updateStaff = Staff.builder()
                .address(staffUpdateDto.getAddress())
                .id(staff.get().getId())
                .salonId(staffUpdateDto.getSalonId())
                .userId(staffUpdateDto.getUserId())
                .salary(staffUpdateDto.getSalary())
                .employeementDate(staffUpdateDto.getEmployeementDate())
                .employeementType(staffUpdateDto.getEmployeementType())
                .contractNumber(staffUpdateDto.getContractNumber())
                        .build();
        staffRepository.save(updateStaff);
        logger.info("updated staff completed");
    }

    @Override
    public StaffResponseDto getStaff(long id) {
        Optional<Staff> staff = staffRepository.findById(id);
        if (staff.isEmpty()){
            throw new StaffNotFoundException("Staff not found for id: "+ id);
        }
        logger.info("getting staff with: " + staff.get());
        return staffMapper.toStaffResponseDto(staff.get());
    }

    @Override
    public List<StaffResponseDto> getAllStaff() {
        logger.info("getting staff list with size: " + staffRepository.findAll().size());
        return staffMapper.toListOfStaffResponseDto(staffRepository.findAll());
    }

    @Override
    public List<StaffResponseDto> getListOfStaffBySalon(long id) {
        return staffMapper.toListOfStaffResponseDto(staffRepository.findAllBySalonId(id));
    }
}
