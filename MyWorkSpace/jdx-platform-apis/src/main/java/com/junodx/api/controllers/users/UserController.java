package com.junodx.api.controllers.users;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junodx.api.controllers.ControllerUtils;
import com.junodx.api.controllers.payloads.MessageResponse;
import com.junodx.api.controllers.users.payloads.RegisterUserPayload;
import com.junodx.api.controllers.users.payloads.RolePayload;
import com.junodx.api.controllers.users.payloads.UserForgotPasswordPayload;
import com.junodx.api.controllers.users.payloads.UserUpdatePasswordPayload;
import com.junodx.api.controllers.users.payloads.UserVerificationPayload;
import com.junodx.api.controllers.users.payloads.*;
import com.junodx.api.controllers.users.payloads.UserUpdate.UserUpdatePayload;
import com.junodx.api.controllers.users.payloads.UserUpdate.UserUpdatePreferencesPayload;
import com.junodx.api.dto.mappers.UserMapStructMapper;
import com.junodx.api.dto.models.auth.UserBatchDto;
import com.junodx.api.logging.LogCode;
import com.junodx.api.models.auth.User;
import com.junodx.api.models.auth.types.UserStatus;
import com.junodx.api.models.auth.types.UserType;
import com.junodx.api.security.ResourceOwnerValidation;
import com.junodx.api.services.ServiceBase;
import com.junodx.api.services.auth.UserDetailsImpl;
import com.junodx.api.services.auth.UserNotInClientException;
import com.junodx.api.services.auth.UserNotOwnerException;
import com.junodx.api.services.auth.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.junodx.api.services.auth.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/users")
public class UserController {

	@Value("${jdx.controllers.maxPageSize}")
	private int maxPageSize;

	@Autowired
	private UserServiceImpl userService;

	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	private ObjectMapper mapper;

	@Autowired
	private UserMapStructMapper userMapper;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	public UserController() {
		this.mapper = new ObjectMapper();
	}

