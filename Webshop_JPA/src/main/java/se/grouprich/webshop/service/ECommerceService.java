package se.grouprich.webshop.service;

import java.util.List;
import java.util.Map;

import se.grouprich.webshop.exception.PaymentException;
import se.grouprich.webshop.exception.ProductRegistrationException;
import se.grouprich.webshop.exception.RepositoryException;
import se.grouprich.webshop.exception.UserRegistrationException;
import se.grouprich.webshop.idgenerator.IdGenerator;
import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.Product;
import se.grouprich.webshop.model.User;
import se.grouprich.webshop.repository.JpaOrderRepository;
import se.grouprich.webshop.repository.JpaProductRepository;
import se.grouprich.webshop.repository.Repository;
import se.grouprich.webshop.service.validation.DuplicateValidator;
import se.grouprich.webshop.service.validation.EmailValidator;
import se.grouprich.webshop.service.validation.PasswordValidator;

public final class ECommerceService
{
	private final JpaProductRepository productRepository;
	private final JpaOrderRepository orderRepository;
	private final Repository<String, User> userRepository;
	private final IdGenerator<String> idGenerator;
	private final PasswordValidator passwordValidator;
	private final DuplicateValidator userDuplicateValidator;
	private final DuplicateValidator productDuplicateValidator;
	private final EmailValidator emailValidator;

	public ECommerceService(JpaOrderRepository orderRepository,
			Repository<String, User> userRepository,
			IdGenerator<String> idGenerator,
			JpaProductRepository productRepository, PasswordValidator passwordValidator, DuplicateValidator userDuplicateValidator,
			DuplicateValidator productDuplicateValidator, EmailValidator emailValidator)
	{
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.idGenerator = idGenerator;
		this.productRepository = productRepository;
		this.passwordValidator = passwordValidator;
		this.userDuplicateValidator = userDuplicateValidator;
		this.productDuplicateValidator = productDuplicateValidator;
		this.emailValidator = emailValidator;
	}

	// public Repository<String, User> getUserRepository()
	// {
	// return userRepository;
	// }

	// public IdGenerator<String> getIdGenerator()
	// {
	// return idGenerator;
	// }

	public JpaProductRepository getProductRepository()
	{
		return productRepository;
	}

	public JpaOrderRepository getOrderRepository()
	{
		return orderRepository;
	}

	public PasswordValidator getPasswordValidator()
	{
		return passwordValidator;
	}

	public DuplicateValidator getUserDuplicateValidator()
	{
		return userDuplicateValidator;
	}

	public DuplicateValidator getProductDuplicateValidator()
	{
		return productDuplicateValidator;
	}

	public EmailValidator getEmailValidator()
	{
		return emailValidator;
	}

	public Product fetchProductById(Long id) throws RepositoryException
	{
		return productRepository.findById(id);
	}

	public List<Product> fetchAllProducts()
	{
		return productRepository.fetchAll();
	}

	public List<Product> fetchProductsByProductName(String productName)
	{
		return productRepository.fetchProductsByProductName(productName);
	}
	
	public Product createProduct(String productName, double price, int stockQuantity, String status) throws ProductRegistrationException, RepositoryException
	{
		if (productDuplicateValidator.alreadyExists(productName))
		{
			throw new ProductRegistrationException("Product with name: " + productName + " already exists");
		}
		Product product = new Product(productName, price, stockQuantity, status);
		return productRepository.saveOrUpdate(product);
	}
	
	public Product updateProduct(Product product) throws RepositoryException
	{
		// TODO: validera vilka har rättigheter att göra det
		return productRepository.saveOrUpdate(product);
	}
	
	public Product changeProductStatus(Product product, String status)
	{
		product.setStatus(status);
		return productRepository.saveOrUpdate(product);
	}

	public User fetchUserById(String id) throws RepositoryException
	{
		return userRepository.read(id);
	}

	public Order fetchOrderById(Long id) throws RepositoryException
	{
		return orderRepository.findById(id);
	}

	public User createUser(String username, String password, String firstName, String lastName) throws UserRegistrationException
	{
		if (userDuplicateValidator.alreadyExists(username))
		{
			throw new UserRegistrationException("User with E-mail: " + username + " already exists");
		}
		if (!emailValidator.isLengthWithinRange(username))
		{
			throw new UserRegistrationException("Email address that is longer than 30 characters is not allowed");
		}
		if (!passwordValidator.isValidPassword(password))
		{
			throw new UserRegistrationException("Password must have at least an uppercase letter, two digits and a special character such as !@#$%^&*(){}[]");
		}
		// String id = idGenerator.getGeneratedId();
		User user = new User(username, password, firstName, lastName);
		return userRepository.create(user);
	}

	// public Order checkOut(User user, OrderRow orderRow) throws OrderException
	// {
	// if (orderRow.getProducts().isEmpty())
	// {
	// throw new OrderException("Shopping cart is empty");
	// }
	// String id = null;
	// return new Order(user, orderRow);
	// }

	public Order createOrder(Order order) throws PaymentException
	{
		if (order.getTotalPrice() > 50000.00)
		{
			throw new PaymentException("We can not accept the total price exceeding SEK 50,000");
		}
		return orderRepository.saveOrUpdate(order);
	}

	public User updateUser(String userId, User user) throws RepositoryException
	{
		// TODO: validera vilka har rättigheter att göra det
		return userRepository.update(userId, user);
	}

	public Order updateOrder(Order order) throws RepositoryException
	{
		// TODO: validera vilka har rättigheter att göra det
		return orderRepository.saveOrUpdate(order);
	}

	public Map<String, User> fetchAllUser()
	{
		return userRepository.readAll();
	}

	public List<Order> fetchAllOrders()
	{
		return orderRepository.fetchAll();
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