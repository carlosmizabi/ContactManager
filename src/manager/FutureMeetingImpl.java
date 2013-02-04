package manager;

import java.util.Calendar;
import java.util.Set;

import interfaces.Contact;
import interfaces.FutureMeeting;

public class FutureMeetingImpl extends MeetingImpl implements FutureMeeting {

	public FutureMeetingImpl(Set<Contact> contacts, Calendar meetDate, int id){
		super(contacts, meetDate,id);
	}
}