	@Operation(description = "Fetch user by user id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User fetched"),
			@ApiResponse(responseCode = "200", description = "Cannot find user") })
	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('ROLE_LAB_DIRECTOR')" + "|| hasRole('ROLE_ADMIN')" + "|| hasRole('ROLE_CSR')"
			+ "|| hasRole('ROLE_LAB_SUPERVISOR')" + "|| hasRole('ROLE_TECHNICIAN')" + "|| hasRole('ROLE_PATIENT')")
	public ResponseEntity getUser(@PathVariable String userId) {
		Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
		UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

		try {
			if (ResourceOwnerValidation.resourceRequiresRestrictedScope(userContext)) {
				if (!ResourceOwnerValidation.validateOwnership(userContext, userId, null))
					return ResponseEntity.badRequest().body(
							"Cannot allow access to resource, requesting user is not owner or have sufficient privileges to this data ");
			}
		} catch (UserNotOwnerException | UserNotInClientException e) {
			return ResponseEntity.badRequest().body(
					"Cannot allow access to resource, requesting user is not owner or have sufficient privileges to this data "
							+ e.getMessage());
		}

		Optional<User> u;
		try {
			u = userService.findOne(userId);
		} catch (NoSuchElementException ex) {
			return ResponseEntity.ok("Cannot find user: " + userId);
		}

		if (u.isPresent())
			return ResponseEntity.ok(u.get());

		return ResponseEntity.ok("Cannot find user: " + userId);
	}

	@Operation(description = "Fetch user roles by user id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Roles fetched"),
			@ApiResponse(responseCode = "200", description = "Cannot find user") })
	@GetMapping("/{userId}/roles")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity getUserRoles(@PathVariable String userId) {
		Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
		UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

		Optional<User> u;
		try {
			return ResponseEntity.ok().body(userService.getRoles(userId));
		} catch (NoSuchElementException ex) {
			return ResponseEntity.ok("Cannot find user: " + userId);
		}
	}

	@Operation(description = "Create user role for a user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Role created for a user"),
			@ApiResponse(responseCode = "200", description = "Cannot find user") })
	@PostMapping("/{userId}/roles")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity addUserRole(@PathVariable("userId") String userId, @RequestBody RolePayload role) {
		Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
		UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

		Optional<User> u;
		try {
			return ResponseEntity.ok().body(userService.addRole(userId, role.getRole(), userContext));
		} catch (NoSuchElementException ex) {
			return ResponseEntity.ok("Cannot find user: " + userId);
		}
	}

	@Operation(description = "Delete user role for a user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Role deleted for a user"),
			@ApiResponse(responseCode = "200", description = "Cannot find user") })
	@DeleteMapping("/{userId}/roles")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity removeUserRole(@PathVariable String userId, @RequestBody RolePayload role) {
		Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
		UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

		Optional<User> u;
		try {
			return ResponseEntity.ok().body(userService.removeRole(userId, role.getRole(), userContext));
		} catch (NoSuchElementException ex) {
			return ResponseEntity.ok("Cannot find user: " + userId);
		}
	}

	@Operation(description = "Lists all the users by query params")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "users fetched"),
			@ApiResponse(responseCode = "404", description = "user not found") })
	@GetMapping("/search")
	@PreAuthorize("hasRole('ROLE_LAB_DIRECTOR')" + "|| hasRole('ROLE_ADMIN')" + "|| hasRole('ROLE_CSR')"
			+ "|| hasRole('ROLE_LAB_SUPERVISOR')" + "|| hasRole('ROLE_TECHNICIAN')")
	public ResponseEntity<?> search(@RequestParam("page") Optional<Integer> pg,
			@RequestParam("size") Optional<Integer> ps, @RequestParam("bylastname") Optional<String> lastName,
			@RequestParam("byfirstname") Optional<String> firstName, @RequestParam("bycity") Optional<String> city,
			@RequestParam("bystate") Optional<String> state, @RequestParam("bypostalcode") Optional<String> postalCode,
			@RequestParam("byemail") Optional<String> email, @RequestParam("bypracticeid") Optional<String> practiceId,
			@RequestParam("byproviderid") Optional<String> providerId,
			@RequestParam("byxifinid") Optional<String> xifinId, @RequestParam("bystripeid") Optional<String> stripeId,
			@RequestParam("bytype") Optional<UserType> type, @RequestParam("bystatus") Optional<UserStatus> status,
			@RequestParam("include") Optional<String> includeFields) {
		// Grab the userId from the security context (access token) and use that Role is
		// patient
		Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
		UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

		int page = 0;
		int pageSize = maxPageSize;

		if (pg.isPresent())
			page = pg.get().intValue();
		if (ps.isPresent())
			pageSize = ps.get().intValue();

		// Limit the pageSize
		if (pageSize > maxPageSize)
			pageSize = maxPageSize;

		String iFields = "";
		if (includeFields.isPresent())
			iFields = includeFields.get();

		Page<User> userResp = null;
		Pageable pageable = PageRequest.of(page, pageSize);

		UserType selectedType = null;
		if (type.isPresent())
			selectedType = type.get();
		else
			selectedType = UserType.STANDARD;

		userResp = userService.search(lastName, firstName, state, city, postalCode, email, practiceId, providerId,
				xifinId, stripeId, type, status, pageable);

		if (userResp != null) {
			List<UserBatchDto> transitionUsers = userMapper.userToUserBatchDtos(userResp.getContent());
			ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();
			return ResponseEntity.ok().header("link",
					
					ControllerUtils.buildLinkHeader(ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString(),
							page, pageSize, userResp.getTotalPages()))
					.header("X-Jdx-Total-Results",
							ControllerUtils.buildQueryResultsDataHeader(userResp.getTotalElements()))
					.header("X-Jdx-Max-Page-Size", ControllerUtils.buildMaxPageSizeDataheader(maxPageSize))
					.header("X-Jdx-Current-Page",
							ControllerUtils.buildMaxPageSizeDataheader(userResp.getPageable().getPageNumber()))
					.body(transitionUsers);
		}

		return ResponseEntity.notFound().build();
	}

	@Operation(description = "Create user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "user created"),
			@ApiResponse(responseCode = "400", description = "Bad request") })
	@PostMapping("")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> create(@RequestBody User user) {
		// Grab the userId from the security context (access token) and use that Role is
		// patient
		Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
		UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

		try {
			return ResponseEntity.ok().body(userService.save(user, userContext));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(new MessageResponse(LogCode.RESOURCE_CREATE_ERROR.label, LogCode.RESOURCE_CREATE_ERROR.code,
							Calendar.getInstance().getTime(), "Unable to create the patient for email "));
		}
	}

	@Operation(description = "Register user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "user registration"),
			@ApiResponse(responseCode = "400", description = "Bad request") })
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody RegisterUserPayload user) {
		return ResponseEntity.ok().body(userService.register(user));
	}

	@Operation(description = "Resend user verification code")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "user verification code resent"),
			@ApiResponse(responseCode = "400", description = "Bad request") })
	@GetMapping("/code")
	public ResponseEntity<?> sendVerificationCode(@RequestParam("email") String email,
			@RequestParam("clientid") String clientId) {
		try {
			email = email.replaceAll(" ", "+");

			return ResponseEntity.ok().body(userService.sendVerificationCode(email, clientId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(new MessageResponse(LogCode.RESOURCE_CREATE_ERROR.label, LogCode.RESOURCE_CREATE_ERROR.code,
							Calendar.getInstance().getTime(), "Unable to resend verification code for user"));
		}
	}

	@Operation(description = "Verify user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "user verify"),
			@ApiResponse(responseCode = "400", description = "Bad request") })
	@PostMapping("/verify")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> verifyUser(@RequestBody UserVerificationPayload payload) {
		// Grab the userId from the security context (access token) and use that Role is
		// patient
		// Authentication authentication = (UsernamePasswordAuthenticationToken)
		// SecurityContextHolder.getContext().getAuthentication();
		// UserDetailsImpl userContext = (UserDetailsImpl)
		// authentication.getPrincipal();

		if (payload == null)
			return ResponseEntity.badRequest().body("Cannot find request payload");

		try {
			return ResponseEntity.ok().body(userService.verify(payload));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(new MessageResponse(LogCode.RESOURCE_CREATE_ERROR.label, LogCode.RESOURCE_CREATE_ERROR.code,
							Calendar.getInstance().getTime(), "Unable to create the patient for email "));
		}
	}

	@Operation(description = "Verify if email already exists.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "email available"),
			@ApiResponse(responseCode = "400", description = "Bad request") })
	@GetMapping("/email-exists")
	public ResponseEntity<?> verifyIfEmailAddressExistsAlready(@RequestParam("email") String email,
			@RequestParam("clientid") Optional<String> clientId) {
		if (email == null)
			return ResponseEntity.badRequest().body("Cannot find email to test against");

		email = email.replaceAll(" ", "+");

		try {
			return ResponseEntity.ok().body(userService.doesEmailExistForClientId(email, clientId));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(new MessageResponse(LogCode.RESOURCE_CREATE_ERROR.label, LogCode.RESOURCE_CREATE_ERROR.code,
							Calendar.getInstance().getTime(), "Unable to determine uniqueness for email address "));
		}
	}

	@Operation(description = "Forgot password")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "forgot password"),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "2010", description = "Unable to send user forgot password email") })
	@PostMapping("/forgot-password")
	public ResponseEntity<?> generateForgotPassword(@RequestBody UserForgotPasswordPayload payload) {
		if (payload == null)
			return ResponseEntity.badRequest().body("Cannot find request payload");

		try {
			userService.forgotPassword(payload);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(new MessageResponse(LogCode.RESOURCE_CREATE_ERROR.label, LogCode.RESOURCE_CREATE_ERROR.code,
							Calendar.getInstance().getTime(), "Unable to send user forgot password email"));
		}
	}

	@Operation(description = "Update user password")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "password updated"),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "2010", description = "Unable to update password for user") })
	@PostMapping("/update-password")
	public ResponseEntity<?> updatePassword(@RequestBody UserUpdatePasswordPayload payload) {
		if (payload == null)
			return ResponseEntity.badRequest().body("Cannot find request payload");

		try {
			userService.updatePassword(payload);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(new MessageResponse(LogCode.RESOURCE_CREATE_ERROR.label, LogCode.RESOURCE_CREATE_ERROR.code,
							Calendar.getInstance().getTime(), "Unable to update password for user"));
		}
	}

	@Operation(description = "Create user preferences")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "user preferences created"),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "2010", description = "Unable to update preferences for user") })
	@PostMapping("/update-preferences/{userId}")
	@PreAuthorize("hasRole('ROLE_CSR')" + "|| hasRole('ROLE_ADMIN')" + "|| hasRole('ROLE_PATIENT')")
	public ResponseEntity<?> updatePreferences(@PathVariable("userId") String userId,
			@RequestBody UserUpdatePreferencesPayload payload) {
		Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
		UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

		if (payload == null || userId == null)
			return ResponseEntity.badRequest().body("Cannot find request payload");

		try {
			if (ResourceOwnerValidation.resourceRequiresRestrictedScope(userContext)) {
				if (!ResourceOwnerValidation.validateOwnership(userContext, userId, null))
					return ResponseEntity.badRequest().body(
							"Cannot allow access to resource, requesting user is not owner or have sufficient privileges to this data ");
			}
		} catch (UserNotOwnerException | UserNotInClientException e) {
			return ResponseEntity.badRequest().body(
					"Cannot allow access to resource, requesting user is not owner or have sufficient privileges to this data "
							+ e.getMessage());
		}

		try {
			return ResponseEntity.ok().body(userService.updatePreferences(userId, payload, userContext));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(new MessageResponse(LogCode.RESOURCE_UPDATE_ERROR.label, LogCode.RESOURCE_UPDATE_ERROR.code,
							Calendar.getInstance().getTime(), "Unable to update preferences for user"));
		}
	}

	@Operation(description = "Update user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "user updated"),
			@ApiResponse(responseCode = "400", description = "Bad request"),
			@ApiResponse(responseCode = "2010", description = "Cannot allow access to resource") })
	@PatchMapping("")
	@PreAuthorize("hasRole('ROLE_LAB_DIRECTOR')" + "|| hasRole('ROLE_ADMIN')" + "|| hasRole('ROLE_CSR')"
			+ "|| hasRole('ROLE_LAB_SUPERVISOR')" + "|| hasRole('ROLE_TECHNICIAN')" + "|| hasRole('ROLE_PATIENT')")
	public ResponseEntity<?> update(@RequestBody UserUpdatePayload user) {
		// Grab the userId from the security context (access token) and use that Role is
		// patient
		Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
		UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

		JsonNode node = null;

		try {
			if (user == null)
				return ResponseEntity.badRequest().body("Cannot find user information to update");

			// node = mapper.readTree(user);
			// String userId = String.valueOf(node.get("id"));
			if (user.getId() == null)
				return ResponseEntity.badRequest().body("Cannot find user information to update");

			if (ResourceOwnerValidation.resourceRequiresRestrictedScope(userContext)) {
				if (!ResourceOwnerValidation.validateOwnership(userContext, user.getId(), null))
					return ResponseEntity.badRequest().body(
							"Cannot allow access to resource, requesting user is not owner or have sufficient privileges to this data ");
			}
		} catch (UserNotOwnerException | UserNotInClientException e) {
			return ResponseEntity.badRequest().body(
					"Cannot allow access to resource, requesting user is not owner or have sufficient privileges to this data "
							+ e.getMessage());
		}

		try {
			User resp = userService.update(user, userContext);
			if (resp != null) {
				return ResponseEntity.ok().body(resp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest()
					.body(new MessageResponse(LogCode.RESOURCE_UPDATE_ERROR.label, LogCode.RESOURCE_UPDATE_ERROR.code,
							Calendar.getInstance().getTime(), "Unable to update the patient for email "));
		}

		return ResponseEntity.badRequest()
				.body(new MessageResponse(LogCode.RESOURCE_UPDATE_ERROR.label, LogCode.RESOURCE_UPDATE_ERROR.code,
						Calendar.getInstance().getTime(), "Unable to update the patient for email "));
	}

	@Operation(description = "Delete user")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "user deleted"),
			@ApiResponse(responseCode = "404", description = "user not found") })
	@DeleteMapping("/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteUser(@PathVariable("userId") String userId) {
		try {
			userService.delete(userId);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().build();
	}

	@PatchMapping("/email")
	@PreAuthorize("hasRole('ROLE_ADMIN')" + "|| hasRole('ROLE_CSR')" + "|| hasRole('ROLE_PATIENT')")
	public ResponseEntity<?> updateEmail(@RequestBody EmailChangePayload emailChange) {
		// Grab the userId from the security context (access token) and use that Role is
		// patient
		Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
		UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

		if (emailChange == null)
			return ResponseEntity.badRequest().body("Cannot find request for email change");

		try {
			return ResponseEntity.ok().body(userService.startEmailUpdate(emailChange.getUserId(),
					emailChange.getChangeToEmail(), userContext.getClientId(), userContext));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Cannot process request to change email address");
		}
	}

	@PatchMapping("/email/verify")
	@PreAuthorize("hasRole('ROLE_ADMIN')" + "|| hasRole('ROLE_CSR')" + "|| hasRole('ROLE_PATIENT')")
	public ResponseEntity<?> verifyUpdateEmail(@RequestBody UserVerificationPayload emailChangeVerification) {
		// Grab the userId from the security context (access token) and use that Role is
		// patient
		Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
		UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();

		if (emailChangeVerification == null)
			return ResponseEntity.badRequest().body("Cannot find request for email change");

		try {
			return ResponseEntity.ok().body(userService.finishEmailUpdate(emailChangeVerification, userContext));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body("Cannot process request to change email address: " + e.getMessage());
		}
	}

	@PostMapping("/change-password")
	@PreAuthorize("hasRole('ROLE_ADMIN')" + "|| hasRole('ROLE_CSR')" + "|| hasRole('ROLE_PATIENT')")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordPayload payload) {
		Authentication authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext()
				.getAuthentication();
		UserDetailsImpl userContext = (UserDetailsImpl) authentication.getPrincipal();
		if (payload == null)
			return ResponseEntity.badRequest().body("Cannot find request payload");

		try {
			if (userService.changePassword(payload, userContext))
				return ResponseEntity.ok().build();
			else
				return ResponseEntity.badRequest().body("Cannot change password");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Unable to update password: " + e.getMessage());
		}
	}
}
