package com.school.bookstore.services;

import com.school.bookstore.models.dtos.StaffMemberDTO;

import java.util.List;

public interface StaffService {

    StaffMemberDTO createStaffMember(StaffMemberDTO staffMemberDTO);
    StaffMemberDTO getStaffById(Long id);
    String deleteStaffMemberById(Long id);
    List<StaffMemberDTO> getAllStaffMembers();
}
