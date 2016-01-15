package se.grouprich.webshop.service;

import java.util.List;

import se.grouprich.webshop.exception.OrderException;
import se.grouprich.webshop.exception.StorageException;
import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.OrderRow;
import se.grouprich.webshop.model.Product;
import se.grouprich.webshop.model.User;
import se.grouprich.webshop.model.status.OrderStatus;
import se.grouprich.webshop.model.status.ProductStatus;
import se.grouprich.webshop.model.status.UserStatus;
import se.grouprich.webshop.repository.OrderRepository;
import se.grouprich.webshop.repository.ProductRepository;
import se.grouprich.webshop.repository.UserRepository;
import se.grouprich.webshop.service.validation.ECommerceValidator;

public final class ECommerceService
{
	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final ECommerceValidator eCommerceValidator;

	public ECommerceService(final OrderRepository orderRepository, final UserRepository userRepository, final ProductRepository productRepository,
			final ECommerceValidator userValidator)
	{
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.eCommerceValidator = userValidator;
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

	public ECommerceValidator getUserValidator()
	{
		return eCommerceValidator;
	}

	public Product fetchProductById(final Long id)
	{
		return productRepository.findById(id);
	}

	public User fetchUserById(final Long id)
	{
		return userRepository.findById(id);
	}

	public Order fetchOrderById(final Long id)
	{
		return orderRepository.findById(id);
	}

	public List<Product> fetchAllProducts(final User user)
	{
		return productRepository.fetchAll();
	}

	public List<User> fetchAllUsers()
	{
		return userRepository.fetchAll();
	}

	public List<Order> fetchAllOrders()
	{
		return orderRepository.fetchAll();
	}

	public Product createProduct(final Product product) throws StorageException
	{
		if (eCommerceValidator.productNameAlreadyExists(product.getProductName()))
		{
			throw new StorageException("Product with name: " + product.getProductName() + " already exists");
		}
		return productRepository.saveOrUpdate(product);
	}

	public User createUser(final User user) throws StorageException
	{
		if (eCommerceValidator.usernameAlreadyExists(user.getUsername()))
		{
			throw new StorageException("User with username '" + user.getUsername() + "' already exists");
		}
		if (!eCommerceValidator.isLengthWithinRange(user.getUsername()))
		{
			throw new IllegalArgumentException("Username that is longer than 30 characters is not allowed");
		}
		if (!eCommerceValidator.isValidPassword(user.getPassword()))
		{
			throw new IllegalArgumentException("Password must have at least an uppercase letter, two digits and a special character such as !@#$%^&*(){}[]");
		}
		return userRepository.saveOrUpdate(user);
	}

	public Order createOrder(final Order order) throws OrderException
	{
		if (order.getTotalPrice() > 50000.00)
		{
			throw new OrderException("Total price exceeding SEK 50,000 is not allowed");
		}
		order.updateStockQuantities(order.getOrderRows());
		order.setStatus(OrderStatus.PLACED);
		return orderRepository.saveOrUpdate(order);
	}

	public Product updateProduct(final Product product) throws StorageException
	{
		if (product.getId() == null)
		{
			throw new StorageException("Cannot update product. Product does not exists");
		}
		return productRepository.saveOrUpdate(product);
	}

	public User updateUser(final User user) throws StorageException
	{
		if (user.getId() == null)
		{
			throw new StorageException("Cannot update user. User does not exists");
		}
		return userRepository.saveOrUpdate(user);
	}

	public Order updateOrder(final Order order) throws OrderException, StorageException
	{
		if (order.getId() == null)
		{
			throw new StorageException("Cannot update order. Order does not exists");
		}
		order.updateStockQuantities(order.getOrderRows());
		return orderRepository.saveOrUpdate(order);
	}

	public List<Product> searchProductsBasedOnProductName(final String keyword)
	{
		return productRepository.searchProductsBasedOnProductName(keyword);
	}

	public User fetchUserByUsername(final String username)
	{
		if (userRepository.fetchUsersByUsername(username).size() > 0)
		{
			return userRepository.fetchUsersByUsername(username).get(0);
		}
		return null;
	}

	public Product changeProductStatus(final Product product, final ProductStatus status) throws StorageException
	{
		product.setStatus(status);
		return productRepository.saveOrUpdate(product);
	}

	public User changeUserStatus(final User user, final UserStatus status) throws StorageException
	{
		user.setStatus(status);
		return userRepository.saveOrUpdate(user);
	}

	public Order changeOrderStatus(final Order order, final OrderStatus status) throws OrderException, StorageException
	{
		order.setStatus(status);
		return orderRepository.saveOrUpdate(order);
	}

	public List<Order> fetchOrdersByUser(final User user, final User customer)
	{
		return orderRepository.fetchOrdersByUser(customer);
	}

	public List<Order> fetchOrdersByStatus(final User admin, final OrderStatus orderStatus)
	{
		return orderRepository.fetchOrdersByStatus(orderStatus);
	}

	public List<Order> fetchOrdersByMinimumValue(final User admin, final Double minimumValue)
	{
		return orderRepository.fetchOrdersByMinimumValue(minimumValue);
	}

	public User activateUser(final User user)
	{
		user.setStatus(UserStatus.ACTIVE);
		return userRepository.saveOrUpdate(user);
	}

	public Order addOrderRows(final User customer, final Order order, final OrderRow... orderRows) throws OrderException, StorageException
	{
		if (order.getId() == null)
		{
			throw new StorageException("Cannot add items. Order does not exists");
		}
		order.addOrderRows(orderRows);
		order.updateStockQuantities(order.getOrderRows());
		return order;
	}
}
