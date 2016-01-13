package se.grouprich.webshop.repository;

import java.util.List;
import java.util.function.Function;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import se.grouprich.webshop.model.AbstractEntity;

public abstract class AbstractJpaRepository<E extends AbstractEntity> implements CrudRepository<E>
{
	private final EntityManagerFactory factory;
	private final Class<E> entityClass;

	protected AbstractJpaRepository(final EntityManagerFactory factory, final Class<E> entityClass)
	{
		this.factory = factory;
		this.entityClass = entityClass;
	}

	@Override
	public E findById(final Long id)
	{
		EntityManager manager = factory.createEntityManager();
		try
		{
			return manager.find(entityClass, id);
		}
		catch (PersistenceException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			manager.close();
		}
	}

	@Override
	public List<E> fetchMany(final String queryName, final Function<TypedQuery<E>, TypedQuery<E>> queryFunction)
	{
		EntityManager manager = factory.createEntityManager();
		try
		{
			TypedQuery<E> typedQuery = manager.createNamedQuery(queryName, entityClass);
			return queryFunction.apply(typedQuery).getResultList();
		}
		catch (PersistenceException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			manager.close();
		}
	}

	@Override
	public E saveOrUpdate(final E entity)
	{
		return entity.getId() == null ? execute(manager -> {
			manager.persist(entity);
			return entity;
		}) : merge(entity);
	}

	public E merge(final E entity)
	{
		return execute(manager -> manager.merge(entity));
	}

	protected E execute(final Function<EntityManager, E> operation)
	{
		EntityManager manager = factory.createEntityManager();
		try
		{
			manager.getTransaction().begin();
			E result = operation.apply(manager);
			manager.getTransaction().commit();

			return result;
		}
		catch (PersistenceException e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			manager.close();
		}
	}
}
