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

		Product product1 = eCommerceService.createProduct(admin, new Product("cat food", 33.657, 10));
		Product product2 = eCommerceService.createProduct(admin, new Product("dog food", 27.83, 10));
		Product product3 = eCommerceService.createProduct(admin, new Product("cat godis", 25.48, 10));
		System.out.println("product1 is: " + product1);
		System.out.println("product2 is: " + product2);
		System.out.println("product3 is: " + product3);
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
	}
}
