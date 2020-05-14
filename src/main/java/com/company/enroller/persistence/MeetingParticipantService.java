package com.company.enroller.persistence;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.MeetingParticipant;
import com.company.enroller.model.Participant;

import java.util.Collection;

@Component("meetingParticipantService")
public class MeetingParticipantService {

	// DatabaseConnector connector;
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

	public void deleteFromMeeting(Participant participant, Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.createQuery("DELETE FROM MeetingParticipant mp WHERE meeting_id= :id AND participant_login= :login")
				.setLong("id", meeting.getId())
				.setString("login", participant.getLogin())
				.executeUpdate();
		transaction.commit();
	}

	public Collection<Meeting> getParticipantMeetings(Participant participant) {
		String hql = "SELECT mp.meeting FROM MeetingParticipant as mp WHERE mp.participant.login= :login";
		Query query = session.createQuery(hql).setString("login", participant.getLogin());
		return query.list();
	}
}