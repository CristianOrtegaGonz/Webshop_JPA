package se.grouprich.webshop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import se.grouprich.webshop.exception.UserRegistrationException;

@Entity
public class User extends AbstractEntity implements Serializable
{
	@Transient
	private static final long serialVersionUID = 8550124813033398565L;
	@Column
	private String username;
	@Column
	private String password;
	@Column
	private String firstName;
	@Column
	private String lastName;
	@Column
	private String role;
	@Column
	private String status;

	protected User()
	{
	}

	public User(String username, String password, String firstName, String lastName) throws UserRegistrationException
	{
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = "Customer";
		// Fick idén från den här sidan om user status. http://developers.socialcast.com/admin/managing-users/user-status/
		status = "Pending Activation";
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public String getRole()
	{
		return role;
	}
	
	public String getStatus()
	{
		return status;
	}
	
	public String getName()
	{
		return firstName + " " + lastName;
	}

	public void setEmail(final String email)
	{
		this.username = email;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}
	
	public void setRole(String role)
	{
		this.role = role;
	}

	public void setStatus(String status)
	{
		this.status = status;
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
			return username.equals(otherUser.username) && password.equals(otherUser.password) && role.equals(otherUser.role) && status.equals(otherUser.status);
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
		return "User [username=" + username + ", password=" + password + ", firstName=" + firstName + ", lastName=" + lastName + ", role=" + role + ", status=" + status + "]";
	}
}
