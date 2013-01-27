package manager;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


import interfaces.Contact;
import interfaces.ContactManager;
import interfaces.Editor;
import interfaces.FutureMeeting;
import interfaces.Meeting;
import interfaces.PastMeeting;

public class ContactManagerImpl implements ContactManager {
	
	private Editor editor;
	Comparator<Contact> comparator;
	private List<Meeting> meetingList;
	private List<PastMeeting> pastMeetingsList;
	private Calendar date;
	private Set<Contact> contacts;
	
	private int meetingCounter = 0;
	private int contactCounter = 0;
	
	public ContactManagerImpl()
	{
		date = new GregorianCalendar();
		comparator = new ContactComparator();
		contacts= new TreeSet<Contact>(comparator);
		List<Meeting> meetingList = new LinkedList<Meeting>();
		List<PastMeeting> pastMeetingsList = new LinkedList<PastMeeting>();
		editor = new EditorImpl(contacts, meetingList, pastMeetingsList);
		setCounters();
	}

	// set the counter to initial list sizes
	//
	private void setCounters()
	{
		meetingCounter = meetingList.size();
		contactCounter = contacts.size();
	}
	
	private int getContactNewId()
	{
		contactCounter++;
		return contactCounter;
	}
	private int getMeetingNewId()
	{
		meetingCounter++;
		return meetingCounter;
	}
	@Override
	public int addFutureMeeting(Set<Contact> contacts, Calendar date) 
	{
		if(date.compareTo(this.date) > 0) {
			throw new IllegalArgumentException("Date is in the pasty!"); 
		}else if(!contacts.containsAll(contacts)) {
			throw new IllegalArgumentException("Cannot Add contact list with unknown Contacts!" +
					"\n" + "Please add them first");
		}else{	
		MeetingImpl meeting = new MeetingImpl(contacts, date, getMeetingNewId());
		meetingList.add(meeting);
		return this.meetingCounter;
		}
	}

	@Override
	public PastMeeting getPastMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FutureMeeting getFutureMeeting(int id) {
		
		return null;
	}

	@Override
	public Meeting getMeeting(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addNewPastMeeting(Set<Contact> contacts, Calendar date,
			String text) {
		MeetingImpl meeting = new MeetingImpl(contacts, date, getMeetingNewId());
		meetingList.add(meeting);

	}

	@Override
	public void addMeetingNotes(int id, String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addNewContact(String name, String notes) {
		if(name == null | notes == null) {
			throw new NullPointerException("Date is in the pasty!"); 
		}else{
			ContactImpl contact = new ContactImpl(name, notes, getContactNewId());
			contacts.add(contact);
		}
		
	}

	@Override
	public Set<Contact> getContacts(int... ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Contact> getContacts(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO 
		editor.save(contacts, meetingList);
	}

}
