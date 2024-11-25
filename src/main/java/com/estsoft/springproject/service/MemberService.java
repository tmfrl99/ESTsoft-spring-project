package com.estsoft.springproject.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.estsoft.springproject.entity.Member;
import com.estsoft.springproject.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository repository;

	public List<Member> getAllMembers() {
		return repository.findAll();        // Member 테이블 모든 정보 조회
		// SELECT * FROM member;
	}

	public Member saveMember(Member member) {
		return repository.save(member);
	}

}