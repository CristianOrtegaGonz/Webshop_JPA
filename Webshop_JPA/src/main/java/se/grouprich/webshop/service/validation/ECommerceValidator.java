package se.grouprich.webshop.service.validation;

import se.grouprich.webshop.repository.ProductRepository;
import se.grouprich.webshop.repository.UserRepository;

public final class ECommerceValidator
{
	private final UserRepository userRepository;
	private final ProductRepository productRepository;

	public ECommerceValidator(final UserRepository userRepository, final ProductRepository productRepository)
	{
		this.userRepository = userRepository;
		this.productRepository = productRepository;
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

	public boolean usernameAlreadyExists(final String username)
	{
		if (userRepository.fetchUserByUsername(username) != null)
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
	
	public boolean productNameAlreadyExists(final String productName)
	{
		if (!productRepository.fetchProductsByProductName(productName).isEmpty())
		{
			return true;
		}
		return false;
	}
}
