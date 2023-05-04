package com.example.adverts.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreateDto implements Serializable {

    private UUID id;
    @NotBlank(message = "First name must not be null")
    private String firstname;
    @NotBlank(message = "Last name must not be null")
    private String lastname;
    @Email(message = "That's not a valid email")
    @NotBlank(message = "Email must not be null")
    private String email;
    @Email(message = "That's not a valid username")
    @NotBlank(message = "Username must not be null")
    private String username;
    @NotBlank(message = "Password must not be null")
    private String password;
    private String role;

    public UserCreateDto(String firstname, String lastname, String email, String username, String password, String role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
    }

	

	public UserCreateDto() {
		super();
	}



	public UserCreateDto(UUID id, @NotBlank(message = "First name must not be null") String firstname,
			@NotBlank(message = "Last name must not be null") String lastname,
			@Email(message = "That's not a valid email") @NotBlank(message = "Email must not be null") String email,
			@Email(message = "That's not a valid username") @NotBlank(message = "Username must not be null") String username,
			@NotBlank(message = "Password must not be null") String password, String role) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.username = username;
		this.password = password;
		this.role = role;
	}



	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
}
