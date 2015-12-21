package se.grouprich.webshop.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;

import se.grouprich.webshop.exception.UserRegistrationException;

@Entity
public class User extends AbstractEntity implements Serializable
{
	@Transient
	private static final long serialVersionUID = 8550124813033398565L;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String role;

	protected User()
	{
	}

	public User(String email, String password, String firstName, String lastName, String role) throws UserRegistrationException
	{
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
	}

	public String getEmail()
	{
		return email;
	}

	public String getPassword()
	{
		return password;
	}

	public String getRole()
	{
		return role;
	}

	public void setEmail(final String email)
	{
		this.email = email;
	}

	public void setPassword(final String password)
	{
		this.password = password;
	}

	public String getName()
	{
		return firstName + " " + lastName;
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
			return email.equals(otherUser.email) && password.equals(otherUser.password) && role.equals(otherUser.role);
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result += email.hashCode() * 37;
		result += password.hashCode() * 37;
		result += role.hashCode() * 37;

		return result;
	}

	@Override
	public String toString()
	{
		return "[Name] " + getName() + ", [E-mail] " + email + ", [Role] " + role;
	}
}
