package com.school.bookstore.repositories;

import com.school.bookstore.models.entities.StaffMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<StaffMember, Long> {
}
