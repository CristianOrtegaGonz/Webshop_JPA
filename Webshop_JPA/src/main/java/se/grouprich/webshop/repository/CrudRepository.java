package se.grouprich.webshop.repository;

import java.util.List;

import se.grouprich.webshop.model.AbstractEntity;

public interface CrudRepository<E extends AbstractEntity>
{
	 E findById(Long id);
	 List<E> getAll();
	 E saveOrUpdate(E entity);
}
