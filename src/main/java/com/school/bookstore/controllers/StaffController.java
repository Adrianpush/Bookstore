package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.StaffMemberDTO;
import com.school.bookstore.services.StaffService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/staff")
public class StaffController {

    StaffService staffService;

    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping
    public ResponseEntity<StaffMemberDTO> createStaffMember(@Valid @RequestBody StaffMemberDTO staffMemberDTO) {
        return ResponseEntity.ok(staffService.createStaffMember(staffMemberDTO));
    }

    @GetMapping
    public ResponseEntity<List<StaffMemberDTO>> getAllStaffMembers() {
        return ResponseEntity.ok(staffService.getAllStaffMembers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StaffMemberDTO> getStaffMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStaffMember(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.deleteStaffMemberById(id));
    }
}
