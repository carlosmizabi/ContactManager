package manager;

import java.util.Comparator;

import interfaces.Contact;

public class ContactImpl implements Contact, Comparator<ContactImpl>, Comparable<ContactImpl> {
	
	private int id = 0;
	private String name = "";
	private String notes = "";
	
	public ContactImpl(String name, String notes, int id)
	{
		this.name = name;
		addNotes(notes);
		setId(id);
	}
	
	public ContactImpl(String name, int id)
	{
		this(name,"", id);
	}
	
	@Override
	public int getId() 
	{
		return this.id;
	}

	@Override
	public String getName() 
	{
		return this.name;
	}

	@Override
	public String getNotes() 
	{
		return this.notes;
	}
	
	private void setId(int id)
	{
		this.id = id;
	}

	@Override
	public void addNotes(String note) 
	{
		this.notes =  notes + note;
		if(!this.notes.equals(""))
		{
			this.notes =  notes + "\n";
		}
	}
	
	@Override
	public String toString()
	{
	  return "Id: " + this.getId() + " Name: " + this.getName();
	}
	
	@Override
	public boolean equals(Object o)
	// they are equal if their id is the same
	{
		if(this.getId() == ((ContactImpl) o).getId())
		{
			return true;
		}
		return false;	
	}
	
	
	public int compareTo(ContactImpl o){
      return compare(this, o);
	}

	// Overriding the compare method to sort the age 
	public int compare(ContactImpl a, ContactImpl z){
      return a.getId() - z.getId();
	}
	
}
