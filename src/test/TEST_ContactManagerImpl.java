package test;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeSet;

import interfaces.Contact;
import interfaces.ContactManager;

import manager.ContactComparator;
import manager.ContactImpl;
import manager.ContactManagerImpl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TEST_ContactManagerImpl {
	
	ContactManager cmgr;
	//Comparator<Contact> comparator;

//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}

	@Before
	public void setUp() throws Exception {
		cmgr = new ContactManagerImpl();
		cmgr.addNewContact("Primero Contato", "It's the first guy.");
		cmgr.addNewContact("Segundo Contato", "It's the second guy.");
		
	}

	@After
	public void tearDown() throws Exception {
		cmgr = null;
	}

	@Test
	public void testAddFutureMeeting() {
		Calendar date = new GregorianCalendar(2014, 01, 01);
		//comparator = new ContactComparator();
		Set<Contact> contactSet = new TreeSet<Contact>();
		String james = "James T. Kirk";
		String jean = "Jean-Luc Picard";
		cmgr.addNewContact(james, "Spok");
		cmgr.addNewContact(jean, "Data");
		
		for(Contact contact : cmgr.getContacts(james))
		{ if(contact.getName().equals(james)) { contactSet.add(contact); } }
		
		for(Contact contact : cmgr.getContacts(jean))
		{ if(contact.getName().equals(jean)) { contactSet.add(contact); } }
		
		cmgr.addFutureMeeting(contactSet, date);
	}

	@Test
	public void testGetPastMeeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFutureMeeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMeeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFutureMeetingListContact() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFutureMeetingListCalendar() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPastMeetingList() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewPastMeeting() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddMeetingNotes() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewContact() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetContactsIntArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetContactsString() {
		fail("Not yet implemented");
	}

	@Test
	public void testFlush() {
		fail("Not yet implemented");
	}

}
