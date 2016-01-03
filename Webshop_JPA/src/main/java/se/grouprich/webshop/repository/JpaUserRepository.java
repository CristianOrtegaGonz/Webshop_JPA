package se.grouprich.webshop.repository;

import static java.util.function.Function.*;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import se.grouprich.webshop.model.User;

public final class JpaUserRepository extends AbstractJpaRepository<User> implements UserRepository
{
	public JpaUserRepository(EntityManagerFactory factory)
	{
		super(factory, User.class);
	}

	@Override
	public List<User> fetchAll()
	{
		return fetchMany("User.FetchAll", identity());
	}

	@Override
	public User fetchUserByUsername(String username)
	{
		List<User> fetchedUsers = fetchMany("User.FetchUserByUsername", queryFunction -> queryFunction.setParameter("username", username));
		return fetchedUsers.get(0);
	}
}
