package se.grouprich.webshop.repository;

import java.util.Map;

import se.grouprich.webshop.exception.RepositoryException;
@Deprecated
public interface Repository<K, T>
{
	T create(T value);
	T delete(K id) throws RepositoryException;
	T update(K id, T value) throws RepositoryException;
	T read(K id) throws RepositoryException;
	Map<K, T> readAll();
}