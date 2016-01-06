package se.grouprich.webshop.main;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import se.grouprich.webshop.exception.OrderException;
import se.grouprich.webshop.exception.PermissionException;
import se.grouprich.webshop.exception.StorageException;
import se.grouprich.webshop.exception.ValidationException;
import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.OrderRow;
import se.grouprich.webshop.model.Product;
import se.grouprich.webshop.model.Role;
import se.grouprich.webshop.model.User;
import se.grouprich.webshop.model.status.ProductStatus;
import se.grouprich.webshop.repository.JpaOrderRepository;
import se.grouprich.webshop.repository.JpaProductRepository;
import se.grouprich.webshop.repository.JpaUserRepository;
import se.grouprich.webshop.repository.OrderRepository;
import se.grouprich.webshop.repository.ProductRepository;
import se.grouprich.webshop.repository.UserRepository;
import se.grouprich.webshop.service.ECommerceService;
import se.grouprich.webshop.service.validation.ProductValidator;
import se.grouprich.webshop.service.validation.UserValidator;

public final class Main
{
	private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("PersistenceUnit");

	public static final void main(String[] args) throws StorageException, ValidationException, PermissionException, OrderException
	{
		ProductRepository productRepository = new JpaProductRepository(factory);
		OrderRepository orderRepository = new JpaOrderRepository(factory);
		UserRepository userRepository = new JpaUserRepository(factory);
		ProductValidator productValidator = new ProductValidator(productRepository);
		UserValidator userValidator = new UserValidator(userRepository);
		ECommerceService eCommerceService = new ECommerceService(orderRepository, userRepository, productRepository, productValidator, userValidator);
		User user = eCommerceService.createUser(new User("Eskimooo", "55I!", "Eski", "Mooo", Role.ADMIN));
		eCommerceService.activateUser(user);
		System.out.println("user is: " + user);
		
		Product product1 = eCommerceService.createProduct(user, new Product("cat food", 20.00, 10, ProductStatus.IN_STOCK));
		Product product2 = eCommerceService.createProduct(user, new Product("dog food", 20.00, 3, ProductStatus.IN_STOCK));
		System.out.println("product1 is: " + product1);
		System.out.println("product2 is: " + product2);
		Product productFetchedById = eCommerceService.fetchProductById(user, 2L);
		System.out.println("product with id 2 is: " + productFetchedById);
		
		User userFetchedById = eCommerceService.fetchUserById(user, 1L);
		System.out.println("user with id 1 is: " + userFetchedById);
		
		Order order = eCommerceService.createOrder(user, new Order(user, new OrderRow(product1, 2), new OrderRow(product2, 2)));
		System.out.println("order is: " + order);	
	}
}
