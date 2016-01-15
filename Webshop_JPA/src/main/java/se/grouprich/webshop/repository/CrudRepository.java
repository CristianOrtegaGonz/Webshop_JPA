package se.grouprich.webshop.repository;

import se.grouprich.webshop.model.AbstractEntity;

public interface CrudRepository<E extends AbstractEntity>
{
	 E findById(Long id);
	 E saveOrUpdate(E entity);
}
