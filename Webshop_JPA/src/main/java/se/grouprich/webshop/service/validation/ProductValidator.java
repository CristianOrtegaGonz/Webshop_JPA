package se.grouprich.webshop.service.validation;

import javax.persistence.EntityManagerFactory;

import se.grouprich.webshop.repository.JpaProductRepository;

public final class ProductValidator implements DuplicateValidator
{
	private JpaProductRepository productRepository;

	public ProductValidator(EntityManagerFactory factory)
	{
		productRepository = new JpaProductRepository(factory);
	}

	@Override
	public boolean alreadyExists(final String productName)
	{
		if (productRepository.fetchProductsByProductName(productName) != null)
		{
			return true;
		}
		return false;
	}
}
