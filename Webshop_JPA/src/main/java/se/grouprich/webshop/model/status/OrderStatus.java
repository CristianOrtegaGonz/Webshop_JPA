package se.grouprich.webshop.model.status;

public enum OrderStatus
{
	PLACED("This mean ..."),
	SHIPPED("Shipped is when you ... "),
	PAYED("You have already payed ... "),
	CANCELED("You have not finished the ... ");
	
	private final String description;
	
	OrderStatus(String description)
	{
		this.description = description;
	}
	
	public String getDescription()
	{
		return description;
	}
}
