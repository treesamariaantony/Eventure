package fr.epita.eventure.controller;


import fr.epita.eventure.dto.ParticipantDTO;
import fr.epita.eventure.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    @PostMapping
    public ResponseEntity<ParticipantDTO> addParticipant(@RequestBody ParticipantDTO participantDTO, @RequestHeader("userId") String userId) {
        ParticipantDTO createdParticipant = participantService.addParticipant(participantDTO, userId);
        return ResponseEntity.ok(createdParticipant);
    }

    @PutMapping("/{participantId}")
    public ResponseEntity<ParticipantDTO> updateParticipant(@PathVariable String participantId, @RequestBody ParticipantDTO participantDTO, @RequestHeader("userId") String userId) {
        ParticipantDTO updatedParticipant = participantService.updateParticipant(participantId, participantDTO, userId);
        return ResponseEntity.ok(updatedParticipant);
    }

    @DeleteMapping("/{participantId}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable String participantId, @RequestHeader("userId") String userId) {
        participantService.deleteParticipant(participantId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ParticipantDTO>> getAllParticipants() {
        List<ParticipantDTO> participants = participantService.getAllParticipants();
        return ResponseEntity.ok(participants);
    }

    @GetMapping("/{participantId}")
    public ResponseEntity<ParticipantDTO> getParticipantById(@PathVariable String participantId) {
        ParticipantDTO participant = participantService.getParticipantById(participantId);
        return ResponseEntity.ok(participant);
    }
}
