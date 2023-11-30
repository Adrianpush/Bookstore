package com.school.bookstore.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.bookstore.exceptions.UserCreateException;
import com.school.bookstore.exceptions.UserNotFoundException;
import com.school.bookstore.models.dtos.StaffMemberDTO;
import com.school.bookstore.models.entities.StaffMember;
import com.school.bookstore.repositories.StaffRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;
    private final ObjectMapper objectMapper;

    public StaffServiceImpl(StaffRepository staffRepository, ObjectMapper objectMapper) {
        this.staffRepository = staffRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public StaffMemberDTO createStaffMember(StaffMemberDTO staffMemberDTO) {
        validateStaffMemberDTO(staffMemberDTO);
        StaffMember staffMember = objectMapper.convertValue(staffMemberDTO, StaffMember.class);
        staffMember = staffRepository.save(staffMember);

        return objectMapper.convertValue(staffMember, StaffMemberDTO.class);
    }

    @Override
    public StaffMemberDTO getStaffById(Long id) {
        StaffMember staffMember = staffRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        return objectMapper.convertValue(staffMember, StaffMemberDTO.class);
    }

    @Override
    public String deleteStaffMemberById(Long id) {
        StaffMember staffMember = staffRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
        staffRepository.delete(staffMember);
        return "Staff Member with id " + id + " was deleted.";
    }

    @Override
    public List<StaffMemberDTO> getAllStaffMembers() {

        return staffRepository.findAll().stream()
                .map(staffMember -> objectMapper.convertValue(staffMember, StaffMemberDTO.class))
                .toList();
    }

    private void validateStaffMemberDTO(StaffMemberDTO staffMemberDTO) {
        if (staffMemberDTO.getFullName().length() < 4
                || staffMemberDTO.getUsername().length() < 4
                || staffMemberDTO.getPassword().length() < 4) {
            throw new UserCreateException("Length of username, full name and password must be longer than 4 characters");
        }
    }
}
