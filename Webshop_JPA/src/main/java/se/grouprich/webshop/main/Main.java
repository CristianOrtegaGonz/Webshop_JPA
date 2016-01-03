package se.grouprich.webshop.main;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import se.grouprich.webshop.exception.ProductRegistrationException;
import se.grouprich.webshop.model.Product;
import se.grouprich.webshop.repository.JpaProductRepository;

public final class Main
{
	private static final EntityManagerFactory factory = Persistence.createEntityManagerFactory("PersistenceUnit");

	public static final void main(String[] args) throws ProductRegistrationException
	{
		JpaProductRepository productRepository = new JpaProductRepository(factory);
		Product product1 = new Product("pen", 10.00, 5, "In Stock");
		Product product2 = new Product("notebook", 10.00, 5, "Ordered");
		productRepository.saveOrUpdate(product1);
		productRepository.saveOrUpdate(product2);
		
		List<Product> fetchAll = productRepository.fetchAll();
		System.out.println(fetchAll);
		
		List<Product> fetchedProducts = productRepository.fetchProductsByProductName("pen");
		for (Product fetchedProduct : fetchedProducts)
		{
			System.out.println(fetchedProduct);
		}
		
		Product productFoundById = productRepository.findById(2L);
		System.out.println(productFoundById);
		
		product2.setStatus("In Stock");
		Product productUpdated = productRepository.saveOrUpdate(product2);
		System.out.println(productUpdated);
	}
}
