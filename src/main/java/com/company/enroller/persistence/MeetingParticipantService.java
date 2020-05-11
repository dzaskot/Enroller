package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.MeetingParticipant;
import com.company.enroller.model.Participant;

@Component("meetingParticipantService")
public class MeetingParticipantService {

	//DatabaseConnector connector;
	Session session;

	public MeetingParticipantService() {
		session = DatabaseConnector.getInstance().getSession();
	}
	
	public MeetingParticipant addMeetingParticipant(MeetingParticipant meetingParticipant) {
		Transaction transaction = this.session.beginTransaction();
		session.save(meetingParticipant);
		transaction.commit();
		return meetingParticipant;
	}

}