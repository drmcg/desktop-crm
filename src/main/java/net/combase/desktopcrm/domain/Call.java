/**
 * 
 */
package net.combase.desktopcrm.domain;

import org.joda.time.DateTime;

/**
 * @author till
 *
 */
public class Call extends AbstractCrmObject
{
	private DateTime start;
	private CallType type;
	private String description;


	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description)
	{
		this.description = description;
	}


	public CallType getType()
	{
		return type;
	}


	public void setType(CallType type)
	{
		this.type = type;
	}



	public DateTime getStart() {
		return start;
	}



	public void setStart(DateTime start) {
		this.start = start;
	}


	public Call()
	{
		super();
	}


	public Call(String id, String title)
	{
		super(id, title);
	}


	@Override
	public String getCrmEntityType()
	{
		return "Calls";
	}
}
