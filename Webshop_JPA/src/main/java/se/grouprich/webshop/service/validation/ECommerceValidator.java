package se.grouprich.webshop.service.validation;

import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.Role;
import se.grouprich.webshop.model.User;
import se.grouprich.webshop.model.status.UserStatus;
import se.grouprich.webshop.repository.OrderRepository;
import se.grouprich.webshop.repository.ProductRepository;
import se.grouprich.webshop.repository.UserRepository;

public final class ECommerceValidator
{
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	public ECommerceValidator(final UserRepository userRepository, final OrderRepository orderRepository, final ProductRepository productRepository)
	{
		this.userRepository = userRepository;
		this.orderRepository = orderRepository;
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

	public boolean isActiveAdmin(final User admin)
	{
		User fetchedUser = userRepository.findById(admin.getId());
		if (fetchedUser.getRole().equals(Role.ADMIN) && isActiveUser(fetchedUser))
		{
			return true;
		}
		return false;
	}

	public boolean hasPermission(final User user1, final User user2)
	{
		User fetchedUser = userRepository.findById(user1.getId());
		if (areSameUsers(fetchedUser, user2) && isActiveUser(fetchedUser))
		{
			return true;
		}
		return false;
	}

	private boolean areSameUsers(final User user1, final User user2)
	{
		if (user1.getId().equals(user2.getId()))
		{
			return true;
		}
		return false;
	}

	private boolean isActiveUser(final User user)
	{
		if (user.getStatus().equals(UserStatus.ACTIVE))
		{
			return true;
		}
		return false;
	}

	public boolean hasChangedRoleOrUserStatus(final User user)
	{
		User fetchedUser = userRepository.findById(user.getId());
		if (fetchedUser.getRole().equals(user.getRole()) && fetchedUser.getStatus().equals(user.getStatus()))
		{
			return false;
		}
		return true;
	}

	public boolean hasChangedOrderStatus(final Order order)
	{
		Order fetchedOrder = orderRepository.findById(order.getId());
		if (fetchedOrder.getStatus().equals(order.getStatus()))
		{
			return false;
		}
		return true;
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
