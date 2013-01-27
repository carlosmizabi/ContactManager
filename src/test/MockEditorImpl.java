package test;

import interfaces.Contact;			// interface
import interfaces.Editor;			// interface
import interfaces.Meeting;			// interface
import interfaces.PastMeeting;		// interface
////////////////////////////////
import java.util.Calendar;			// interface
import java.util.GregorianCalendar;	// ^ implementation
import java.util.List;				// interface
import java.util.LinkedList;			// ^ implementation		
import java.util.Set;				// interface
import java.util.TreeSet;			// ^ implementation
/////////////////////////////////

import manager.ContactImpl;
import manager.EditorImpl;
import manager.MeetingImpl;
import manager.PastMeetingImpl;

public class MockEditorImpl implements Editor {
	
	private List<Meeting> meetingList;
	private List<PastMeeting> pastMeetingsList;
	private Calendar date;
	private Set<Contact> contactList;
	private int contactIdCounter = 0;
	private int meetingIdCounter = 0;

	public MockEditorImpl(Set<Contact> contacts, List<Meeting> meetings, List<PastMeeting> pastMeetings)
	//
	// The constructor sets the received set/lists and 
	// fills them with some initial values.
	// This is the expected behavior of the editor
	// when called to initiate the contactManager.
	//
	{
		setContactList(contacts);
		setMeetingList(meetings);
		setPastMeetingList(pastMeetings);
	}
	
	private void setContactList(Set<Contact> contacts)
	//
	// adds contacts to the set
	//
	{
		this.contactList = contacts;
		contacts.add(new ContactImpl("Sidharta", 1));
		contacts.add(new ContactImpl("Vishnu", 2));
		// System.out.println(contacts.size());
	}
	
	private void setMeetingList(List<Meeting> meetings)
	//
	// ads meetings to the list
	//
	{
		date = new GregorianCalendar(2012, 01, 01);
		addMeeting(meetings, date);
		addMeeting(meetings, date);	
	}
	
	private void setPastMeetingList(List<PastMeeting> pastMeetings)
	//
	// ads meetings to the list
	//
	{
		pastMeetings.add(new PastMeetingImpl());
	}
	
	private void addMeeting(List<Meeting> meetings, Calendar date)
	{
		meetings.add(new MeetingImpl(this.contactList, date, setMeetingId()));
	}
	
	private int setMeetingId()
	{
		this.meetingIdCounter++;
		return this.meetingIdCounter;
	}
	
	private int setConttactsId()
	{
		this.contactIdCounter++;
		return this.contactIdCounter;
	}
	
	
	@Override
	public boolean save(Set<Contact> contacts, List<Meeting> meetings) 
	{
		return false;
	}

}
