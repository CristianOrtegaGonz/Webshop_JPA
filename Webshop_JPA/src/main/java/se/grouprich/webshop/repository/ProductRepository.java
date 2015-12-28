package se.grouprich.webshop.repository;

import java.util.List;

import se.grouprich.webshop.model.Product;

public interface ProductRepository extends CrudRepository<Product>
{
	List<Product> fetchProductsByProductName(String productName);
	Product changeStatus(String status);
}
