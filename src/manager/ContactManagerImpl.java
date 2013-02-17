package manager;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
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

	private EditorImpl editor;					// the editor will fetch and store all information
	Comparator<Contact> comparator;				// needed for contacts TreeSet iteration
	private List<Meeting> meetingList;			// store all meetings
	private List<PastMeeting> pastMeetingsList;	// store past meetings
	private Calendar date;	// current date should ALWAYS be updated before use -> updateMgrDate();
	private Set<Contact> contactsList;				// contact list
	private static meetingDateComparator meetingDateComparator;
	
	/* Meetings' and contacts' classes don't manage
	 * the id assignment and management, hence it needs
	 * to be managed by the client class
	 */
	private int meetingCounter = 0;				// counter for meetings
	private int contactCounter = 0;				// counter for contacts
	
	public ContactManagerImpl()
	{
		meetingDateComparator = new meetingDateComparator(); // for data comparisons
		date = new GregorianCalendar();		// date at initialization			
		comparator = new ContactComparator();
		this.contactsList = new TreeSet<Contact>(comparator);
		this.meetingList = new LinkedList<Meeting>();
		this.pastMeetingsList = new LinkedList<PastMeeting>();
		editor = new EditorImpl(this.contactsList, this.meetingList, this.pastMeetingsList);
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

	@Override
	public void addMeetingNotes(int id, String text) 
	{
		updateMeetingLists();
		Boolean idExists = false;
		Boolean futureMeeting = false;
		Meeting tempMeeting = null;
		
		// Join all(future and past) Meetings in a temporary meeting List
		//
		List<Meeting> tempMeetingList = getOneBigList();
		
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
			contactsList.add(contact);
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
		// This list will hold all Meetings converted to PastMeetings
		//
		List<Meeting> removalList = new LinkedList<Meeting>();
		updatedMgrDate();
		for(Meeting meeting : meetingList)
		{
			if(meeting.getDate().compareTo(this.date) < 0)
			{
				// remove it from meetingList and on pastMeetingList
				PastMeeting transferedMeeting = new PastMeetingImpl(meeting.getContacts(), meeting.getDate(), meeting.getId(), "");
				pastMeetingsList.add(transferedMeeting);
				removalList.add(meeting); 
			}		
		}
		
		// Lets remove the meetings from the meetingList if any
		//
		if(removalList.isEmpty() == false){
			for(Meeting meeting : removalList){
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
	private Calendar updatedMgrDate() { return this.date = new GregorianCalendar(); }
	
	/*
	 * Checks that parameters are not null and that contact set is not empty
	 * TRUE = all is swell
	 */
	private boolean checkIfIsAValidMeeting(Set<Contact> contactSet, Calendar meetingDate)
	{
		updatedMgrDate();
		if(contactSet == null | meetingDate == null)
		{
			throw new IllegalArgumentException("One of the parameters is null! This is not allowed");
			
		}else if(this.contactsList.containsAll(contactSet) == false) 
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
	public void flush() 
	{ 
		editor.save(contactsList, getOneBigList()); 
	}
	
	/*
	 * This method joins the meeting Lists (Past and Future) 
	 * into one big list.
	 */
	private List<Meeting> getOneBigList()
	{
		List<Meeting> meetingList = new LinkedList<Meeting>();
		meetingList.addAll(this.meetingList);
		meetingList.addAll(this.pastMeetingsList);
		return meetingList;
	}

	/*
	 * 4# - SETTERS /////////////////////////////////////////////////////////////////
	 */

	/* set the counter to initial list sizes
	 * fetch the lists present size
	 */
	private void setCounters()
	{
		meetingCounter = meetingList.size();
		contactCounter = contactsList.size();
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
	public Meeting getMeeting(int id) 
	{
		// temp meeting list of all meetings (past and future)
		//
		List<Meeting> tempMeetingList = getOneBigList();
		
		// Iterate over temporary list and if found return it
		//
		for(Meeting meeting : tempMeetingList) { if(meeting.getId() == id) { return meeting; } }
		
		return null;
	} // close getMeeting(...) //
	
	@Override
	public PastMeeting getPastMeeting(int id) 
	{
		Meeting meeting = getMeeting(id);
		if(meeting.getDate().compareTo(updatedMgrDate()) > 0)
		{ 
			throw new IllegalArgumentException("The id passed is for a future " +
					"meeting and not for a past meeting!");
		}
		return (PastMeeting)meeting;
	} // closes getPastMeeting(...); //

	@Override
	public FutureMeeting getFutureMeeting(int id) 
	{
		Meeting meeting = getMeeting(id);
		if(meeting != null) 
		{  
			if(meeting.getDate().compareTo(updatedMgrDate()) < 0)
			{ 
				throw new IllegalArgumentException("The id passed is for a past " +
						"meeting and not for a future meeting!");
			}
		}
		return (FutureMeeting)meeting;
		
	} // closes getFutureMeeting(...); //

	@Override
	public List<Meeting> getFutureMeetingList(Contact contact) 
	/*
	 * Join the Lists and iterate over each contact. Then extract
	 * the Contact<Set> and see if the contact is there. If it
	 * is then add it to the return list.
	 */ 
	{
		updateMeetingLists();
		List<Meeting> returnList = new LinkedList<Meeting>();
		if(!this.contactsList.contains(contact))
		{
			throw new IllegalArgumentException("Contact does not exist!");
		}else{
			
			// (1) Iterate over the meetingList
			//
			for(Meeting meeting : meetingList)
			{				
				// (2) Check the date is in future
				//
				if(meeting.getDate().compareTo(updatedMgrDate()) > 0)
				{
					// (3) Peep and check that its contactSet has the contact
					//
					if(meeting.getContacts().contains(contact) == true)
					{
						// (4) If it does then add it to the return list
						//
						returnList.add(meeting);
					}
				}
			}
		}
		
		return orderListChronologically(returnList);
	}
	
	

	public List<Meeting> orderListChronologically(List<Meeting> returnList) {
		
		TreeSet<Meeting> meetingsTree = new TreeSet<Meeting>(meetingDateComparator);
		
		for(Meeting meeting : returnList)
		{
			meetingsTree.add(meeting);
		}
		returnList.clear();
		returnList.addAll(meetingsTree);
		return returnList;
	}

	private List<Meeting> orderListChronologically0(List<Meeting> returnList) 
	{
		// Because the LinkedList does not have a getNext one needs to keep
		// track of the next Meeting index. In order to compare current with
		// next and to use it to compare backwards.
		// 
		int nextMeeting = 1;
		
		// (1) Iterate over the returnList
		//
		for (Meeting meeting : returnList)
		{
			// (2) compare this meeting with nextMeeting if result is a 
			// negative number (2012 - 2013 = -1) then it is ordered correctly.
			// If NOT and it is greater than 0 then the next meeting takes place before
			// the current meeting: must send it to its place somewhere before current
			//
			if(meeting.getDate().compareTo(returnList.get(nextMeeting).getDate()) > 0) 
			{
				// (3) So the next meeting should be somewhere before current meeting
				// lets iterate over the return list backwards 
				// jMeeting starts on the current meeting's list index -1 or 
				// more precisely (nextMeeting-2)
				//
				for(int jMeeting = (nextMeeting-2); jMeeting >= 0; jMeeting--)
				{
					// Making sure that jMeeting is not an null index of returnList
					//
					if (returnList.get(jMeeting) != null){
						
						// (4) If (nextMeeting - jMeeting) is smaller than 0 (ex. -1) then
						// jMeeting takes place after the
						//
						if( returnList.get(nextMeeting).getDate().compareTo(returnList.get(jMeeting).getDate())
								< 0 )
						{
							// (5) remove it from list and add it at the position jMeeting+1
							//
							returnList.add(jMeeting+1, returnList.remove(nextMeeting));
						}
					}else{	// the nextMeeting has the "smallest" date so far
						returnList.add(0, returnList.remove(nextMeeting));
					}
				} //for(int jMeeting ... //
			} // closes if (...) date comparison //
			
			nextMeeting++; 
			
		}// closes for (Meeting meeting : returnList) //
		
		return returnList;
	} // closes orderListChronologically(...) //

	@Override
	public List<Meeting> getFutureMeetingList(Calendar date) 
	{
		List<Meeting> returnList = new LinkedList<Meeting>();
		List<Meeting> allMeetings = getOneBigList();
		
		for(Meeting meeting : allMeetings)
		{
			if(meeting.getDate().compareTo(updatedMgrDate()) > 0)
			{
				returnList.add(meeting);
			}
		}
		return returnList;
	}

	@Override
	public List<PastMeeting> getPastMeetingList(Contact contact) 
	{
		List<PastMeeting> returnList = new LinkedList<PastMeeting>();
		if(!this.contactsList.contains(contact))
		{
			throw new IllegalArgumentException("Contact does not exist!");
		}else{
			for(PastMeeting meeting : pastMeetingsList)
			{
				if(meeting.getDate().compareTo(updatedMgrDate()) < 0)
				{
					returnList.add(meeting);
				}
			}
		}
		return returnList;
	}

	/*
	* Returns a list containing the contacts that correspond to the IDs.
	*
	* @param ids an arbitrary number of contact IDs
	* @return a list containing the contacts that correspond to the IDs.
	* @throws IllegalArgumentException if any of the IDs does not correspond to a real contact
	*/
	@Override
	public Set<Contact> getContacts(int... ids) 
	{
		Set<Contact> returnList = new TreeSet<Contact>(comparator);
		Boolean existingId = true;
		for(int id : ids)
		{
			if(existingId == true)
			{
				for(Contact contact : this.contactsList)
				{
					if(contact.getId() == id) { returnList.add(contact); }
				}
			}else{
				existingId = false;
				throw new IllegalArgumentException("The id: " + id + " does not exist!");
			}
		}
		return returnList;
	}
	
	@Override
	public Set<Contact> getContacts(String name) 
	{
		Set<Contact> returnList = new TreeSet<Contact>(comparator);
		
		if(name == null){
			throw new NullPointerException("The parameter name cannot be null!");
		}else{
			for(Contact contact : this.contactsList)
			{
				if(contact.getName().contains(name)){
					returnList.add(contact);
				}
			}	
		}
		return returnList;
	}
	
	/**
	 * @return string of backup status "ON" or "OFF"
	 */
	public String getBackupStatus()
	{
		return editor.getBackupStatus();
	}
	
	/**
	 * @param onOff it must either "ON" or "OFF"
	 * @return string of backup status 
	 * @throws IllegalArgumentException if the @param is null.
	 */
	public void setBackupStatus(String onOff)
	{
		try {
			editor.setBackup(onOff);
		}catch(IllegalArgumentException e){
			e.getMessage();
		}
	}
	
	 class meetingDateComparator implements Comparator<Meeting>
	 {
		@Override
		public int compare(Meeting arg0, Meeting arg1) {
			if(arg0.getDate().getTimeInMillis() == arg1.getDate().getTimeInMillis())
			{
				return 0;
			}else if(arg0.getDate().getTimeInMillis() > arg1.getDate().getTimeInMillis()){
				return 1;
			}else{
				return -1;
			}
		}	
	 } // close class dateComparator{} //
	
} // close class ContactManagerImpl{} //


