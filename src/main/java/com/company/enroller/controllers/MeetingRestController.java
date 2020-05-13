package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.MeetingParticipant;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingParticipantService;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;
	@Autowired
	ParticipantService participantService;
	@Autowired
	MeetingParticipantService meetingParticipantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
		Meeting meeting = meetingService.findById(id);
		if (meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
		Meeting foundMeeting = meetingService.findById(meeting.getId());
		if (foundMeeting != null) {
			return new ResponseEntity<String>(
					"Unable to register. Meeting with id " + foundMeeting.getId() + " already exist",
					HttpStatus.CONFLICT);
		}

		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
		Meeting foundMeeting = meetingService.findById(id);
		if (foundMeeting == null) {
			return new ResponseEntity<String>(
					"Unable to delete. Meeting with id " + id + " doesn't exist",
					HttpStatus.BAD_REQUEST);
		}

		meetingService.delete(foundMeeting);
		return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT) 
	public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting updatedMeeting){
	    Meeting foundMeeting = meetingService.findById(id);
	    if (foundMeeting == null) { 
	    	return new ResponseEntity<String>(
					"Unable to update. Meeting with id " + id + " doesn't exist",
					HttpStatus.NOT_FOUND);
		} 

	    foundMeeting.setTitle(updatedMeeting.getTitle());
	    foundMeeting.setDescription(updatedMeeting.getDescription());
	    foundMeeting.setDate(updatedMeeting.getDate());
	    
	    meetingService.update(foundMeeting);
	    return new ResponseEntity<Meeting>(foundMeeting, HttpStatus.OK); 
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipantToMeeting(@PathVariable("id") long id, @RequestBody Participant participant) {
		Meeting foundMeeting = meetingService.findById(id);
		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
		if (foundMeeting == null) {
			return new ResponseEntity<String>(
					"Unable to register. Meeting with id " + id + " doesn't exist",
					HttpStatus.NOT_FOUND);
		}
		if (foundParticipant == null) {
			return new ResponseEntity<String>(
					"Unable to register. Participant with login " + foundParticipant.getLogin() + " doesn't exist",
					HttpStatus.NOT_FOUND);
		}
		
		MeetingParticipant meetingParticipant = new MeetingParticipant(foundMeeting, foundParticipant);
		meetingParticipantService.addMeetingParticipant(meetingParticipant);
		return new ResponseEntity<Participant>(foundParticipant, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
		Meeting foundMeeting = meetingService.findById(id);
		if (foundMeeting == null) {
			return new ResponseEntity<String>(
					"Unable to register. Meeting with id " + id + " doesn't exist",
					HttpStatus.NOT_FOUND);
		}
		
		Collection<Participant> participants = meetingService.getParticipants(foundMeeting);
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}
}
