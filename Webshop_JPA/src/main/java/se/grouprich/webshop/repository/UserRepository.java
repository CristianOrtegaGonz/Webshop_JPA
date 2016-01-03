package se.grouprich.webshop.repository;

import java.util.List;

import se.grouprich.webshop.model.User;

public interface UserRepository extends CrudRepository<User>
{
	List<User> fetchAll();
	User fetchUserByUsername(String username);
	User changeStatus(String status);
}
