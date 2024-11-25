package com.estsoft.springproject.service;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.estsoft.springproject.entity.Member;
import com.estsoft.springproject.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@InjectMocks
	MemberService service;

	@Mock
	MemberRepository repository;

	@Test
	void getAllMembers() {
		// given
		List<Member> mockMembers = Arrays.asList(
			new Member(1L, "이름1"),
			new Member(2L, "이름1"),
			new Member(3L, "이름1")
		);
		when(repository.findAll()).thenReturn(mockMembers);

		// when
		List<Member> members = service.getAllMembers();

		// then
		assertThat(members.size(), is(3));
	}

	@Test
	void saveMember() {
		// given
		Long id = 1L;
		String name = "name";
		Member member = new Member(id, name);
		Mockito.doReturn(member).when(repository).save(Mockito.any(Member.class));

		// when
		Member savedMember = service.saveMember(member);

		// then
		assertThat(savedMember.getId(), is(id));

		verify(repository, times(1)).save(Mockito.any(Member.class));
	}
}