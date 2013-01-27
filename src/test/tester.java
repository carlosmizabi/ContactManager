package test;

import static org.junit.Assert.*;
import manager.ContactComparator;
import manager.ContactImpl;
import manager.EditorImpl;
import manager.MeetingImpl;
import manager.XmlFormat;

import org.junit.Before;
import org.junit.Test;
/////////////////////////////////
import interfaces.Contact;			// interface
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
		
		// TODO Auto-generated method stub
		
		
//		System.out.println("Tester contactList's size sent to editor:" +  contactList.size());
//		//System.out.println(meetingList.get(1).toString());
//		System.out.println(((MeetingImpl) meetingList.get(1)).toStringWithContacts());
//		MockEditorImpl editor = new MockEditorImpl(contactList, meetingList, pastMeetingsList);
		
		// LETS TEST THE File READER
		
		EditorImpl editor = new EditorImpl(contactList, meetingList, pastMeetingsList);
		System.out.println("ContactList " + contactList.size());
		System.out.println("MeettingList " + meetingList.size());
		System.out.println("pastMeetingList  " + pastMeetingsList.size());
		
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
	
}











	

