package com.company.enroller.persistence;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

@Component("meetingService")
public class MeetingService {

	DatabaseConnector connector;
	Session session;

	public MeetingService() {
		session = DatabaseConnector.getInstance().getSession();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting m ORDER BY LOWER(m.title) ";
		Query query = session.createQuery(hql);
		return query.list();
	}
	
	public Meeting findById(Long id){
		return (Meeting) session.get(Meeting.class, id);
	}

	public List<Meeting> findByTitle(String string){
		Query query = session.createQuery("FROM Meeting as m WHERE m.title LIKE :searchString");
		query.setParameter("searchString", "%" + string + "%");
		return query.list();
	}

	public Meeting add(Meeting meeting){
		Transaction transaction = this.session.beginTransaction();
		session.save(meeting);
		transaction.commit();
		return meeting;
	}
	
	public void delete(Meeting meeting){
		Transaction transaction = this.session.beginTransaction();
		session.delete(meeting);
		transaction.commit();
	}
	
	public Meeting update(Meeting meeting){
		Transaction transaction = this.session.beginTransaction();
		session.update(meeting);
		transaction.commit();
		return meeting;
	}

	public Collection<Participant> getParticipants(Meeting meeting) {
		return this.findById(meeting.getId()).getParticipants();
	}

}
