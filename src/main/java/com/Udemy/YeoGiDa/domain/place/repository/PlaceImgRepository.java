package com.Udemy.YeoGiDa.domain.place.repository;

import com.Udemy.YeoGiDa.domain.place.entity.Place;
import com.Udemy.YeoGiDa.domain.place.entity.PlaceImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceImgRepository extends JpaRepository<PlaceImg, Long>, PlaceImgRepositoryCustom {

}
