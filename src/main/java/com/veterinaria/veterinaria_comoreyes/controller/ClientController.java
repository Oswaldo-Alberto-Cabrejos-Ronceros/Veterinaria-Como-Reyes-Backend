package com.veterinaria.veterinaria_comoreyes.controller;

import com.veterinaria.veterinaria_comoreyes.dto.Client.*;
import com.veterinaria.veterinaria_comoreyes.service.IClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
public class ClientController {
    private final IClientService clientService;

    @Autowired
    public ClientController(IClientService clientService) {
        this.clientService = clientService;
    }

    //Obtener info client -> lista de permisos
     @PreAuthorize("hasAuthority('drop_client')")
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById( @PathVariable Long id) {
        ClientDTO client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/search")
    public Page<ClientListDTO> searchClients(
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Long headquarterId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return clientService.searchClients(dni, name, lastName, status, headquarterId, pageable);
    }

    // crear client
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        ClientDTO createdClient = clientService.createClient(clientDTO);
        return ResponseEntity.ok(createdClient);
    }

    // Obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    // Update client
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('update_client')")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        ClientDTO updatedClient = clientService.updateClient(id, clientDTO);
        return ResponseEntity.ok(updatedClient);
    }

    // Block Client and Note of why
    @PatchMapping("/{id}/block")
    public ResponseEntity<Void> blockClient(@PathVariable Long id, String note) {
        clientService.updateBlockNote(id, note);
        clientService.blockClientById(id);
        return ResponseEntity.noContent().build();
    }

    // Delete Client  (baja definitiva)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClientById(id);
        return ResponseEntity.noContent().build();
    }

    //update my information as a client
    @PutMapping("/{id}/update-my-info")
    public ResponseEntity<String> updateClient( @PathVariable Long id,
                                                @RequestBody DataUpdateAsClientDTO dto,
                                                @CookieValue("accessToken") String token) {
        clientService.updateInfoAsClient(token, id, dto);
        return ResponseEntity.ok("Actualizado correctamente");
    }

    //update BlockNote in case of block
    @PatchMapping("/{id}/blockNote")
    public ResponseEntity<String> updateBlockNote(@PathVariable Long id, @RequestBody String lockNote) {
        clientService.updateBlockNote(id, lockNote);
        return ResponseEntity.ok("Comentario actualizada correctamente");
    }

    /******************************************
     * Controllers user-client
     * ****************************************/

    // client information for the profile
    @GetMapping("/{id}/my-info")
    public ResponseEntity<nMyInfoClientDTO> myInfoAsClient(@PathVariable Long id) {
        nMyInfoClientDTO myInfoClientDTO = clientService.getMyInfoAsClient(id);
        return ResponseEntity.ok(myInfoClientDTO);
    }


}