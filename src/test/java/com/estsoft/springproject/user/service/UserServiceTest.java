package com.estsoft.springproject.user.service;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.estsoft.springproject.user.domain.Users;
import com.estsoft.springproject.user.domain.dto.AddUserRequest;
import com.estsoft.springproject.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@InjectMocks
	UserService userService;

	@Mock
	UserRepository repository;

	@Spy
	BCryptPasswordEncoder encoder;

	@Test
	public void testSave() {
		// given
		String email = "mock_email";
		BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();
		String password = pwEncoder.encode("mock_password");
		AddUserRequest user = new AddUserRequest(email, password);

		// userRepository.save -> stub
		Mockito.doReturn(new Users(email, password))
			.when(repository).save(Mockito.any(Users.class));

		// when : 회원가입 기능 - 사용자 정보 저장
		Users returnSaved = userService.save(user);

		// then
		assertThat(returnSaved.getEmail(), is(email));

		verify(repository, times(1)).save(Mockito.any(Users.class));
		verify(repository, times(1)).save(Mockito.any(Users.class));
	}
}