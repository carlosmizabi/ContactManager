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

/* - ///////////////////////////////////////////////////////
 * 
 * Class INDEX:
 * 
 * 1# CLASS FIELDS & CONTRUCTOR/S
 * 2# ADD (new objects to mgr's lists)
 * 3# +METHODS (all non-categorized methods)
 * 4# SETTERS
 * 5# GETTERS
 * 
 * - ///////////////////////////////////////////////// - */

public class ContactManagerImpl implements ContactManager {
	
	/*
	 * 1# - CLASS FIELDS & CONTRUCTOR/S		///////////////////////////////////////////
	 */
	
	private Editor editor;						// the editor will fetch and store all information
	Comparator<Contact> comparator;				// needed for contacts TreeSet iteration
	private List<Meeting> meetingList;			// store all meetings
	private List<PastMeeting> pastMeetingsList;	// store past meetings
	private Calendar date;	// current date should be updated ALWAYS before use -> updateMgrDate();
	private Set<Contact> contacts;				// contact list
	
	/* Meetings' and contacts' classes don't manage
	 * the id assignment and management, hence it needs
	 * to be managed by the client class
	 */
	private int meetingCounter = 0;				// counter for meetings
	private int contactCounter = 0;				// counter for contacts
	
	public ContactManagerImpl()
	{
		date = new GregorianCalendar();		// date at initialization			
		comparator = new ContactComparator();
		contacts = new TreeSet<Contact>(comparator);
		List<Meeting> meetingList = new LinkedList<Meeting>();
		List<PastMeeting> pastMeetingsList = new LinkedList<PastMeeting>();
		editor = new EditorImpl(contacts, meetingList, pastMeetingsList);
		setCounters();	// set the counter
	}
	
	
	/*
	 * 2# - ADD /////////////////////////////////////////////////////////////////
	 */

	/**
	* Creates a new record for a meeting that took place in the past.
	*
	* @throws IllegalArgumentException if the list of contacts is
	* empty, or any of the contacts does not exist
	* @throws NullPointerException if any of the arguments is null
	*/
	@Override
	public void addNewPastMeeting(Set<Contact> contactSet, Calendar meetingDate, String text) 
	{
		if(meetingDate.compareTo(this.date) > 0) // e.g., 2013 - 2012 = 1
		{
			throw new IllegalArgumentException("Date is in the Future!");
		}else{
			if(checkIfIsAValidMeeting(contactSet, meetingDate) == true)
			{
				makeAddMeeting("PAST", contactSet, meetingDate, text);
			}
		}
	}
	
	@Override
	public int addFutureMeeting(Set<Contact> contactSet, Calendar meetingDate) 
	{
		if(meetingDate.compareTo(this.date) <= 0) 
		/*
		 * for example, 2012 - 2013 = -1. In practice, however,
		 * any meeting beyond the current time will be in the future.
		 */
		{
			throw new IllegalArgumentException("Date is in the past! To add a occuring meeting create a Past Meeting.");
		}else{
			if(checkIfIsAValidMeeting(contactSet, meetingDate) == true)
			{
				return makeAddMeeting("FUTURE", contactSet, meetingDate, "").getId();
			}else{
				return 0;
			}
		}
	} 

	/**
	* Adds notes to a meeting.
	*
	* This method is used when a future meeting takes place, and is
	* then converted to a past meeting (with notes).
	*
	* It can be also used to add notes to a past meeting at a later date.
	*
	* @param id the ID of the meeting
	* @param text messages to be added about the meeting.
	* @throws IllegalArgumentException if the meeting does not exist
	* @throws IllegalStateException if the meeting is set for a date in the future
	* @throws NullPointerException if the notes are null
	*/
	@Override
	public void addMeetingNotes(int id, String text) 
	{
		updateMeetingLists();
		Boolean idExists = false;
		Boolean futureMeeting = false;
		Meeting tempMeeting = null;
		
		// Join all(future and past) Meetings in a temporary meeting List
		//
		List<Meeting> tempMeetingList = new LinkedList<Meeting>();
		tempMeetingList.addAll(meetingList);
		tempMeetingList.addAll(pastMeetingsList);
		
		for(Meeting meeting : tempMeetingList)
		{
			if(meeting.getId() == id) { idExists = true; }
			
			if(meeting.getDate().compareTo(this.date) > 0) { futureMeeting = true; }
			
			tempMeeting = meeting;
		}

		if(idExists == false)
		{
			throw new IllegalArgumentException("There are no meetings with the id: " + id);
		}else if(futureMeeting == true)
		{
			throw new IllegalStateException ("Sorry, but you can only add notes to PAST meetings!");
		}else if(text == null){
			throw new NullPointerException ("The note Cannot be NULL!");
		}
		
		// if meeting is not null cast it to PastMeeting and add the note
		//
		if(tempMeeting != null){ ((PastMeetingImpl)tempMeeting).addNotes(text); }
		
	} // close addMeetingNotes(...); // 

