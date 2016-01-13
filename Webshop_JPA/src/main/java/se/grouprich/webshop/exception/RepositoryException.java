package se.grouprich.webshop.exception;

public final class RepositoryException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public RepositoryException(String message)
	{
		super(message);
	}
}
