package se.grouprich.webshop.repository;

import se.grouprich.webshop.model.User;

public interface UserRepository extends CrudRepository<User>
{
	User fetchUserByUsername(String username);
	User changeStatus(String status);
}
