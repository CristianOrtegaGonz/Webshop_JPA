package se.grouprich.webshop.main;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import se.grouprich.webshop.exception.OrderException;
import se.grouprich.webshop.exception.PermissionException;
import se.grouprich.webshop.exception.StorageException;
import se.grouprich.webshop.model.Order;
import se.grouprich.webshop.model.OrderRow;
import se.grouprich.webshop.model.Product;
import se.grouprich.webshop.model.Role;
import se.grouprich.webshop.model.User;
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

	public static final void main(String[] args) throws StorageException, IllegalArgumentException, PermissionException, OrderException
	{
		ProductRepository productRepository = new JpaProductRepository(factory);
		OrderRepository orderRepository = new JpaOrderRepository(factory);
		UserRepository userRepository = new JpaUserRepository(factory);
		ProductValidator productValidator = new ProductValidator(productRepository);
		UserValidator userValidator = new UserValidator(userRepository);
		ECommerceService eCommerceService = new ECommerceService(orderRepository, userRepository, productRepository, productValidator, userValidator);
		User admin = eCommerceService.createUser(new User("Eskimooo", "55I!", "Eski", "Mooo", Role.ADMIN));
		eCommerceService.activateUser(admin);
		System.out.println("admin is: " + admin);
		
		User customer = eCommerceService.createUser(new User("Marimekko", "39P!", "Mari", "Mekko", Role.CUSTOMER));
		eCommerceService.activateUser(customer);
		System.out.println("customer is: " + customer);
		
		Product product1 = eCommerceService.createProduct(admin, new Product("cat food", 30.00, 10));
		Product product4 = eCommerceService.createProduct(admin, new Product("cat godis", 20.00, 10));
		Product product2 = eCommerceService.createProduct(admin, new Product("dog food", 20.00, 3));
		System.out.println("product1 is: " + product1);
		System.out.println("product2 is: " + product2);
		System.out.println("product4 is: " + product4);
		Order order = eCommerceService.createOrder(customer, new Order(customer, new OrderRow(product1, 2), new OrderRow(product2, 2)));
		System.out.println("order is: " + order);	
		Order order1 = eCommerceService.createOrder(customer, new Order(customer, new OrderRow(new Product("cat tunnel", 50.00, 4), 2)));
		System.out.println("order1 is: " + order1);
		System.out.println();
		
		Product productFetchedById = eCommerceService.fetchProductById(admin, 2L);
		System.out.println("product with id 2 is: " + productFetchedById);
		System.out.println();
		
		User userFetchedById = eCommerceService.fetchUserById(admin, 1L);
		System.out.println("user with id 1 is: " + userFetchedById);
		System.out.println();
		
		Order orderFetchedById = eCommerceService.fetchOrderById(admin, 6L);
		System.out.println("orderFetchedById is: " + orderFetchedById);
		System.out.println();
		
		Product product3 = eCommerceService.createProduct(admin, new Product("soffa", 4200.00, 0));
		System.out.println("product3 is: " + product3);
		System.out.println();
		
		List<Product> searchedProducts = eCommerceService.searchProductsBasedOnProductName("cat");
		System.out.println("Searched with keyword \"cat\". Result is: " + searchedProducts);
		System.out.println();
		
		List<Order> orders = eCommerceService.fetchAllOrders(userFetchedById);
		System.out.println(orders);
		
		List<Order> ordersFetchedByUser = eCommerceService.fetchOrdersByUser(customer, customer);
		System.out.println(ordersFetchedByUser);	
	}
}
