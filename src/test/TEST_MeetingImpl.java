package test;

import static org.junit.Assert.*;
import manager.MeetingImpl;

import org.junit.Before;
import org.junit.Test;
/////////////////////////////////////
import interfaces.Contact;
import java.util.Calendar; 
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;
//////////////////////////////////////

public class TEST_MeetingImpl {
	
	MeetingImpl meeting;
	Calendar calendar;
	Set<Contact> contacts;

	@Before
	public void setUp() throws Exception 
	{
		calendar = new GregorianCalendar(2012, 01, 01);
		contacts = new TreeSet<Contact>();
		meeting = new MeetingImpl(contacts, calendar, 1);
	}

	@Test
	public void testGetDate() 
	{
		Calendar calendars = new GregorianCalendar(2012, 01, 01);
		assertEquals(calendars, meeting.getDate());
	}

	@Test
	public void testGetContacts() 
	{
		Set<Contact >contacts2 = new TreeSet<Contact>();
		assertNotSame(contacts2, meeting.getContacts());
		assertEquals(contacts, meeting.getContacts());
	}

}
