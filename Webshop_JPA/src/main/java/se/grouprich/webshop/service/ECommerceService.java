package se.grouprich.webshop.service;

import java.util.List;

import se.grouprich.webshop.exception.PaymentException;
import se.grouprich.webshop.exception.ProductRegistrationException;
import se.grouprich.webshop.exception.UserRegistrationException;
import se.grouprich.webshop.exception.PermissionException;
import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.Product;
import se.grouprich.webshop.model.User;
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

	public Product createProduct(String productName, double price, int stockQuantity, String status) throws ProductRegistrationException
	{
		if (productValidator.alreadyExists(productName))
		{
			throw new ProductRegistrationException("Product with name: " + productName + " already exists");
		}
		Product product = new Product(productName, price, stockQuantity, status);
		return productRepository.saveOrUpdate(product);
	}

	public User createUser(String username, String password, String firstName, String lastName) throws UserRegistrationException
	{
		if (userValidator.alreadyExists(username))
		{
			throw new UserRegistrationException("User with username: " + username + " already exists");
		}
		if (!userValidator.isLengthWithinRange(username))
		{
			throw new UserRegistrationException("Username that is longer than 30 characters is not allowed");
		}
		if (!userValidator.isValidPassword(password))
		{
			throw new UserRegistrationException("Password must have at least an uppercase letter, two digits and a special character such as !@#$%^&*(){}[]");
		}
		User user = new User(username, password, firstName, lastName);
		return userRepository.saveOrUpdate(user);
	}

	public Order createOrder(Order order) throws PaymentException
	{
		if (order.getTotalPrice() > 50000.00)
		{
			throw new PaymentException("We can not accept the total price exceeding SEK 50,000");
		}
		return orderRepository.saveOrUpdate(order);
	}

	public Product updateProduct(Product product, User user) throws PermissionException
	{
		if (user.getRole().equals("admin") && user.getStatus().equals("ACTIVATED"))
		{
			return productRepository.saveOrUpdate(product);
		}
		else
		{
			throw new PermissionException("You must be an activated admin user to update products");
		}
	}

	public User updateUser(User user, String oldPassword) throws PermissionException
	{
		User oldUser = userRepository.findById(user.getId());
		if (oldUser.getPassword().equals(oldPassword))
		{
			return userRepository.saveOrUpdate(user);
		}
		else
		{
			throw new PermissionException("Password does not match the confirm password");
		}
	}

	public Order updateOrder(Order order, User user) throws PermissionException
	{
		if (user.getRole().equals("admin") && user.getStatus().equals("ACTIVATED"))
		{
			return orderRepository.saveOrUpdate(order);
		}
		else
		{
			throw new PermissionException("You must be an activated admin user to update orders");
		}
	}

	public List<Product> searchProductsBasedOnProductName(String keyword)
	{
		return productRepository.searchProductsBasedOnProductName(keyword);
	}

	public Product changeProductStatus(Product product, String status)
	{
		product.setStatus(status);
		return productRepository.saveOrUpdate(product);
	}

	public List<Order> fetchOrdersByUser(User user)
	{
		return orderRepository.fetchOrdersByUser(user);
	}

	// public void addProduct(OrderRow shoppingCart, String productId, int
	// orderQuantity) throws RepositoryException, OrderException
	// {
	// if (productRepository.readAll().containsKey(productId))
	// {
	// Product product = productRepository.read(productId);
	// if (shoppingCart.getProducts().contains(product) &&
	// product.getStockQuantity() >= product.getOrderQuantity() + orderQuantity)
	// {
	// product.addOrderQuantity(orderQuantity);
	// }
	// else if (product.getStockQuantity() >= orderQuantity)
	// {
	// shoppingCart.addProduct(product, orderQuantity);
	// }
	// else
	// {
	// throw new OrderException("Stock quantity is: " +
	// product.getStockQuantity());
	// }
	// }
	// else
	// {
	// throw new OrderException("Product with id: " + productId + "doesn't
	// exists");
	// }
	// }

	// public void changeOrderQuantity(OrderRow shoppingCart, String productId,
	// int orderQuantity) throws RepositoryException, OrderException
	// {
	// if (productRepository.readAll().containsKey(productId))
	// {
	// Product product = productRepository.read(productId);
	// if (shoppingCart.getProducts().contains(product) &&
	// product.getStockQuantity() >= orderQuantity)
	// {
	// product.setOrderQuantity(orderQuantity);
	// shoppingCart.calculateTotalPrice();
	// }
	// else
	// {
	// throw new OrderException("Stock quantity is: " +
	// product.getStockQuantity());
	// }
	// }
	// }
}