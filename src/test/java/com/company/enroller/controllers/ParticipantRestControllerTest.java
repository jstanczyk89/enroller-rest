package com.company.enroller.controllers;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(ParticipantRestController.class)
public class ParticipantRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private MeetingService meetingService;

	@MockBean
	private ParticipantService participantService;

	@Test
	public void getParticipants() throws Exception {
		Participant participant = new Participant();
		participant.setLogin("testlogin");
		participant.setPassword("testpassword");

		Collection<Participant> allParticipants = singletonList(participant);
		given(participantService.getAll()).willReturn(allParticipants);

		mvc.perform(get("/participants").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1))).andExpect(jsonPath("$[0].login", is(participant.getLogin())));
	}

	@Test
	public void getParticipant() throws Exception {
		Participant participant = new Participant();
		participant.setLogin("testlogin");
		participant.setPassword("testpassword");

		given(participantService.findByLogin("testlogin")).willReturn(participant);

		mvc.perform(get("/participants/testlogin").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(new ObjectMapper().writeValueAsString(participant)));
	}

	@Test
	public void postParticipant() throws Exception {
		Participant Participant = new Participant();
		Participant.setLogin("testlogin");
		Participant.setPassword("testpassword");

		String partcipantStr = new ObjectMapper().writeValueAsString(Participant);

		mvc.perform(MockMvcRequestBuilders.post("/participants").content(partcipantStr)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().string(new ObjectMapper().writeValueAsString(Participant)));
	}

	@Test
	public void addExistingParticipant() throws Exception {

		Participant existingParticipant = new Participant();
		existingParticipant.setLogin("testlogin");
		existingParticipant.setPassword("testpassword");
		given(participantService.findByLogin(existingParticipant.getLogin())).willReturn(existingParticipant);

		String existingPartcipantStr = new ObjectMapper().writeValueAsString(existingParticipant);

		mvc.perform(MockMvcRequestBuilders.post("/participants").content(existingPartcipantStr)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict());
	}
	
	@Test
	public void deleteParticipant() throws Exception {

		Participant ParticipantForDeletion = new Participant();
		ParticipantForDeletion.setLogin("testlogin");
		ParticipantForDeletion.setPassword("testpassword");
		given(participantService.findByLogin("testlogin")).willReturn(ParticipantForDeletion);
		given(participantService.findByLogin("testlogin")).willReturn(ParticipantForDeletion);

		String existingPartcipantStr = new ObjectMapper().writeValueAsString(ParticipantForDeletion);

		mvc.perform(MockMvcRequestBuilders.post("/participants").content(existingPartcipantStr)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

}
