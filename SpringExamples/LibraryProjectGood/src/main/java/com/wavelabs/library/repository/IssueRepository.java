package com.wavelabs.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wavelabs.library.model.Issue;
import com.wavelabs.library.model.Member;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
	public List<Issue> findByReturned(Integer returned);
	public Long countByMemberAndReturned(Member member, Integer returned);
}
