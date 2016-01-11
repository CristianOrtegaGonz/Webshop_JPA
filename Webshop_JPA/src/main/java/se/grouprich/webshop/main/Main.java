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
import se.grouprich.webshop.model.status.OrderStatus;
import se.grouprich.webshop.model.status.ProductStatus;
import se.grouprich.webshop.model.status.UserStatus;
import se.grouprich.webshop.repository.JpaOrderRepository;
import se.grouprich.webshop.repository.JpaProductRepository;
import se.grouprich.webshop.repository.JpaUserRepository;
import se.grouprich.webshop.repository.OrderRepository;
import se.grouprich.webshop.repository.ProductRepository;
import se.grouprich.webshop.repository.UserRepository;
import se.grouprich.webshop.service.ECommerceService;
import se.grouprich.webshop.service.validation.ECommerceValidator;

public final class Main
{
	private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("PersistenceUnit");

	public static final void main(String[] args) throws StorageException, IllegalArgumentException, PermissionException, OrderException
	{
		ProductRepository productRepository = new JpaProductRepository(factory);
		OrderRepository orderRepository = new JpaOrderRepository(factory);
		UserRepository userRepository = new JpaUserRepository(factory);
		ECommerceValidator eCommerceValidator = new ECommerceValidator(userRepository, orderRepository, productRepository);
		ECommerceService eCommerceService = new ECommerceService(orderRepository, userRepository, productRepository, eCommerceValidator);
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

		Product product1 = eCommerceService.createProduct(admin, new Product("cat food", 33.657, 10));
		Product product2 = eCommerceService.createProduct(admin, new Product("dog food", 27.83, 10));
		Product product3 = eCommerceService.createProduct(admin, new Product("cat godis", 25.48, 10));
		System.out.println("product1 is: " + product1);
		System.out.println("product2 is: " + product2);
		System.out.println("product3 is: " + product3);
		System.out.println();

		product2.setStatus(ProductStatus.NOT_AVAILABLE);

		Product product2Updated = eCommerceService.updateProduct(admin, product2);
		System.out.println("product2Updated is: " + product2Updated);
		System.out.println();

		List<Product> catProducts = eCommerceService.searchProductsBasedOnProductName("cat");
		System.out.println("catProducts are: " + catProducts);
		System.out.println();

		List<Product> productsFood = eCommerceService.searchProductsBasedOnProductName("food");
		System.out.println("productsFood are: " + productsFood);
		System.out.println();

		Product availableProduct = eCommerceService.changeProductStatus(admin, product1, ProductStatus.AVAILABLE);
		System.out.println("availableProduct is: " + availableProduct);
		System.out.println();

		Product availableProduct2 = eCommerceService.changeProductStatus(admin, product2, ProductStatus.AVAILABLE);
		System.out.println("availableProduct2 is: " + availableProduct2);
		System.out.println();

		Order order = eCommerceService.createOrder(customer2, new Order(customer2, new OrderRow(product1, 4)));
		System.out.println("order is: " + order);
		System.out.println();

		Order order2 = eCommerceService.createOrder(customer2, new Order(customer2, new OrderRow(product2, 5)));
		System.out.println("order2 is: " + order2);
		System.out.println();

		User deactivatedUser = eCommerceService.changeUserStatus(admin, customer, UserStatus.DEACTIVATED);
		System.out.println("deactivatedUser is: " + deactivatedUser);
		System.out.println();

		Order payedOrder = eCommerceService.changeOrderStatus(admin, order, OrderStatus.PAYED);
		System.out.println("payedOrder is: " + payedOrder);
		System.out.println();

		List<Order> ordersMinim100 = eCommerceService.fetchOrdersByMinimumValue(admin, 100.0);
		System.out.println("ordersMinim100 are: " + ordersMinim100);
		System.out.println();

		Product productFetchedById = eCommerceService.fetchProductById(admin, 3L);
		System.out.println("productFetchedById is: " + productFetchedById);
		System.out.println();

		User userFethedById = eCommerceService.fetchUserById(customer, 2L);
		System.out.println("userFetchedById is: " + userFethedById);
		System.out.println();

		Order orderFetchedById = eCommerceService.fetchOrderById(customer2, 7L);
		System.out.println("orderFetchedById is: " + orderFetchedById);
		System.out.println();

		List<User> allUsers = eCommerceService.fetchAllUsers(admin);
		System.out.println("allUsers are: " + allUsers);
		System.out.println();

		List<Order> allOrders = eCommerceService.fetchAllOrders(admin);
		System.out.println("allOrders are: " + allOrders);
		System.out.println();

		Product newProduct = eCommerceService.createProduct(admin, new Product("cat tunnel", 10.00, 4)).setStatus(ProductStatus.AVAILABLE);
		;
		System.out.println("newProduct is: " + newProduct);
		System.out.println();

		User newUser = eCommerceService.createUser(new User("Marimo", "111%LD", "Masaru", "Marimo", Role.CUSTOMER));
		System.out.println("newUser is: " + newUser);
		System.out.println();

		User fetchedUser = eCommerceService.fetchUserByUsername(admin, "Marimo");
		System.out.println("fetchedUser is: " + fetchedUser);
		System.out.println();

		User panda = newUser.setUsername("Panda").setStatus(UserStatus.ACTIVE);

		eCommerceService.updateUser(admin, panda);

		User pandaUpdated = eCommerceService.updateUser(admin, panda);
		System.out.println("userUpdated is: " + pandaUpdated);
		System.out.println();

		Order shippedOrder = order.setStatus(OrderStatus.SHIPPED);

		Order updatedOrder = eCommerceService.updateOrder(admin, shippedOrder);
		System.out.println("updatedOrder is: " + updatedOrder);
		System.out.println();

		User userFetchedByUsername = eCommerceService.fetchUserByUsername(customer2, "Kinoko");
		System.out.println("userFetchedByUsername is: " + userFetchedByUsername);
		System.out.println();

		Product notPublishedProduct = eCommerceService.changeProductStatus(admin, product1, ProductStatus.NOT_PUBLISHED);
		System.out.println("notPublishedProduct is: " + notPublishedProduct);
		System.out.println();

		Product product4 = new Product("mugg", 30.00, 5);
		Product productStatusChanged = eCommerceService.changeProductStatus(admin, product4, ProductStatus.AVAILABLE);
		System.out.println("productStatusChanged is: " + productStatusChanged);

		User lockedUser = eCommerceService.changeUserStatus(admin, panda, UserStatus.LOCKED);
		System.out.println("lockedUser is:" + lockedUser);
		System.out.println();

		Order canceledOrder = eCommerceService.changeOrderStatus(admin, updatedOrder, OrderStatus.PLACED);
		System.out.println("canceledOrder is: " + canceledOrder);
		System.out.println();

		List<Order> ordersFetchedByUser = eCommerceService.fetchOrdersByUser(admin, customer2);
		System.out.println("ordersFetchedByUser is: " + ordersFetchedByUser);
		System.out.println();

		List<Order> ordersFetchedByStatus = eCommerceService.fetchOrdersByStatus(admin, OrderStatus.CANCELED);
		System.out.println("ordersFetchedByStatus is:" + ordersFetchedByStatus);
		System.out.println();

		List<Order> ordersFetchedByMinimValue = eCommerceService.fetchOrdersByMinimumValue(admin, 100.00);
		System.out.println("ordersFetchedByMinimValue is: " + ordersFetchedByMinimValue);
		System.out.println();
		
		Order order3 = new Order(customer2, new OrderRow(product2, 2));
		Order orderRowsAdded = eCommerceService.addOrderRows(customer2, order3, new OrderRow(product2, 3));
		System.out.println("orderRowsAdded is: " + orderRowsAdded);
	}
}
