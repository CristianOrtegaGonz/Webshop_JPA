package se.grouprich.webshop.model;

import java.io.Serializable;

import se.grouprich.webshop.exception.UserRegistrationException;
import se.grouprich.webshop.idgenerator.Identifiable;

public final class User implements Serializable, Identifiable<String>
{
	private static final long serialVersionUID = 8550124813033398565L;
	private String userId;
	private String email;
	private String password;
	private final String firstName;
	private final String lastName;
	private final String role;

	public User(String userId, String email, String password, String firstName, String lastName, String role) throws UserRegistrationException
	{
		this.userId = userId;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
	}
	
	@Override
	public String getId()
	{
		return userId;
	}

	@Override
	public void setId(final String userId)
	{
		this.userId = userId;
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
