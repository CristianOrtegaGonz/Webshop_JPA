package se.grouprich.webshop.repository;

import static java.util.function.Function.*;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import se.grouprich.webshop.model.Product;

public final class JpaProductRepository extends AbstractJpaRepository<Product> implements ProductRepository
{
	public JpaProductRepository(EntityManagerFactory factory)
	{
		super(factory, Product.class);
	}

	@Override
	public List<Product> fetchAll()
	{
		return fetchMany("Product.FetchAll", identity());
	}

	@Override
	public List<Product> fetchProductsByProductName(String productName)
	{
		return fetchMany("Product.FetchProductsByProductName", queryFunction -> queryFunction.setParameter("productName", productName));
	}
}
