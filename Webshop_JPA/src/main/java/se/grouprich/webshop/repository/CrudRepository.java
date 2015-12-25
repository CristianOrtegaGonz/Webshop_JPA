package se.grouprich.webshop.repository;

import java.util.List;
import java.util.function.Function;

import javax.persistence.TypedQuery;

import se.grouprich.webshop.model.AbstractEntity;

public interface CrudRepository<E extends AbstractEntity>
{
	 E findById(Long id);
	 List<E> fetchAll(String queryName, Function<TypedQuery<E>, TypedQuery<E>> queryFunction);
	 E saveOrUpdate(E entity);
}
