package com.studentcollege.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentcollege.entity.Center;


@Repository
public interface CenterRepository extends JpaRepository<Center, Long>{

}
