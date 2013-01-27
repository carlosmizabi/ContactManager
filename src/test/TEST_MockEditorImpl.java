package test;

import static org.junit.Assert.*;

import manager.ContactComparator;
import manager.ContactImpl;
import manager.MeetingImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
/////////////////////////////////
import interfaces.Contact;			// interface
import interfaces.Editor;			// interface
import test.MockEditorImpl;			// ^ implementation
import interfaces.Meeting;			// interface
import interfaces.PastMeeting;		// interface
////////////////////////////////
import java.util.Calendar;			// interface
import java.util.GregorianCalendar;	// ^ implementation
import java.util.List;				// interface
import java.util.LinkedList;			// ^ implementation		
import java.util.Set;				// interface
import java.util.TreeSet;			// ^ implementation
import java.util.Comparator;
/////////////////////////////////



public class TEST_MockEditorImpl {
	
	private MockEditorImpl editor;
	private List<Meeting> meetingList;
	private List<PastMeeting> pastMeetingList;
	private Calendar date;
	private Comparator comparator;
	private Set<Contact> contactList;

	@Before
	public void setUp() throws Exception 
	{
		meetingList = new LinkedList<Meeting>();
		pastMeetingList = new LinkedList<PastMeeting>();
		comparator = new ContactComparator();
		contactList = new TreeSet<Contact>(comparator);
		editor = new MockEditorImpl(contactList, meetingList, pastMeetingList);
	}
	@After
	public void tearDown() throws Exception 
	{
		meetingList = null;
		pastMeetingList = null;
		comparator = null;
		contactList = null;
		editor = null;
	}
	@Test
	public void testContactListInitiation() 
	{
		int size = contactList.size();
		assertEquals(2, size);
		assertEquals("Sidharta", ((TreeSet<Contact>) contactList).first().getName());
		assertEquals("Vishnu", ((TreeSet<Contact>) contactList).last().getName());
	}
	
	@Test
	public void testMeetingInitiation() 
	{
		int size = meetingList.size();
		assertEquals(2, size);
		assertEquals(1, meetingList.get(0).getId());
		assertEquals(2, meetingList.get(1).getId());
		assertEquals("Vishnu", 
			((TreeSet<Contact>)meetingList.get(1).getContacts()).last().getName());
	}

}
