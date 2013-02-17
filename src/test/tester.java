package test;

import static org.junit.Assert.*;
import manager.ContactComparator;
import manager.ContactImpl;
import manager.ContactManagerImpl;
import manager.EditorImpl;
import manager.MeetingImpl;
import manager.XmlFormat;

import org.junit.Before;
import org.junit.Test;
/////////////////////////////////
import interfaces.Contact;			// interface
import interfaces.ContactManager;
import interfaces.Editor;			// interface
import test.MockEditorImpl;			// ^ implementation
import interfaces.Meeting;			// interface
import interfaces.PastMeeting;		// interface

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
////////////////////////////////
import java.util.Calendar;			// interface
import java.util.Comparator;
import java.util.GregorianCalendar;	// ^ implementation
import java.util.List;				// interface
import java.util.LinkedList;			// ^ implementation		
import java.util.Scanner;
import java.util.Set;				// interface
import java.util.TreeSet;			// ^ implementation
/////////////////////////////////

public class tester {
	
	
	private static Comparator<Contact> comparator = new ContactComparator();
	private static Set<Contact> contactList = new TreeSet<Contact>(comparator);
	private static List<Meeting> meetingList = new LinkedList<Meeting>();
	private static List<PastMeeting> pastMeetingsList = new LinkedList<PastMeeting>();

	public static void main(String[] args) {
		
		ContactManager mgr = new ContactManagerImpl();
		Calendar date12 = new GregorianCalendar(2012, 1, 2);
		Calendar date14 = new GregorianCalendar(2014, 1, 2);
		Calendar date15 = new GregorianCalendar(2015, 1, 1);
		Calendar date16 = new GregorianCalendar(2016, 1, 2);

		String fstString = "First";
		String sndString = "Second";
		String sid = fstString;
		mgr.addNewContact(fstString, "");
		mgr.addNewContact(sndString, "");
		
		Contact firstContact = null;
		Set<Contact> contactList = mgr.getContacts(sid);
		contactList.addAll(mgr.getContacts(sndString));
		
		for(Contact contact : contactList)
		{
			if(contact.getName().equals(sid)){
				firstContact = contact;
			}
		}
		System.out.println(firstContact.getName());
		//Meeting meet01 = new MeetingImpl(contactList, date15, 1);
		mgr.addFutureMeeting(contactList, date15); 
		mgr.addFutureMeeting(contactList, date16); 
		mgr.addFutureMeeting(contactList, date14); 
		
		if(firstContact != null){
			List<Meeting> meetList = mgr.getFutureMeetingList(firstContact);
			
			System.out.println("meet List size " + meetList.size());
			for(Meeting meeting : meetList)
			{
				System.out.println("Meetings ids:" + meeting.getId());
			}
		}
		
		mgr.flush();
		mgr.addNewPastMeeting(contactList, date12, "Very Weird Meeting, full of Teletubies!");
		
		ContactManager mgr2 = new ContactManagerImpl();
		
		System.out.println(mgr.getMeeting(1).getContacts().toString());
		mgr.addNewContact("LaLa", "");
		
		for(Contact contact : mgr.getContacts(1,2,3))
		{
			System.out.println(contact.toString());
		}
		
		mgr.flush();
		
//		ContactManagerImpl cmgr = new ContactManagerImpl();
//		String james = "James T. Kirk";
//		String jean = "Jean-Luc Picard";
//		Comparator<Contact> comparator = new ContactComparator();
//		Set<Contact> contactSet = new TreeSet<Contact>(comparator);
//		Boolean exists = false;
//		Calendar date = new GregorianCalendar(2012, 4, 4);
//		cmgr.addNewPastMeeting(contactSet, date, "Helloooaa!");
//		Contact contacto = null;
//		
//		// Need to iterate over the set to extract the contact
//		// If contact is found then assign it to contacto
//		// so that it can get a list of meeting from the cmgr
//		//
//		for(Contact contact : contactSet)
//		{ 
//			if(contact.getName().equals(jean)){ contacto = contact; } 
//		}
//		List<PastMeeting> meetings = cmgr.getPastMeetingList(contacto);
//		for(PastMeeting meeting : meetings)
//		{
//			
//			if(meeting.getDate().equals(date)){ exists = true; }
//		}
		
//		List<Integer> list = new LinkedList<Integer>();
//		
//		for(int i =0; i < 10; i++)
//		{
//			list.add((Integer)i);
//		}
//		System.out.println("Checking the indexOf" + list.get(10));
		
		
		
//		System.out.println("Tester contactList's size sent to editor:" +  contactList.size());
//		//System.out.println(meetingList.get(1).toString());
//		System.out.println(((MeetingImpl) meetingList.get(1)).toStringWithContacts());
//		MockEditorImpl editor = new MockEditorImpl(contactList, meetingList, pastMeetingsList);
		
		// LETS TEST THE File READER
		
		
		
//		editor.printClipBoard();
//		BufferedReader in;
//		try{
//			in = new BufferedReader(new FileReader("userdata99.xml"));
//			String line;
		
//			while ((line = in.readLine()) != null) {
//				
//				if(line.equals("<contact>"))
//				{
//					System.out.println("Aleluia");
//				}
//			}
//		}catch(IOException ex)
//		{
//			System.out.println("could not read file");
//		}
		
//		String line = "<contacte>999</id>";
//		System.out.println(line.replaceAll("<(/)*\\w*>", ""));
//		line = "<meeting>10/10/10</id>";
//		String line2 = line;
//		System.out.println(line = line.replaceAll(">\\w*</\\w*>", ">"));
//		System.out.println(line.replaceAll("<*>*", ""));
//		line2 = line2.replaceAll("<+", "");
//		System.out.println(line2);
//		System.out.println(line2.replaceAll(">[\\w]*[/*\\w*]*", ""));
		
//		String dateString = "14/12/14";
//		int index = dateString.indexOf("/");
//		String yearstr = "20" + dateString.substring(6);
//		int year = Integer.parseInt(yearstr);
//		
//				
//				System.out.println("Year " + year );

	
//	private static void makeFile()
//	{
////		File file = new File("userdata.xml");
////		PrintWriter out = new PrintWriter(file);
//		
//		File file = new File("userdata.xml");
//		PrintWriter out = null;
//		try {
//			out = new PrintWriter(file);
//			XmlFormat xml = new XmlFormat();
//			out.write(xml.getSkeleton());
//		} catch (FileNotFoundException ex) {
//			// This happens if file does not exist and cannot be created,
//			// or if it exists, but is not writable
//			System.out.println("Cannot write to file " + file + ".");
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		} finally {
//			out.close();
//		}
//	}
//	private static boolean fileThere()
//	{
//		try {
//			Scanner scanner = new Scanner(new FileReader("userdata.xml"));
//		
//		}catch (IOException ex)
//		{
//			return false;
////			System.out.println("File NOT Found!");
//		}
//		return true;
//	}
		
	}
	
//	 class meetingDateComparator implements Comparable<Meeting>{
//
//			@Override
//			public int compare(Meeting arg0, Meeting arg1) {
//				if(arg0.getDate().getTimeInMillis() == arg1.getDate().getTimeInMillis())
//				{
//					return 0;
//				}else if(arg0.getDate().getTimeInMillis() > arg1.getDate().getTimeInMillis()){
//					return 1;
//				}else{
//					return -1;
//				}
//			}
//
//			@Override
//			public int compareTo(Meeting arg0) {
//				// TODO Auto-generated method stub
//				return 0;
//			}	
//		 } // close class dateComparator{} //
	
}











	

