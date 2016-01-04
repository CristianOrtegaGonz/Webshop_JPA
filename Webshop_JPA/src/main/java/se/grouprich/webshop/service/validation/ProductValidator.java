package se.grouprich.webshop.service.validation;

import se.grouprich.webshop.repository.JpaProductRepository;

public class ProductValidator
{
	private final JpaProductRepository productRepository;

	public ProductValidator(JpaProductRepository productRepository)
	{
		this.productRepository = productRepository;
	}
	
	public boolean alreadyExists(final String productName)
	{
		if (productRepository.fetchProductByProductName(productName) != null)
		{
			return true;
		}
		return false;
	}
}