	@Override
	public void addNewContact(String name, String notes) 
	{
		if(name == null | notes == null) 
		{
			throw new NullPointerException("One of the parameters is null!"); 
		}else{
			ContactImpl contact = new ContactImpl(name, notes, getContactNewId());
			contacts.add(contact);
		}
	}
	
	/*
	 * 3# - +METHODS /////////////////////////////////////////////////////////////////
	 */
	
	/*
	 * This method will look for meetings in the meeting lists 
	 * compare their dates to present. Any meeting that is not 
	 * longer a future meeting to the past meetings list.
	 */
	private void updateMeetingLists()
	{
		updateMgrDate();
		for(Meeting meeting : meetingList)
		{
			if(meeting.getDate().compareTo(this.date) < 0)
			{
				// remove it from meetingList and on pastMeetingList
				pastMeetingsList.add((PastMeeting)meeting);
				meetingList.remove(meeting);
			}		
		}
	}	// close updateMeetingList(); //
	
	/*
	 * Update the date to current time when using date.
	 * It is always good to update the current date.
	 * The initialized date at construction might have
	 * became out dated during use.
	 */
	private void updateMgrDate() { this.date = new GregorianCalendar(); }
	
	/*
	 * Checks that parameters are not null and that contact set is not empty
	 * TRUE = all is swell
	 */
	private boolean checkIfIsAValidMeeting(Set<Contact> contactSet, Calendar meetingDate)
	{
		updateMgrDate();
		if(contactSet == null | meetingDate == null)
		{
			throw new IllegalArgumentException("One of the parameters is null! This is not allowed");
			
		}else if(!this.contacts.containsAll(contactSet)) 
			//
			// Do all contacts on the new meeting's set already exist in Mgr's contact list?
		{
			throw new IllegalArgumentException("Cannot Add a contact list with unknown Contacts!" +
					"\n" + "Please add each new contat first");
		}else
		{	
			return true;
		}
	} // close checkIfIsAValidMeeting(...); //
	
	/*
	 * 
	 */
	private Meeting makeAddMeeting(String meetingType, Set<Contact> contactSet, Calendar meetingDate, String note)
	{
		meetingType = meetingType.toUpperCase();
		Meeting newMeeting;
		
		if(meetingType.equals("FUTURE") == true)
		{
			newMeeting = new MeetingImpl(contactSet, meetingDate, getMeetingNewId());
			this.meetingList.add(newMeeting);
			return (Meeting)newMeeting;
			
		}else if(meetingType.equals("PAST") == true){
			
			newMeeting = new PastMeetingImpl(contactSet, meetingDate, getMeetingNewId(), note);
			this.pastMeetingsList.add((PastMeeting)newMeeting);
			return (Meeting)newMeeting;
		}else{
			throw new IllegalArgumentException("The meeting type is incorrect! Only \"FUTURE\" " +
					"or \"PAST\" is allowed!" + "\n" + "Check your code.");
		}
	}
	

	@Override
	public void flush() { editor.save(contacts, meetingList); }

	/*
	 * 4# - SETTERS /////////////////////////////////////////////////////////////////
	 */

	/* set the counter to initial list sizes
	 * fetch the lists present size
	 */
	private void setCounters()
	{
		meetingCounter = meetingList.size();
		contactCounter = contacts.size();
	}
	
	/*
	 * 5# - GETTERS /////////////////////////////////////////////////////////////////
	 */
	
	private int getContactNewId()
	{
		contactCounter++;	// increment contacts' counter
		return contactCounter;
	}
	private int getMeetingNewId()
	{
		meetingCounter++;  // increment meetings' counter
		return meetingCounter;
	}
	
	@Override
	public PastMeeting getPastMeeting(int id) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FutureMeeting getFutureMeeting(int id) 
	{
		return null;
	}

	@Override
	public Meeting getMeeting(int id) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Contact> getContacts(int... ids) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Contact> getContacts(String name) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
} // close class ContactManagerImpl{} //
