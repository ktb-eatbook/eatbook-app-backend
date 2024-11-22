package com.ktb.eatbookappbackend.domain.novel.repository;

import com.ktb.eatbookappbackend.entity.Novel;
import java.util.List;

public interface NovelRepositoryCustom {

    List<Novel> findAllWithDetails();
}
