package com.junodx.api.controllers.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.ParametersAreNullableByDefault;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.ControllerUtils;
import com.junodx.api.controllers.commerce.OrderControllerTest;
import com.junodx.api.controllers.payloads.MessageResponse;
import com.junodx.api.controllers.users.payloads.RolePayload;
import com.junodx.api.controllers.users.payloads.UserForgotPasswordPayload;
import com.junodx.api.controllers.users.payloads.UserVerificationPayload;
import com.junodx.api.dto.mappers.UserMapStructMapper;
import com.junodx.api.dto.models.auth.UserBatchDto;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.auth.Authority;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.models.commerce.DataBuilderOrder;
import com.junodx.api.models.providers.DataBuilder;
import com.junodx.api.repositories.UserRepository;
import com.junodx.api.security.ResourceOwnerValidation;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.auth.UserServiceImpl;

import io.swagger.v3.oas.annotations.Parameters;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes = { UserControllerTest.class })
public class UserControllerTest {
	@Mock
	private UserRepository userRepository;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private UserServiceImpl userService;
	@InjectMocks
	private UserController userController;
	@Mock
	private ObjectMapper mapper;
	@Mock
	private UserMapStructMapper userMapper;
	@Mock
	private Authentication authentication;
	@Mock
	private UserDetailsImpl userDetailsImpls;
	@Mock
	private AuthenticationManager authenticationManager;
	@Mock
	private SecurityContext securityContext;
	@Mock
	private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationTokens;
	@Mock
	private User users;
	@Mock
	private ResourceOwnerValidation resourceOwnerValidation;
	private static UserDetails usedetails;
	@Autowired
	private MockMvc mockMvc;
	@Mock
	private UserVerificationPayload userVerificationPayload;

	@BeforeEach
	public void setUp() {
		usedetails = new org.springframework.security.core.userdetails.User("no-email@junodx.com", "",
				new ArrayList<>());
		mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
		HttpServletRequest mockRequest = new MockHttpServletRequest();
		ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
		RequestContextHolder.setRequestAttributes(servletRequestAttributes);

	}
 
