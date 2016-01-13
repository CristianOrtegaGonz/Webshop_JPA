package se.grouprich.webshop.repository;

import java.util.List;

import se.grouprich.webshop.model.Product;

public interface ProductRepository extends CrudRepository<Product>
{
	List<Product> fetchAll();
	List<Product> searchProductsBasedOnProductName(final String productName);
	List<Product> fetchProductsByProductName(final String productName);
}
