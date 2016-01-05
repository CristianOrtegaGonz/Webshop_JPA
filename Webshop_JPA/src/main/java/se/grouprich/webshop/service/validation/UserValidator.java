package se.grouprich.webshop.service.validation;

import se.grouprich.webshop.model.User;
import se.grouprich.webshop.repository.UserRepository;

public final class UserValidator
{
	private final UserRepository userRepository;

	public UserValidator(UserRepository userRepository)
	{
		this.userRepository = userRepository;
	}

	public boolean isValidPassword(final String password)
	{
		boolean digits = false;
		boolean versal = false;
		boolean specialCharacter = false;
		int counterNumbers = 0;

		if (password == null || password.trim().isEmpty())
		{
			return false;
		}

		for (int i = 0; i < password.length(); i++)
		{
			if (password.substring(i, i + 1).matches("[A-ZÅÖÄa-zåöä\\d\\p{Punct}]+"))
			{
				if (password.substring(i, i + 1).matches("\\d+"))
				{
					counterNumbers++;

					if (counterNumbers >= 2)
					{
						digits = true;
					}
				}

				if (password.substring(i, i + 1).matches("[A-ZÅÄÖ]+"))
				{
					versal = true;
				}

				if (password.substring(i, i + 1).matches("\\p{Punct}+"))
				{
					specialCharacter = true;
				}
			}
		}
		return (digits && versal && specialCharacter);
	}

	public boolean alreadyExists(final String username)
	{
		if (!userRepository.fetchUsersByUsername(username).isEmpty())
		{
			return true;
		}
		return false;
	}

	public boolean isLengthWithinRange(final String username)
	{
		if (username != null && !username.trim().isEmpty())
		{
			if (username.length() <= 30)
			{
				return true;
			}
		}
		return false;
	}

	public boolean isActivatedAdmin(User user)
	{
		// "" av "UserStatus.ACTIVATED" ska tas bort efter enum UserStatus har
		// skapats.
		if (user.getRole().equals("admin") && user.getStatus().equals("UserStatus.ACTIVATED"))
		{
			return true;
		}
		return false;
	}
}