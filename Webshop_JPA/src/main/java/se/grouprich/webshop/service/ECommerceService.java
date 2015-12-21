package se.grouprich.webshop.service;

import java.util.ArrayList;
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
import se.grouprich.webshop.repository.Repository;
import se.grouprich.webshop.service.validation.DuplicateValidator;
import se.grouprich.webshop.service.validation.EmailValidator;
import se.grouprich.webshop.service.validation.PasswordValidator;

public final class ECommerceService
{
	private final Repository<String, Order> orderRepository;
	private final Repository<String, User> userRepository;
	private final Repository<String, Product> productRepository;
	private final IdGenerator<String> idGenerator;
	private final PasswordValidator passwordValidator;
	private final DuplicateValidator userDuplicateValidator;
	private final DuplicateValidator productDuplicateValidator;
	private final EmailValidator emailValidator;

	public ECommerceService(Repository<String, Order> orderRepository, Repository<String, User> userRepository, Repository<String, Product> productRepository,
			IdGenerator<String> idGenerator, PasswordValidator passwordValidator, DuplicateValidator userDuplicateValidator,
			DuplicateValidator productDuplicateValidator, EmailValidator emailValidator)
	{
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.idGenerator = idGenerator;
		this.passwordValidator = passwordValidator;
		this.userDuplicateValidator = userDuplicateValidator;
		this.productDuplicateValidator = productDuplicateValidator;
		this.emailValidator = emailValidator;
	}

	public Repository<String, Order> getOrderRepository()
	{
		return orderRepository;
	}

	public Repository<String, User> getUserRepository()
	{
		return userRepository;
	}

	public Repository<String, Product> getProductRepository()
	{
		return productRepository;
	}

	public IdGenerator<String> getIdGenerator()
	{
		return idGenerator;
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

//	public OrderRow createShoppingCart()
//	{
//		return new OrderRow();
//	}

	public User createUser(String email, String password, String firstName, String lastName, String role) throws UserRegistrationException
	{
		if (userDuplicateValidator.alreadyExists(email))
		{
			throw new UserRegistrationException("User with E-mail: " + email + " already exists");
		}
		if (!emailValidator.isLengthWithinRange(email))
		{
			throw new UserRegistrationException("Email address that is longer than 30 characters is not allowed");
		}
		if (!passwordValidator.isValidPassword(password))
		{
			throw new UserRegistrationException("Password must have at least an uppercase letter, two digits and a special character such as !@#$%^&*(){}[]");
		}
//		String id = idGenerator.getGeneratedId();
		User user = new User(email, password, firstName, lastName, role);
		return userRepository.create(user);
	}

	public Product createProduct(String productName, double price, int stockQuantity) throws ProductRegistrationException, RepositoryException
	{
		if (productDuplicateValidator.alreadyExists(productName))
		{
			throw new ProductRegistrationException("Product with name: " + productName + " already exists");
		}
//		String id = idGenerator.getGeneratedId();
		Product product = new Product(productName, price, stockQuantity);
		return productRepository.create(product);
	}

//	public Order checkOut(User user, OrderRow orderRow) throws OrderException
//	{
//		if (orderRow.getProducts().isEmpty())
//		{
//			throw new OrderException("Shopping cart is empty");
//		}
//		String id = null;
//		return new Order(user, orderRow);
//	}

	public Order createOrder(Order order) throws PaymentException
	{
		if (order.getTotalPrice() > 50000.00)
		{
			throw new PaymentException("We can not accept the total price exceeding SEK 50,000");
		}
//		order.pay();
		String id = idGenerator.getGeneratedId();
//		order.setId(id);
		return orderRepository.create(order);
	}

	public User deleteUser(String userId) throws RepositoryException
	{
		return userRepository.delete(userId);
	}

	public Order deleteOrder(String orderId) throws RepositoryException
	{
		return orderRepository.delete(orderId);
	}

	public Product deleteProduct(String productId) throws RepositoryException
	{
		return productRepository.delete(productId);
	}

	public User updateUser(String userId, User user) throws RepositoryException
	{
		//TODO: validera vilka har rättigheter att göra det
		return userRepository.update(userId, user);
	}

	public Order updateOrder(String orderId, Order order) throws RepositoryException
	{
		//TODO: validera vilka har rättigheter att göra det
		return orderRepository.update(orderId, order);
	}

	public Product updateProduct(String productId, Product product) throws RepositoryException
	{
		//TODO: validera vilka har rättigheter att göra det
		return productRepository.update(productId, product);
	}

	public User fetchUser(String userId) throws RepositoryException
	{
		return userRepository.read(userId);
	}

	public Order fetchOrder(String orderId) throws RepositoryException
	{
		return orderRepository.read(orderId);
	}

	public Product fetchProduct(String productId) throws RepositoryException
	{
		return productRepository.read(productId);
	}

	public Map<String, User> fetchAllUser()
	{
		return userRepository.readAll();
	}

	public Map<String, Order> fetchAllOrders()
	{
		return orderRepository.readAll();
	}

	public Map<String, Product> fetchAllProducts()
	{
		return productRepository.readAll();
	}

	public List<Order> fetchOrdersByUser(User user)
	{
		List<Order> ordersByUser = new ArrayList<>();
		for (Order order : (orderRepository.readAll()).values())
		{
			if (order.getUser().equals(user))
			{
				ordersByUser.add(order);
			}
		}
		return ordersByUser;
	}

//	public void addProduct(OrderRow shoppingCart, String productId, int orderQuantity) throws RepositoryException, OrderException
//	{
//		if (productRepository.readAll().containsKey(productId))
//		{
//			Product product = productRepository.read(productId);
//			if (shoppingCart.getProducts().contains(product) && product.getStockQuantity() >= product.getOrderQuantity() + orderQuantity)
//			{
//				product.addOrderQuantity(orderQuantity);
//			}
//			else if (product.getStockQuantity() >= orderQuantity)
//			{
//				shoppingCart.addProduct(product, orderQuantity);
//			}
//			else
//			{
//				throw new OrderException("Stock quantity is: " + product.getStockQuantity());
//			}
//		}
//		else
//		{
//			throw new OrderException("Product with id: " + productId + "doesn't exists");
//		}
//	}

//	public void changeOrderQuantity(OrderRow shoppingCart, String productId, int orderQuantity) throws RepositoryException, OrderException
//	{
//		if (productRepository.readAll().containsKey(productId))
//		{
//			Product product = productRepository.read(productId);
//			if (shoppingCart.getProducts().contains(product) && product.getStockQuantity() >= orderQuantity)
//			{
//				product.setOrderQuantity(orderQuantity);
//				shoppingCart.calculateTotalPrice();
//			}
//			else
//			{
//				throw new OrderException("Stock quantity is: " + product.getStockQuantity());
//			}
//		}
//	}
}