	@Test
	public void testgetUser() throws Exception {
		String userId = "1L";
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUser().setAuthorities(authority);
		RolePayload roles = new RolePayload(auth);
		roles.setRole(auth);
		SimpleGrantedAuthority authoritys = new SimpleGrantedAuthority(Authority.ROLE_PATIENT);
		List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		updatedAuthorities.add(authoritys);
		DataBuilder.authorities.add(authoritys);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(userService.findOne(userId)).thenReturn(Optional.of(DataBuilderOrder.mockUser()));
		assertEquals(userId, DataBuilderOrder.mockUser().getId());
		this.mockMvc.perform(get("/api/users/{userId}" + "?userId=1L", userId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetUserException() throws Exception {
		String userId = "1L";
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUser().setAuthorities(authority);
		RolePayload roles = new RolePayload(auth);
		roles.setRole(auth);
		SimpleGrantedAuthority authoritys = new SimpleGrantedAuthority(Authority.ROLE_PATIENT);
		List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		updatedAuthorities.add(authoritys);
		DataBuilder.authorities.add(authoritys);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		this.mockMvc.perform(get("/api/users/{userId}" + "?userId=1L", userId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetUserForValidation() throws Exception {
		String userId = "12345";
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUser().setAuthorities(authority);
		RolePayload roles = new RolePayload(auth);
		roles.setRole(auth);
		SimpleGrantedAuthority authoritys = new SimpleGrantedAuthority(Authority.ROLE_PATIENT);
		List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		updatedAuthorities.add(authoritys);
		DataBuilder.authorities.add(authoritys);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		this.mockMvc.perform(get("/api/users/{userId}" + "?userId=1L", userId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testgetUserCatch() throws Exception {
		String userId = "1L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		assertEquals(userId, DataBuilderOrder.mockUser().getId());
		doThrow(new NoSuchElementException()).when(userService).findOne(userId);
		this.mockMvc.perform(get("/api/users/{userId}" + "?userId=1L", userId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetUserRoles() throws Exception {
		String userId = "1L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		assertEquals(userId, DataBuilderOrder.mockUser().getId());
		this.mockMvc
				.perform(
						get("/api/users/{userId}/roles" + "?userId=1L", userId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgetUserRolesException() throws Exception {
		String userId = "1L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		assertEquals(userId, DataBuilderOrder.mockUser().getId());
		doThrow(new NoSuchElementException()).when(userService).getRoles(Mockito.anyString());
		this.mockMvc
				.perform(
						get("/api/users/{userId}/roles" + "?userId=1L", userId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testaddUserRole() throws Exception {
		String userId = "1L";
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUser().setAuthorities(authority);
		RolePayload roles = new RolePayload(auth);
		roles.setRole(auth);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		assertEquals(userId, DataBuilderOrder.mockUser().getId());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrder());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/users/{userId}/roles" + "?userId=1L", userId).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testaddUserRoleException() throws Exception {
		String userId = "1L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		assertEquals(userId, DataBuilderOrder.mockUser().getId());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilderOrder.mockOrder());
		doThrow(new NoSuchElementException()).when(userService).addRole(Mockito.anyString(), Mockito.any(),
				Mockito.any());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/users/{userId}/roles" + "?userId=1L", userId).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testremoveUserRole() throws Exception {
		String userId = "1L";
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUser().setAuthorities(authority);
		RolePayload roles = new RolePayload(auth);
		roles.setRole(auth);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		assertEquals(userId, DataBuilderOrder.mockUser().getId());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockUser());
		System.out.println(inputJson);
		this.mockMvc.perform(delete("/api/users/{userId}/roles" + "?userId=1L", userId).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testremoveUserRoleException() throws Exception {
		String userId = "1L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		assertEquals(userId, DataBuilderOrder.mockUser().getId());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockUser());
		doThrow(new NoSuchElementException()).when(userService).removeRole(Mockito.anyString(), Mockito.any(),
				Mockito.any());
		System.out.println(inputJson);
		this.mockMvc.perform(delete("/api/users/{userId}/roles" + "?userId=1L", userId).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());

	} 

	@Test
	public void testsearchException() throws Exception {
		User user = User.createDummyUser();
		List<UserBatchDto> userBatchDtos = new ArrayList<>();
		userBatchDtos.add(DataBuilder.mockUserBatchDto());
		List<User> users = new ArrayList<>();
		users.add(user);
		Pageable paging = PageRequest.of(5, 5);
		final Page<User> pages = new PageImpl<>(users, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(userController, "maxPageSize", 20);
		when(userService.search(Optional.of("User"), Optional.of("Juno_test"), Optional.of("Nizamabad"),
				Optional.of("CA"), Optional.of("503165"), Optional.of("no-email@junodx.com"), Optional.of("12345"),
				Optional.of("234566"), Optional.of("1234"), Optional.of("23345"), Optional.of(UserType.STANDARD),
				Optional.of(UserStatus.ACTIVATED), paging)).thenReturn(pages);
		when(userMapper.userToUserBatchDtos(pages.getContent())).thenReturn(userBatchDtos);

		this.mockMvc.perform(get("/api/users/search" + "?page=5&size=5")).andExpect(status().isNotFound())
				.andDo(print());
	}

	@Test
	public void testsearch() throws Exception {
		User user = User.createDummyUser();
		List<UserBatchDto> userBatchDtos = new ArrayList<>();
		userBatchDtos.add(DataBuilder.mockUserBatchDto());
		List<User> users = new ArrayList<>();
		users.add(user);
		Pageable paging = PageRequest.of(5, 5);
		final Page<User> pages = new PageImpl<>(users, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(userController, "maxPageSize", 20);
		when(userService.search(Optional.of("User"), Optional.of("Juno_test"), Optional.of("Nizamabad"),
				Optional.of("CA"), Optional.of("503165"), Optional.of("no-email@junodx.com"), Optional.of("12345"),
				Optional.of("234566"), Optional.of("1234"), Optional.of("23345"), Optional.of(UserType.STANDARD),
				Optional.of(UserStatus.ACTIVATED), paging)).thenReturn(pages);
		when(userMapper.userToUserBatchDtos(pages.getContent())).thenReturn(userBatchDtos);
		this.mockMvc
				.perform(get("/api/users/search" + "?page=5" + "&size=5" + "&bylastname=User" + "&byfirstname=Juno_test"
						+ "&bycity=CA" + "&bystate=Nizamabad" + "&bypostalcode=503165" + "&byemail=no-email@junodx.com"
						+ "&bypracticeid=12345" + "&byproviderid=234566" + "&byxifinid=1234" + "&bystripeid=23345"
						+ "&bytype=STANDARD" + "&bystatus=ACTIVATED" + "&include=8&"))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testsearchMaximum() throws Exception {
		User user = User.createDummyUser();
		List<UserBatchDto> userBatchDtos = new ArrayList<>();
		userBatchDtos.add(DataBuilder.mockUserBatchDto());
		List<User> users = new ArrayList<>();
		users.add(user);
		Pageable paging = PageRequest.of(5, 5);
		final Page<User> pages = new PageImpl<>(users, paging, 5);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ReflectionTestUtils.setField(userController, "maxPageSize", 20);
		when(userService.search(Optional.of("User"), Optional.of("Juno_test"), Optional.of("Nizamabad"),
				Optional.of("CA"), Optional.of("503165"), Optional.of("no-email@junodx.com"), Optional.of("12345"),
				Optional.of("234566"), Optional.of("1234"), Optional.of("23345"), Optional.of(UserType.STANDARD),
				Optional.of(UserStatus.ACTIVATED), paging)).thenReturn(pages);
		when(userMapper.userToUserBatchDtos(pages.getContent())).thenReturn(userBatchDtos);
		this.mockMvc.perform(get("/api/users/search" + "?page=5" + "&size=21" + "&bylastname=User"
				+ "&byfirstname=Juno_test" + "&bycity=CA" + "&bystate=Nizamabad" + "&bypostalcode=503165"
				+ "&byemail=no-email@junodx.com" + "&bypracticeid=12345" + "&byproviderid=234566" + "&byxifinid=1234"
				+ "&bystripeid=23345" + "&bytype=STANDARD" + "&bystatus=ACTIVATED" + "&include=8&"))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testcreate() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockUser());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/users").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testcreateException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
	 	String inputJson = mapper.writeValueAsString(DataBuilder.mockUser());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(userService).save(Mockito.any(), Mockito.any());
		this.mockMvc.perform(post("/api/users").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testregisterUser() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockRegisterUserPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/users/register").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testsendVerificationCode() throws Exception {
		String email = "no-email@junodx.com";
		String clientid = "12345";
		assertEquals(email, DataBuilder.mockUser().getEmail());
		assertEquals(clientid, DataBuilder.mockUser().getClientId());
		this.mockMvc.perform(get("/api/users/code" + "?email=email&clientid=clientid", email, clientid)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testsendVerificationCodeException() throws Exception {
		String email = "no-email@junodx.com";
		String clientid = "12345";
		assertEquals(email, DataBuilder.mockUser().getEmail());
		assertEquals(clientid, DataBuilder.mockUser().getClientId());
		doThrow(new NoSuchElementException()).when(userService).sendVerificationCode(Mockito.anyString(),
				Mockito.anyString());
		this.mockMvc.perform(get("/api/users/code" + "?email=email&clientid=clientid", email, clientid)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testverifyUser() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.payload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/users/verify").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testverifyUserException() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.payload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(userService).verify(Mockito.any());
		this.mockMvc.perform(post("/api/users/verify").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test 
	public void testverifyUserPayLoadException() throws Exception {
		ResponseEntity<?> res = userController.verifyUser(null);
		assertEquals(HttpStatus.SC_BAD_REQUEST, res.getStatusCode().value());
		this.mockMvc.perform(post("/api/users/verify").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testverifyIfEmailAddressExistsAlready() throws Exception {
		String email = "no-email@junodx.com";
		String clientid = "12345";
		assertEquals(email, DataBuilder.mockUser().getEmail());
		assertEquals(clientid, DataBuilder.mockUser().getClientId());
		this.mockMvc.perform(get("/api/users/email-exists" + "?email=email&clientid=clientid", email, clientid)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}
 
	@Test
	public void testverifyIfEmailAddressExistsAlreadyCatchException() throws Exception {
		String email = "no-email@junodx.com";
		String clientId = "12345";
		assertEquals(email, DataBuilder.mockUser().getEmail());
		assertEquals(clientId, DataBuilder.mockUser().getClientId());
		doThrow(new NoSuchElementException()).when(userService).doesEmailExistForClientId(email, Optional.of(clientId));
		this.mockMvc
				.perform(get("/api/users/email-exists" + "?email=no-email@junodx.com&clientid=12345", email, clientId)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testverifyIfEmailAddressExistsAlreadyException() throws Exception {
		String email = "no-email@junodx.com";
		String clientId = "12345";
		assertEquals(clientId, DataBuilder.mockUser().getClientId());
		ResponseEntity<?> res = userController.verifyIfEmailAddressExistsAlready(null, Optional.of(clientId));
//		assertEquals(HttpStatus.SC_BAD_REQUEST, res.getStatusCode().value());
		this.mockMvc.perform(get("/api/users/email-exists" , email, clientId)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());
	} 

	@Test
	public void testgenerateForgotPassword() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockUserForgotPasswordPayload());
		System.out.println(inputJson);
		userService.forgotPassword(DataBuilder.mockUserForgotPasswordPayload());
		this.mockMvc
				.perform(post("/api/users/forgot-password").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testgenerateForgotCatchPasswordException() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockUserForgotPasswordPayload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(userService).forgotPassword(Mockito.any());
		this.mockMvc
				.perform(post("/api/users/forgot-password").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testgenerateForgotPasswordException() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(null);
		System.out.println(inputJson);
		ResponseEntity<?> res = userController.generateForgotPassword(null);
		assertEquals(HttpStatus.SC_BAD_REQUEST, res.getStatusCode().value());
		this.mockMvc
				.perform(post("/api/users/forgot-password").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testgenerateForgotPasswordNullCondition() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(null);
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(userService).forgotPassword(Mockito.any());
		this.mockMvc
				.perform(post("/api/users/forgot-password").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
//		userController.generateForgotPassword(null);
	}

	@Test
	public void testupdatePassword() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockUserUpdatePasswordPayload());
		System.out.println(inputJson);
		this.mockMvc
				.perform(post("/api/users/update-password").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testupdatePasswordException() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockUserUpdatePasswordPayload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(userService).updatePassword(Mockito.any());
		this.mockMvc
				.perform(post("/api/users/update-password").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdatePasswordNull() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(null);
		System.out.println(inputJson);
		ResponseEntity<?> res = userController.updatePassword(null);
		assertEquals(HttpStatus.SC_BAD_REQUEST, res.getStatusCode().value());
		this.mockMvc
				.perform(post("/api/users/update-password").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdatePreferences() throws Exception {
		String userId = "1L";
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUser().setAuthorities(authority);
		RolePayload roles = new RolePayload(auth);
		roles.setRole(auth);
		SimpleGrantedAuthority authoritys = new SimpleGrantedAuthority(Authority.ROLE_PATIENT);
		List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		updatedAuthorities.add(authoritys);
		DataBuilder.authorities.add(authoritys);
		SecurityContextHolder.setContext(securityContext);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockUserUpdatePreferencesPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/users/update-preferences/{userId}", userId).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testupdatePreferenceNull() throws Exception {
		String userId = "1L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(null);
		System.out.println(inputJson);
		ResponseEntity<?> res = userController.updatePreferences(userId, null);
		assertEquals(HttpStatus.SC_BAD_REQUEST, res.getStatusCode().value());
		this.mockMvc.perform(post("/api/users/update-preferences/{userId}", userId).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdatePreferenceException() throws Exception {
		String userId = "1L";
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockUserUpdatePreferencesPayload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(userService).updatePreferences(Mockito.anyString(), Mockito.any(),
				Mockito.any());
		this.mockMvc.perform(post("/api/users/update-preferences/{userId}", userId).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdatePreferencesException() throws Exception {
		String userId = "12345";
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUser().setAuthorities(authority);
		RolePayload roles = new RolePayload(auth);
		roles.setRole(auth);
		SimpleGrantedAuthority authoritys = new SimpleGrantedAuthority(Authority.ROLE_PATIENT);
		List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		updatedAuthorities.add(authoritys);
		DataBuilder.authorities.add(authoritys);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationTokens);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockUserUpdatePreferencesPayload());
		System.out.println(inputJson);
		this.mockMvc.perform(post("/api/users/update-preferences/{userId}", userId).content(inputJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdate() throws Exception {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT"); 
	 	List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUser().setAuthorities(authority);
		RolePayload roles = new RolePayload(auth);
		roles.setRole(auth);
		SimpleGrantedAuthority authoritys = new SimpleGrantedAuthority(Authority.ROLE_PATIENT);
		List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		updatedAuthorities.add(authoritys);
		User user = User.createDummyUser();
		DataBuilder.mockuserUpdatePayload.setPatientDetails(DataBuilder.mockUserUpdatePatientDetailsPayload());
		DataBuilder.medications.add(DataBuilder.mockMedication());
		DataBuilder.mockUserUpdatePatientDetailsPayload().setMedications(DataBuilder.medications);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
	//	User update = userService.update(DataBuilder.mockuserUpdatePayload, DataBuilder.getMockUserDetailsImpl());
		when(userService.update(DataBuilder.mockuserUpdatePayload, DataBuilder.getMockUserDetailsImpl()))
				.thenReturn(user);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockuserUpdatePayload());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/users").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
//		ResponseEntity<?> response = userController.update(DataBuilder.mockuserUpdatePayload);
//		assertEquals(HttpStatus.SC_OK, response.getStatusCode().value());
	}

	@Test
	public void testupdateBadRequestException() throws Exception {
		DataBuilder.mockuserUpdatePayload.setPatientDetails(DataBuilder.mockUserUpdatePatientDetailsPayload());
		DataBuilder.medications.add(DataBuilder.mockMedication());
		DataBuilder.mockUserUpdatePatientDetailsPayload().setMedications(DataBuilder.medications);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockuserUpdatePayload);
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(userService).update(DataBuilder.mockuserUpdatePayload,
				DataBuilder.getMockUserDetailsImpl());
		this.mockMvc.perform(patch("/api/users").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdateBadRequestCatchException() throws Exception {
		DataBuilder.mockuserUpdatePayload.setPatientDetails(DataBuilder.mockUserUpdatePatientDetailsPayload());
		DataBuilder.medications.add(DataBuilder.mockMedication());
		DataBuilder.mockUserUpdatePatientDetailsPayload().setMedications(DataBuilder.medications);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockuserUpdatePayload);
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(userService).update(DataBuilder.mockuserUpdatePayload,
				DataBuilder.getMockUserDetailsImpl());
		ResponseEntity<?> response = userController.update(DataBuilder.mockuserUpdatePayload);
		assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode().value());
//		this.mockMvc.perform(patch("/api/users").content(inputJson).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest()).andDo(print());
	} 

	@Test
	public void testupdateUserIdException() throws Exception {
		DataBuilder.mockUserUpdatePatientDetailsPayload().setMedications(DataBuilder.medications);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockuserUpdatePayloadId());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/users").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdateUserException() throws Exception {
		DataBuilder.mockUserUpdatePatientDetailsPayload().setMedications(DataBuilder.medications);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(null);
		System.out.println(inputJson);
		ResponseEntity<?> response = userController.update(null);
		assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode().value());
		this.mockMvc.perform(patch("/api/users").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdateExceptions() throws Exception {
		Authority auth = new Authority();
		auth.setName("ROLE_PATIENT");
		List<Authority> authority = new ArrayList<>();
		authority.add(auth);
		DataBuilder.mockUser().setAuthorities(authority);
		RolePayload roles = new RolePayload(auth);
		roles.setRole(auth);
		SimpleGrantedAuthority authoritys = new SimpleGrantedAuthority(Authority.ROLE_PATIENT);
		List<SimpleGrantedAuthority> updatedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		updatedAuthorities.add(authoritys);
		DataBuilder.authorities.add(authoritys);
		DataBuilder.medications.add(DataBuilder.mockMedication());
		DataBuilder.mockUserUpdatePatientDetailsPayload().setMedications(DataBuilder.medications);
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationTokens);
		when(userService.update(DataBuilder.mockuserUpdatePayload(), DataBuilder.getMockUserDetailsImpl()))
				.thenReturn(DataBuilder.mockUser());
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockuserUpdatePayload());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/users").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testdeleteUser() throws Exception {
		String userId = "1L";
		verify(userService, times(0)).delete(userId);
		assertEquals(userId, DataBuilder.mockUser().getId());
		this.mockMvc.perform(delete("/api/users/{userId}", userId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testdeleteUserException() throws Exception {
		String userId = "1L";
		assertEquals(userId, DataBuilder.mockUser().getId());
		doThrow(new NoSuchElementException()).when(userService).delete(Mockito.anyString());
		this.mockMvc.perform(delete("/api/users/{userId}", userId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testupdateEmail() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.getMockEmailChangePayload());
		System.out.println(inputJson);
		this.mockMvc.perform(patch("/api/users/email").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testupdateEmailException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.getMockEmailChangePayload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(userService).startEmailUpdate(Mockito.anyString(),
				Mockito.anyString(), Mockito.anyString(), Mockito.any());
		this.mockMvc.perform(patch("/api/users/email").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testupdateEmailNull() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(null);
		System.out.println(inputJson);
		ResponseEntity<?> response = userController.updateEmail(null);
		assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode().value());
		this.mockMvc.perform(patch("/api/users/email").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testverifyUpdateEmail() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.payload());
		System.out.println(inputJson);
		this.mockMvc
				.perform(patch("/api/users/email/verify").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void testverifyUpdateEmailException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.payload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(userService).finishEmailUpdate(Mockito.any(), Mockito.any());
		this.mockMvc
				.perform(patch("/api/users/email/verify").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testverifyUpdateEmailNull() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(null);
		System.out.println(inputJson);
		ResponseEntity<?> response = userController.verifyUpdateEmail(null);
		assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode().value());
		this.mockMvc
				.perform(patch("/api/users/email/verify").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testchangePassword() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(userService.changePassword(Mockito.any(), Mockito.any())).thenReturn(true);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockChangePasswordPayload());
		System.out.println(inputJson);
		this.mockMvc
				.perform(post("/api/users/change-password").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void testchangePasswordNull() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(null);
		System.out.println(inputJson);
		ResponseEntity<?> response = userController.changePassword(null);
	 	assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode().value());
		this.mockMvc
				.perform(post("/api/users/change-password").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testchangePasswordException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(DataBuilder.mockUser()));
		when(passwordEncoder.matches(DataBuilder.mockChangePasswordPayload().getExistingPassword(),
				DataBuilder.mockUser().getPassword())).thenReturn(true);
		when(userRepository.save(DataBuilder.mockUser())).thenReturn(DataBuilder.mockUser());
		when(userService.changePassword(DataBuilder.mockChangePasswordPayload(), DataBuilder.getMockUserDetailsImpl()))
				.thenReturn(false);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockChangePasswordPayload());
		System.out.println(inputJson);
		this.mockMvc
				.perform(post("/api/users/change-password").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testchangePasswordCatchException() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(userService.changePassword(DataBuilder.mockChangePasswordPayload(), DataBuilder.getMockUserDetailsImpl()))
				.thenReturn(true);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockChangePasswordPayload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(userService).changePassword(Mockito.any(), Mockito.any());
		this.mockMvc
				.perform(post("/api/users/change-password").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testchangePasswordExceptionCatchRequest() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		when(userService.changePassword(DataBuilder.mockChangePasswordPayload(), DataBuilder.getMockUserDetailsImpl()))
				.thenReturn(true);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockChangePasswordPayload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(userService).finishEmailUpdate(Mockito.any(), Mockito.any());
		this.mockMvc
				.perform(post("/api/users/change-password").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());

	}
 
	@Test
	public void testchangePasswordExceptionBadRequest() throws Exception {
		SecurityContextHolder.setContext(securityContext);
		when((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
				.thenReturn((UsernamePasswordAuthenticationToken) DataBuilder.usernamePasswordAuthenticationToken);
		ObjectMapper mapper = new ObjectMapper();
		String inputJson = mapper.writeValueAsString(DataBuilder.mockChangePasswordPayload());
		System.out.println(inputJson);
		doThrow(new NoSuchElementException()).when(userService).finishEmailUpdate(Mockito.any(), Mockito.any());
		this.mockMvc
				.perform(post("/api/users/change-password").content(inputJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andDo(print());
	}
}
