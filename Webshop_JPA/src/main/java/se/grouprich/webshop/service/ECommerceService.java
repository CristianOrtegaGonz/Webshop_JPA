package se.grouprich.webshop.service;

import java.util.List;

import se.grouprich.webshop.exception.OrderException;
import se.grouprich.webshop.exception.PermissionException;
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

	public ECommerceService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository,
			ECommerceValidator userValidator)
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

	// behöver vi validation här? Fråga Anders
	public Product fetchProductById(User user, Long id) throws PermissionException
	{
		return productRepository.findById(id);
	}

	public User fetchUserById(User user, Long id) throws PermissionException
	{
		if (!eCommerceValidator.isActiveAdmin(user) && !user.getId().equals(id))
		{
			throw new PermissionException("No permission to fetch user by ID");
		}
		return userRepository.findById(id);
	}

	public Order fetchOrderById(User user, Long id) throws PermissionException
	{
		Order orderFoundById = orderRepository.findById(id);
		if (!eCommerceValidator.isActiveAdmin(user) && !eCommerceValidator.hasPermission(user, orderFoundById.getCustomer()))
		{
			throw new PermissionException("No permission to fetch order by ID");
		}
		return orderFoundById;
	}

	// behöver vi validation här? Fråga Anders
	public List<Product> fetchAllProducts(User user) throws PermissionException
	{
		return productRepository.fetchAll();
	}

	public List<User> fetchAllUsers(User admin) throws PermissionException
	{
		if (!eCommerceValidator.isActiveAdmin(admin))
		{
			throw new PermissionException("No permission to fetch all users");
		}
		return userRepository.fetchAll();
	}

	public List<Order> fetchAllOrders(User admin) throws PermissionException
	{
		if (!eCommerceValidator.isActiveAdmin(admin))
		{
			throw new PermissionException("No permission to feth all orders");
		}
		return orderRepository.fetchAll();
	}

	public Product createProduct(User admin, Product product) throws StorageException, PermissionException
	{
		if (!eCommerceValidator.isActiveAdmin(admin))
		{
			throw new PermissionException("No permission to create products");
		}
		if (eCommerceValidator.productNameAlreadyExists(product.getProductName()))
		{
			throw new StorageException("Product with name: " + product.getProductName() + " already exists");
		}
		return productRepository.saveOrUpdate(product);
	}

	public User createUser(User user) throws StorageException
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

	public Order createOrder(User customer, Order order) throws OrderException, PermissionException
	{
		if (!eCommerceValidator.hasPermission(customer, order.getCustomer()))
		{
			throw new PermissionException("No permission to create orders");
		}
		if (order.getTotalPrice() > 50000.00)
		{
			throw new OrderException("Total price exceeding SEK 50,000 is not allowed");
		}
		order.updateStockQuantities(order.getOrderRows());
		order.setStatus(OrderStatus.PLACED);
		return orderRepository.merge(order);
	}

	public Product updateProduct(User admin, Product product) throws PermissionException, StorageException
	{
		if (product.getId() == null)
		{
			throw new StorageException("Cannot update product. Product does not exists");
		}
		if (!eCommerceValidator.isActiveAdmin(admin))
		{
			throw new PermissionException("No permission to update products");
		}
		return productRepository.merge(product);
	}

	public User updateUser(User user, User userToUpdate) throws PermissionException, StorageException
	{
		if (userToUpdate.getId() == null)
		{
			throw new StorageException("Cannot update user. User does not exists");
		}
		if (!eCommerceValidator.isActiveAdmin(user) && !eCommerceValidator.hasPermission(user, userToUpdate))
		{
			throw new PermissionException("No permission to update user");
		}
		if (eCommerceValidator.hasPermission(user, userToUpdate) && eCommerceValidator.changedRoleOrUserStatus(userToUpdate))
		{
			throw new PermissionException("No permission to update role and user status");
		}
		return userRepository.merge(userToUpdate);
	}

	public Order updateOrder(User user, Order order) throws PermissionException, OrderException, StorageException
	{
		if (order.getId() == null)
		{
			throw new StorageException("Cannot update order. Order does not exists");
		}
		if (!eCommerceValidator.isActiveAdmin(user) && !eCommerceValidator.hasPermission(user, order.getCustomer()))
		{
			throw new PermissionException("No permission to update order");
		}
		if (eCommerceValidator.hasPermission(user, order.getCustomer()))
		{
			if (eCommerceValidator.changedOrderStatus(order))
			{
				throw new PermissionException("No permission to change order status");
			}
			if (order.getStatus().equals(OrderStatus.SHIPPED))
			{
				throw new OrderException("Cannot update order. Order already shipped");
			}
		}
		order.updateStockQuantities(order.getOrderRows());
		return orderRepository.merge(order);
	}

	public List<Product> searchProductsBasedOnProductName(String keyword)
	{
		return productRepository.searchProductsBasedOnProductName(keyword);
	}

	public User fetchUserByUsername(User user, String username) throws PermissionException
	{
		if (!eCommerceValidator.isActiveAdmin(user) && !user.getUsername().equals(username))
		{
			throw new PermissionException("No permission to fetch user by username");
		}
		if (userRepository.fetchUsersByUsername(username).size() > 0)
		{
			return userRepository.fetchUsersByUsername(username).get(0);
		}
		return null;
	}

	public Product changeProductStatus(User admin, Product product, ProductStatus status) throws PermissionException, StorageException
	{
		if (!eCommerceValidator.isActiveAdmin(admin))
		{
			throw new PermissionException("No permission to change product status");
		}
		product.setStatus(status);
		return productRepository.saveOrUpdate(product);
	}

	public User changeUserStatus(User admin, User user, UserStatus status) throws PermissionException, StorageException
	{
		if (!eCommerceValidator.isActiveAdmin(admin))
		{
			throw new PermissionException("No permission to change user status");
		}
		user.setStatus(status);
		return userRepository.saveOrUpdate(user);
	}

	public Order changeOrderStatus(User user, Order order, OrderStatus status) throws PermissionException, OrderException, StorageException
	{
		if (!eCommerceValidator.isActiveAdmin(user) && !eCommerceValidator.hasPermission(user, order.getCustomer()))
		{
			throw new PermissionException("No permission to change order status");
		}
		if (eCommerceValidator.hasPermission(user, order.getCustomer()) && status.equals(OrderStatus.CANCELED))
		{
			if (order.getStatus() != null && order.getStatus().equals(OrderStatus.SHIPPED))
			{
				throw new OrderException("Cannot cancel order. Order already shipped");
			}
		}
		order.setStatus(status);
		return orderRepository.merge(order);
	}

	public List<Order> fetchOrdersByUser(User user, User customer) throws PermissionException
	{
		if (!eCommerceValidator.isActiveAdmin(user) && !eCommerceValidator.hasPermission(user, customer))
		{
			throw new PermissionException("No permission to fetch orders by user");
		}
		return orderRepository.fetchOrdersByUser(customer);
	}

	public List<Order> fetchOrdersByStatus(User admin, OrderStatus orderStatus) throws PermissionException
	{
		if (!eCommerceValidator.isActiveAdmin(admin))
		{
			throw new PermissionException("No permission to fetch orders by status");
		}
		return orderRepository.fetchOrdersByStatus(orderStatus);
	}

	public List<Order> fetchOrdersByMinimumValue(User admin, Double minimumValue) throws PermissionException
	{
		if (!eCommerceValidator.isActiveAdmin(admin))
		{
			throw new PermissionException("No permission to fetch orders by minimum value");
		}
		return orderRepository.fetchOrdersByMinimumValue(minimumValue);
	}

	public User activateUser(User user)
	{
		user.setStatus(UserStatus.ACTIVE);
		return userRepository.saveOrUpdate(user);
	}

	public Order addOrderRows(User customer, Order order, OrderRow... orderRows) throws PermissionException, OrderException
	{
		if (!eCommerceValidator.hasPermission(customer, order.getCustomer()))
		{
			throw new PermissionException("No permission to add items");
		}
		if (order.getStatus() != null && !order.getStatus().equals(OrderStatus.PLACED))
		{
			throw new OrderException("Cannot add items. Order is already payed or canceled");
		}
		order.addOrderRows(orderRows);
		return order;
	}
}
