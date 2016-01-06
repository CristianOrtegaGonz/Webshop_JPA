package se.grouprich.webshop.main;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import se.grouprich.webshop.exception.OrderException;
import se.grouprich.webshop.exception.PermissionException;
import se.grouprich.webshop.exception.StorageException;
import se.grouprich.webshop.exception.ValidationException;
import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.OrderRow;
import se.grouprich.webshop.model.Product;
import se.grouprich.webshop.model.User;
import se.grouprich.webshop.model.status.OrderStatus;
import se.grouprich.webshop.model.status.ProductStatus;
import se.grouprich.webshop.model.status.UserStatus;
import se.grouprich.webshop.repository.JpaOrderRepository;
import se.grouprich.webshop.repository.JpaProductRepository;
import se.grouprich.webshop.repository.JpaUserRepository;
import se.grouprich.webshop.repository.OrderRepository;
import se.grouprich.webshop.repository.ProductRepository;
import se.grouprich.webshop.service.ECommerceService;
import se.grouprich.webshop.service.validation.ProductValidator;
import se.grouprich.webshop.service.validation.UserValidator;

public final class Main
{
	private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("PersistenceUnit");

	public static final void main(String[] args) throws StorageException, ValidationException, PermissionException, OrderException
	{
		ProductRepository productRepository = new JpaProductRepository(factory);
		Product product1 = new Product("pen", 10.33, 5, ProductStatus.IN_STOCK);
		Product product2 = new Product("notebook", 10.99, 5, ProductStatus.IN_STOCK);
		Product product3 = new Product("notepad", 10.25, 10, ProductStatus.IN_STOCK);
		productRepository.saveOrUpdate(product1);
		productRepository.saveOrUpdate(product2);
		productRepository.saveOrUpdate(product3);

		OrderRepository orderRepository = new JpaOrderRepository(factory);
		User user = new User("Eskimooo", "55I!", "Eski", "Mooo");
		Order order = new Order(user);
		OrderRow orderRow = new OrderRow(product1, 2);
		OrderRow orderRow2 = new OrderRow(product2, 2);
		OrderRow orderRow3 = new OrderRow(product2, 1);
		order.addOrderRow(orderRow).addOrderRow(orderRow2);
		System.out.println(orderRow);
		orderRepository.saveOrUpdate(order);

		List<Order> allOrders = orderRepository.fetchAll();
		System.out.println();
		System.out.println("All Orders:");
		System.out.println(allOrders);

		List<Order> ordersByMinimumValue = orderRepository.fetchOrdersByMinimumValue(50.00);
		System.out.println();
		System.out.println("Orders By Minimum Value:");
		System.out.println(ordersByMinimumValue);

		Order orderFoundById = orderRepository.findById(3L);
		System.out.println();
		System.out.println("Order Found By ID:");
		System.out.println(orderFoundById);

		order.addOrderRow(orderRow3);
		orderRepository.saveOrUpdate(order);
		System.out.println();
		System.out.println("After update:");
		System.out.println(order);

		JpaUserRepository userRepository = new JpaUserRepository(factory);
		List<User> allUsers = userRepository.fetchAll();
		System.out.println();
		System.out.println("All users:");
		System.out.println(allUsers);

		List<User> fetchedUsersByUsername = userRepository.fetchUsersByUsername("Eskimooo");
		System.out.println();
		System.out.println("User fetched by username:");
		System.out.println(fetchedUsersByUsername);

		User userFoundById = userRepository.findById(4L);
		System.out.println();
		System.out.println("User found by id:");
		System.out.println(userFoundById);

		user.setStatus(UserStatus.ACTIVE);
		userRepository.saveOrUpdate(user);
		System.out.println();
		System.out.println("After update:");
		System.out.println(user);

		ProductValidator productValidator = new ProductValidator(productRepository);
		UserValidator userValidator = new UserValidator(userRepository);
		ECommerceService eCommerceService = new ECommerceService(orderRepository, userRepository, productRepository, productValidator,
				userValidator);
		
		User userFetchedById = eCommerceService.fetchUserById(user, 5L);
		System.out.println();
		System.out.println("User Fetched By ID:");
		System.out.println(userFetchedById);
		
		Order createdOrder = eCommerceService.createOrder(user, order);
		System.out.println();
		System.out.println("Created Order:");
		System.out.println(createdOrder);
		
		List<Product> searchResult = eCommerceService.searchProductsBasedOnProductName("note");
		System.out.println();
		System.out.println("Search result:");
		System.out.println(searchResult);
		
		User user2 = eCommerceService.createUser("Mari", "12&OI4", "Mariko", "Hashimoto");
		User activatedUser = eCommerceService.activateUser(user2);
		activatedUser.setRole("admin");	
		User fetchedUser = eCommerceService.fetchUserByUsername(activatedUser, "Mari");
		System.out.println();
		System.out.println("Fetched User:");
		System.out.println(fetchedUser);
		eCommerceService.updateUser(activatedUser, activatedUser);
		System.out.println();
		System.out.println("Activated User:");
		System.out.println(activatedUser);
		
		User user3 = eCommerceService.createUser("Hanauta", "12&OI4", "Hanako", "Manaka");
		
		List<Product> allProducts = eCommerceService.fetchAllProducts(user2);
		System.out.println();
		System.out.println("All products");
		System.out.println(allProducts);
		
		User userToUpdate = eCommerceService.fetchUserByUsername(user3, "Hanauta");
		userToUpdate.setStatus(UserStatus.ACTIVE);
		System.out.println();
		System.out.println("Before update user3:");
		System.out.println(user3);
		User updatedUser = eCommerceService.updateUser(user2, userToUpdate);
		System.out.println();
		System.out.println("Updated user:");
		System.out.println(updatedUser);
		
		List<Order> ordersByminimValue = eCommerceService.fetchOrdersByMinimumValue(user2, 50.00);
		List<Order> allOrdersInDB = eCommerceService.fetchAllOrders(user2);
		System.out.println();
		System.out.println("All Orders");
		System.out.println(allOrdersInDB);
		System.out.println("Orders by minimum value");
		System.out.println(ordersByminimValue);
		
		Order orderFetchedById = eCommerceService.fetchOrderById(user2, 4L);
		System.out.println();
		System.out.println("Order Fetched By ID:");
		System.out.println(orderFetchedById);
		
		Product productFetchedById = eCommerceService.fetchProductById(user2, 3L);
		System.out.println();
		System.out.println("Product Fetched By ID:");
		System.out.println(productFetchedById);	
	}
}
