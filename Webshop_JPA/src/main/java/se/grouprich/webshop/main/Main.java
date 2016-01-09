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
		System.out.println();

		User customer = eCommerceService.createUser(new User("Marimekko", "39P!", "Mari", "Mekko", Role.CUSTOMER));
		eCommerceService.activateUser(customer);
		System.out.println("customer is: " + customer);
		System.out.println();

		User customer2 = eCommerceService.createUser(new User("Kinoko", "098U#", "Mash", "Room", Role.CUSTOMER));
		eCommerceService.activateUser(customer2);
		System.out.println("customer is: " + customer2);
		System.out.println();

		Product product1 = eCommerceService.createProduct(admin, new Product("cat food", 33.65, 10));
		Product product2 = eCommerceService.createProduct(admin, new Product("dog food", 27.83, 10));
		Product product4 = eCommerceService.createProduct(admin, new Product("cat godis", 25.48, 10));
		System.out.println("product1 is: " + product1);
		System.out.println("product2 is: " + product2);
		System.out.println("product4 is: " + product4);
		System.out.println();

		Order order = eCommerceService.createOrder(customer, new Order(customer, new OrderRow(product1, 2), new OrderRow(product2, 5)));
		System.out.println("order is: " + order);
		System.out.println();

		List<Order> orders1 = eCommerceService.fetchAllOrders(admin);
		System.out.println("orders1 is: " + orders1);
		System.out.println();

		Order order1 = eCommerceService.createOrder(customer, new Order(customer, new OrderRow(new Product("cat tunnel", 49.52, 5), 2)));
		System.out.println("order1 is: " + order1);
		System.out.println();

		Order order2 = eCommerceService.createOrder(customer2, new Order(customer2, new OrderRow(product1, 1)));
		System.out.println("order2 is: " + order2);
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

		Product product3 = eCommerceService.createProduct(admin, new Product("soffa", 4200.00, 2));
		System.out.println("product3 is: " + product3);
		System.out.println();

		List<Product> searchedProducts = eCommerceService.searchProductsBasedOnProductName("cat");
		System.out.println("Searched with keyword \"cat\". Result is: " + searchedProducts);
		System.out.println();

		List<Order> ordersFetchedByUser = eCommerceService.fetchOrdersByUser(customer, customer);
		System.out.println("ordersFetchedByUser is: " + ordersFetchedByUser);
		System.out.println();

		product1.setProductName("cat food for kittens");
		Product updatedProduct = eCommerceService.updateProduct(admin, product1);
		System.out.println("updatedProduct is: " + updatedProduct);
		System.out.println();

		customer.setRole(Role.ADMIN);
		User updatedUser = eCommerceService.updateUser(admin, customer);
		System.out.println(updatedUser);
		System.out.println();

		List<Product> products = eCommerceService.fetchAllProducts(customer);
		System.out.println("products is: " + products);
		System.out.println();
		Order fetchedOrder = eCommerceService.fetchOrderById(admin, 7L);

		Order orderRowsAdded = eCommerceService.addOrderRows(customer, fetchedOrder, new OrderRow(product3, 1), new OrderRow(product2, 5));
		Order updatedOrder = eCommerceService.updateOrder(customer, orderRowsAdded);
		System.out.println(updatedOrder);
		System.out.println();

		List<Product> products2 = eCommerceService.fetchAllProducts(customer);
		System.out.println("products2 is: " + products2);
		System.out.println();

		List<Order> fetchedOrders = eCommerceService.fetchOrdersByUser(admin, customer);
		System.out.println(fetchedOrders);
		System.out.println(products);
		eCommerceService.updateOrder(admin, order2);
		System.out.println(products);
		System.out.println();

		System.out.println("order before: " + order);
		// Om man använder ett gammalt entity(objekt) som "order" här vid uppdatering så bytas ut data
		// till den gamla så man bör inte använda ett gammalt entity(objekt) när det redan 
		// finns sparad entity med samma id. Först hämta den nyaste entity från datanasen sedan redigera den istället.
		Order addOrderRows = eCommerceService.addOrderRows(customer, order/*gammal entity*/, new OrderRow(product3, 1))
				.addOrderRows(new OrderRow(product4, 3));
		
		//Du märker att det finns 5 dog food i lager i rad 140 trots att det var OUT_OF_STOCK i rad 122! 
		System.out.println(addOrderRows);
		eCommerceService.updateOrder(customer, addOrderRows);

		System.out.println(products);

		List<Order> orders = eCommerceService.fetchAllOrders(userFetchedById);
		System.out.println("orders is: " + orders);
		System.out.println();
	}
}
