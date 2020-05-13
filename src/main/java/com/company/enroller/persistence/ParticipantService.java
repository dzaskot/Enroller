package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	//DatabaseConnector connector;
	Session session;

	public ParticipantService() {
		session = DatabaseConnector.getInstance().getSession();
	}

	public Collection<Participant> getAll() {
		return session.createCriteria(Participant.class).list();
	}
	
	public Participant findByLogin(String login){
		return (Participant) session.get(Participant.class, login);
	}
	
	public Participant add(Participant participant){
		Transaction transaction = this.session.beginTransaction();
		session.save(participant);
		transaction.commit();
		return participant;
	}
	
	public void deleteParticipant(Participant participant){
		Transaction transaction = this.session.beginTransaction();
		session.delete(participant);
		transaction.commit();
	}
	
	public Participant update(Participant participant){
		Transaction transaction = this.session.beginTransaction();
		session.update(participant);
		transaction.commit();
		return participant;
	}
	
	public void deleteFromMeeting(Participant participant, Meeting meeting){
		Transaction transaction = this.session.beginTransaction();
		Query query = session.createQuery("DELETE FROM MeetingParticipant mp WHERE login= :login AND meeting_id= :meeting_id");
		query.setString("login", participant.getLogin());
		query.setLong("meeting_id", meeting.getId());
		transaction.commit();
	}

}
