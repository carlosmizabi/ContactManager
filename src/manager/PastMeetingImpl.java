package manager;

import java.util.Calendar;
import java.util.Set;

import interfaces.Contact;
import interfaces.PastMeeting;

public class PastMeetingImpl extends MeetingImpl implements PastMeeting {
	
	String notes = "";
	
	public PastMeetingImpl(Set<Contact> contacts, Calendar meetDate, int id, String note)
	{
		super(contacts, meetDate, id);
		addNotes(note);
	}

	public void addNotes(String note)
	{
		this.notes += "\n" + note;
	}
	
	@Override
	public String getNotes() 
	{
		return this.notes;
	}

}
