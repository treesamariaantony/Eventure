package fr.epita.eventure.service;

import fr.epita.eventure.conversion.ParticipantMapper;
import fr.epita.eventure.datamodel.Participant;
import fr.epita.eventure.datamodel.User;
import fr.epita.eventure.dto.ParticipantDTO;
import fr.epita.eventure.repository.ParticipantRepository;
import fr.epita.eventure.util.EventAuthorizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipantService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private ParticipantMapper participantMapper;

    @Autowired
    private EventAuthorizationUtil eventAuthorizationUtil;

    @Autowired
    private UserService userService;

    public ParticipantDTO addParticipant(ParticipantDTO participantDTO, String userId) {
        Participant participant = participantMapper.toEntity(participantDTO);

        User user = userService.getUserById(userId);

        if (eventAuthorizationUtil.isCreator(participant.getEvent(), user)) {
            Participant savedParticipant = participantRepository.save(participant);
            return participantMapper.toDto(savedParticipant);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to add participants to this event.");
        }
    }

    public ParticipantDTO updateParticipant(String participantId, ParticipantDTO updatedParticipantDTO, String userId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found."));

        User user = userService.getUserById(userId);

        if (eventAuthorizationUtil.isCreator(participant.getEvent(), user)) {
            participantMapper.updateFromDto(updatedParticipantDTO, participant);
            Participant updatedParticipant = participantRepository.save(participant);
            return participantMapper.toDto(updatedParticipant);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this participant.");
        }
    }

    public void deleteParticipant(String participantId, String userId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found."));

        User user = userService.getUserById(userId);

        if (eventAuthorizationUtil.isCreator(participant.getEvent(), user)) {
            participantRepository.delete(participant);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to delete this participant.");
        }
    }

    public List<ParticipantDTO> getAllParticipants() {
        List<Participant> participants = participantRepository.findAll();
        return participants.stream()
                .map(participantMapper::toDto)
                .collect(Collectors.toList());
    }

    public ParticipantDTO getParticipantById(String participantId) {
        Participant participant = participantRepository.findById(participantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Participant not found."));
        return participantMapper.toDto(participant);
    }
}
