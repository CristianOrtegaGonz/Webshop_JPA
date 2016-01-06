package se.grouprich.webshop.service;

import java.util.List;

import se.grouprich.webshop.exception.OrderException;
import se.grouprich.webshop.exception.PermissionException;
import se.grouprich.webshop.exception.StorageException;
import se.grouprich.webshop.exception.ValidationException;
import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.Product;
import se.grouprich.webshop.model.User;
import se.grouprich.webshop.model.status.OrderStatus;
import se.grouprich.webshop.model.status.ProductStatus;
import se.grouprich.webshop.model.status.UserStatus;
import se.grouprich.webshop.repository.OrderRepository;
import se.grouprich.webshop.repository.ProductRepository;
import se.grouprich.webshop.repository.UserRepository;
import se.grouprich.webshop.service.validation.ProductValidator;
import se.grouprich.webshop.service.validation.UserValidator;

public final class ECommerceService
{
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final ProductValidator productValidator;
	private final UserValidator userValidator;

	public ECommerceService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository,
			ProductValidator productValidator, UserValidator userValidator)
	{
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.productValidator = productValidator;
		this.userValidator = userValidator;
	}

	public ProductRepository getProductRepository()
	{
		return productRepository;
	}

	public UserRepository getUserRepository()
	{
		return userRepository;
	}

	public OrderRepository getOrderRepository()
	{
		return orderRepository;
	}

	public ProductValidator getProductValidator()
	{
		return productValidator;
	}

	public UserValidator getUserValidator()
	{
		return userValidator;
	}

	public Product fetchProductById(Long id)
	{
		return productRepository.findById(id);
	}

	public User fetchUserById(Long id)
	{
		return userRepository.findById(id);
	}

	public Order fetchOrderById(Long id)
	{
		return orderRepository.findById(id);
	}

	public List<Product> fetchAllProducts()
	{
		return productRepository.fetchAll();
	}

	public List<User> fetchAllUser()
	{
		return userRepository.fetchAll();
	}

	public List<Order> fetchAllOrders()
	{
		return orderRepository.fetchAll();
	}

	public Product createProduct(User user, String productName, double price, int stockQuantity, ProductStatus status) throws StorageException, PermissionException
	{
		if (!userValidator.isActiveAdmin(user))
		{
			throw new PermissionException("No permission to create products");
		}
		if (productValidator.alreadyExists(productName))
		{
			throw new StorageException("Product with name: " + productName + " already exists");
		}
		Product product = new Product(productName, price, stockQuantity, status);
		return productRepository.saveOrUpdate(product);
	}

	public User createUser(String username, String password, String firstName, String lastName) throws StorageException, ValidationException
	{
		if (userValidator.alreadyExists(username))
		{
			throw new StorageException("User with username: " + username + " already exists");
		}
		if (!userValidator.isLengthWithinRange(username))
		{
			throw new ValidationException("Username that is longer than 30 characters is not allowed");
		}
		if (!userValidator.isValidPassword(password))
		{
			throw new ValidationException("Password must have at least an uppercase letter, two digits and a special character such as !@#$%^&*(){}[]");
		}
		User user = new User(username, password, firstName, lastName);
		return userRepository.saveOrUpdate(user);
	}

	public Order createOrder(User user, Order order) throws OrderException, PermissionException
	{
		if (!userValidator.isActiveAdmin(user) && !userValidator.hasPermissionToAccess(user, order.getUser()))
		{
			throw new PermissionException("No permission to create orders");
		}
		if (order.getTotalPrice() > 50000.00)
		{
			throw new OrderException("We can not accept the total price exceeding SEK 50,000");
		}
		if (userRepository.findById(order.getUser().getId()) == null)
		{
			order.updateStockQuantity();
			return orderRepository.saveOrUpdate(order);
		}
		else
		{
			order.updateStockQuantity();
			return orderRepository.merge(order);
		}
	}

	public Product updateProduct(User user, Product product) throws PermissionException
	{
		if (!userValidator.isActiveAdmin(user))
		{
			throw new PermissionException("No permission to update products");
		}
		return productRepository.saveOrUpdate(product);
	}

	public User updateUser(User user, User userToUpdate) throws PermissionException
	{
		if (!userValidator.isActiveAdmin(user) && !userValidator.hasPermissionToAccess(user, userToUpdate))
		{
			throw new PermissionException("No permission to update user");
		}
		return userRepository.saveOrUpdate(userToUpdate);
	}

	public Order updateOrder(User user, Order order) throws PermissionException
	{
		if (!userValidator.isActiveAdmin(user))
		{
			throw new PermissionException("No permission to update orders");
		}
		return orderRepository.saveOrUpdate(order);
	}

	public List<Product> searchProductsBasedOnProductName(String keyword)
	{
		return productRepository.searchProductsBasedOnProductName(keyword);
	}

	public User fetchUserByUsername(User user, String username) throws PermissionException
	{
		if (!userValidator.isActiveAdmin(user) && !user.getUsername().equals(username))
		{
			throw new PermissionException("No permission to fetch user by username");
		}
		return userRepository.fetchUsersByUsername(username).get(0);
	}

	public Product changeProductStatus(User user, Product product, ProductStatus status) throws PermissionException
	{
		if (!userValidator.isActiveAdmin(user))
		{
			throw new PermissionException("No permission to change product status");
		}
		product.setStatus(status);
		return productRepository.saveOrUpdate(product);
	}

	public List<Order> fetchOrdersByUser(User user, User customer) throws PermissionException
	{
		if (!userValidator.isActiveAdmin(user) && !userValidator.hasPermissionToAccess(user, customer))
		{
			throw new PermissionException("No permission to fetch orders by user");
		}
		return orderRepository.fetchOrdersByUser(customer);
	}

	public List<Order> fetchOrdersByStatus(OrderStatus orderStatus)
	{
		return orderRepository.fetchOrdersByStatus(orderStatus);
	}

	public List<Order> fetchOrdersByMinimumValue(Double minimumValue)
	{
		return orderRepository.fetchOrdersByMinimumValue(minimumValue);
	}

	public User activateUser(User user)
	{
		user.setStatus(UserStatus.ACTIVE);
		return userRepository.saveOrUpdate(user);
	}
}