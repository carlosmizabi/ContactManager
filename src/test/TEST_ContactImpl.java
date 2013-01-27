package test;

import static org.junit.Assert.*;

import manager.ContactImpl;

import org.junit.Test; 
import org.junit.Before;


public class TEST_ContactImpl {
	
	ContactImpl contact;

	@Before
	public void setUp() throws Exception 
	{
		contact = new ContactImpl("Siddharta", 1);
	}
	

	@Test
	public void testGetId() 
	{
		assertEquals(1, contact.getId());
		ContactImpl contact2 = new ContactImpl("Second", 2);
		assertEquals(1, contact.getId());
		assertEquals(2, contact2.getId());
		
	}

	@Test
	public void testGetName() 
	{
		assertEquals("Siddharta", contact.getName());
	}

	@Test
	public void testGetNotes() 
	{
		// Construct another contact and this time 
		// create with a initial note.
		//
		contact = new ContactImpl("Sidh", "A note about Sidh.", 1);
		assertEquals("A note about Sidh.\n", contact.getNotes());
	}

	@Test
	public void testAddGetNotes() 
	{
		// Get notes after adding some notes 
		//
		contact.addNotes("Las Notas del Segnor");
		assertEquals("Las Notas del Segnor\n", contact.getNotes());
	}

}















