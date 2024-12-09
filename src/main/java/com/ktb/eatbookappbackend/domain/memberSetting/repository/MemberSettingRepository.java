package com.ktb.eatbookappbackend.domain.memberSetting.repository;

import com.ktb.eatbookappbackend.entity.MemberSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSettingRepository extends JpaRepository<MemberSetting, String> {

}
