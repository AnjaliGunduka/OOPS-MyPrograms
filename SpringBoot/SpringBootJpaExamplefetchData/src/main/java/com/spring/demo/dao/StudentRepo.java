package com.spring.demo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.spring.demo.model.Student;

public interface StudentRepo extends CrudRepository<Student, Integer>
{
List<Student> findByMarks(int marks);
List<Student> findBySidGreaterThan(int sid);
@Query("from Student where sname=?1 order by sname")
//@Query("from Student where name=?1 order by aname")//to get order by name 
List<Student> findBySnameSorted(String sname);

}
