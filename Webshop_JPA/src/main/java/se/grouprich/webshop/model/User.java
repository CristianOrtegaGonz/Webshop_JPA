package se.grouprich.webshop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import se.grouprich.webshop.exception.ValidationException;
import se.grouprich.webshop.model.status.UserStatus;

@Entity
@NamedQueries(value = { @NamedQuery(name = "User.FetchAll", query = "SELECT u FROM User u"),
		@NamedQuery(name = "User.FetchUserByUsername", query = "SELECT u FROM User u WHERE u.username = :username") })
public class User extends AbstractEntity implements Serializable
{
	@Transient
	private static final long serialVersionUID = 8550124813033398565L;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserStatus status;

	protected User()
	{
	}

	public User(String username, String password, String firstName, String lastName) throws ValidationException
	{
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = Role.CUSTOMER;
		status = UserStatus.PENDING_ACTIVATION; 
	}
	
	public User(String username, String password, String firstName, String lastName, Role role) throws ValidationException
	{
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		status = UserStatus.PENDING_ACTIVATION; 
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public Role getRole()
	{
		return role;
	}

	public String getName()
	{
		return firstName + " " + lastName;
	}
	
	public UserStatus getStatus()
	{
		return status;
	}

	public User setUsername(final String username)
	{
		this.username = username;
		return this;
	}

	public User setRole(Role role)
	{
		this.role = role;
		return this;
	}
	
	public User setStatus(UserStatus status)
	{
		this.status = status;
		return this;
	}
	
	public void changePassword(final String oldPassword, final String newPassword)
	{
		if (oldPassword.equals(this.password))
		{
			this.password = newPassword;
		}
	}

	@Override
	public boolean equals(Object other)
	{
		if (this == other)
		{
			return true;
		}

		if (other instanceof User)
		{
			User otherUser = (User) other;
			return username.equals(otherUser.username) && password.equals(otherUser.password) && role.equals(otherUser.role)
					&& status.equals(otherUser.status);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += username.hashCode() * 37;
		result += password.hashCode() * 37;
		result += role.hashCode() * 37;
		result += status.hashCode() * 37;

		return result;
	}

	@Override
	public String toString()
	{
		return "User [username=" + username + ", password=" + password + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", role=" + role + ", status=" + status + "]";
	}
}
