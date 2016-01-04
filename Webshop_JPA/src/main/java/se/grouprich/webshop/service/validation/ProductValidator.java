package se.grouprich.webshop.service.validation;

import se.grouprich.webshop.repository.ProductRepository;

public class ProductValidator
{
	private final ProductRepository productRepository;

	public ProductValidator(ProductRepository productRepository)
	{
		this.productRepository = productRepository;
	}
	
	public boolean alreadyExists(final String productName)
	{
		if (!productRepository.fetchProductsByProductName(productName).isEmpty())
		{
			return true;
		}
		return false;
	}
}
