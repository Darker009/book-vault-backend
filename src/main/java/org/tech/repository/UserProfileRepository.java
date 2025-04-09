package org.tech.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tech.entity.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
