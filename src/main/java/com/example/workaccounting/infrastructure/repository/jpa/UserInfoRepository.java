package com.example.workaccounting.infrastructure.repository.jpa;

import com.example.workaccounting.domain.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
