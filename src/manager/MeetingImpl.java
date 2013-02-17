package manager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

import interfaces.Contact;
import interfaces.Meeting;

public class MeetingImpl implements Meeting {
	
	private int id = 0;
	private Calendar meetDate;
	private Set<Contact> contacts;
	
	/**
	 * The MeetingImpl Constructor
	 * 
	 * @param contacts  The meeting contacts.
	 * @param meetDate  The meeting date.
	 * @param id			An id must be provided and cannot be changed
	 * 					the client is responsible for generating and 
	 * 					managing them.The id cannot be changed once 
	 * 					assigned.
	 */
	
	public MeetingImpl(Set<Contact> contacts, Calendar meetDate, int id)
	{
		setId(id);
		setDate(meetDate);
		setContacts(contacts);
	}
	
	private void setDate(Calendar meetDate)
	{
		this.meetDate = meetDate;
	}
	
	private void setId(int id)
	{
		this.id = id;
	}

	
	private void setContacts(Set<Contact> contacts)
	{
		this.contacts = contacts;
	}

	@Override
	public int getId() 
	{
		return this.id;
	}

	@Override
	public Calendar getDate() 
	{
		return this.meetDate;
	}

	@Override
	public Set<Contact> getContacts() 
	{
		return this.contacts;
	}
	
	/**
	 * dateToString
	 * @return a string with the the date with the format dd/MM/yy
	 */
	public String dateToString()
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		return df.format(this.meetDate.getTime());
	}
	
	/**
	 * toString
	 * @return a string with the Meeting Id an the date of the meeting
	 */
	@Override
	public String toString()
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		return "Meeting Id: " + this.getId() + " Date: " + df.format(this.meetDate.getTime());
	}
	
	/**
	 * toStringWithContacts
	 * @returns a string with meeting id, date, and list of participants in meeting
	 */
	public String toStringWithContacts()
	//
	// Ads a list of Participants to the toString result
	//
	{
		String list = toString() + "\nParticipants: ";
		Iterator<Contact> itit = contacts.iterator();
		while(itit.hasNext()){
			String c = itit.next().getName();
			list = list + c;
			if(itit.hasNext())
			{
				list = list + ", ";
			}else{
				list = list + ".";
			}
		}
		return list;
	}

}











