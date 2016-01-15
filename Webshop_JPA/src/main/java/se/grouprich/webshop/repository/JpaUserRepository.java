package se.grouprich.webshop.repository;

import static java.util.function.Function.*;

import java.util.List;

import javax.persistence.EntityManagerFactory;

import se.grouprich.webshop.model.User;

public final class JpaUserRepository extends AbstractJpaRepository<User> implements UserRepository
{
	public JpaUserRepository(final EntityManagerFactory factory)
	{
		super(factory, User.class);
	}

	@Override
	public List<User> fetchAll()
	{
		return fetchMany("User.FetchAll", identity());
	}

	@Override
	public User fetchUserByUsername(final String username)
	{
		List<User> usersFetchedByUsername = fetchMany("User.FetchUserByUsername", queryFunction -> queryFunction.setParameter("username", username));
		if (usersFetchedByUsername.isEmpty())
		{
			return null;
		}
		return usersFetchedByUsername.get(0);
	}
}
