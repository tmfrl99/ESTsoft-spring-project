package com.estsoft.springproject.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.estsoft.springproject.entity.Member;
import com.estsoft.springproject.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest // @SpringBootApplication이 있는 클래스를 찾고 그 클래스에 포함되어있는 빈을 찾고 다음 테스트용 애플리케이션 컨텍스트 생성
@AutoConfigureMockMvc // MockMvc 생성하고 자동으로 구성
class MemberControllerTest {
	@Autowired
	WebApplicationContext context;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	MemberRepository repository;

	@BeforeEach // @JUnit 어노테이션. 각각의 테스트 케이스를 실행하기 전마다 실행
	public void setUp() { // 테스트코드 실행 이전에 실행되는 코드. mockMvc 세팅
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	void testGetAllMember() throws Exception {
		// given: 멤버 목록 저장  (생략)

		// when: GET /members
		ResultActions resultActions = mockMvc.perform(get("/members")
			.accept(MediaType.APPLICATION_JSON));

		// then: response 검증
		resultActions.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$[0].id").value(1))
			.andExpect(jsonPath("$[1].id").value(2));
	}

	@Test
	void testSaveMember() throws Exception {
		// given
		Member requestMember = new Member(1L, "홍길동");

		// when
		ResultActions resultActions = mockMvc.perform(post("/members")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(requestMember)));

		// then
		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.name").value("홍길동"));
	}
}