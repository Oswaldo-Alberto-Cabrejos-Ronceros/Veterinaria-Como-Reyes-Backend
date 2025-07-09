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
    /*@PreAuthorize("hasAuthority('drop_client')")*/
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
    //@PreAuthorize("hasAuthority('update_client')")
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        ClientDTO updatedClient = clientService.updateClient(id, clientDTO);
        return ResponseEntity.ok(updatedClient);
    }

    // Block Client and Note of why
    @PatchMapping("/{id}/block")
    public ResponseEntity<Void> blockClient(@PathVariable Long id, @RequestParam String note) {
        clientService.updateBlockNote(id, note);
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


    @GetMapping("/by-dni")
    public ResponseEntity<ClientBasicInfoByDniDTO> getClientByDni(@RequestParam String dni) {
        ClientBasicInfoByDniDTO dto = clientService.getClientBasicInfoByDni(dni);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
    /****** Panel ADMIN ********/
    @GetMapping("/panel-admin")
    public List<ClientInfoPanelAdminDTO> getClientInfoPanelAdmin() {
        return clientService.getClientInfoPanelAdmin();
    }
    @GetMapping("/panel-admin/stats")
    public ResponseEntity<ClientStatsPanelDTO> getClientStats() {
        return ResponseEntity.ok(clientService.getClientStats());
    }

    /****** Panel MANAGER ********/
    @GetMapping("/panel-manager/{headquarterId}")
    public List<ClientInfoPanelAdminDTO> getClientInfoPanelByHeadquarterManager(@PathVariable Long headquarterId) {
        return clientService.getClientInfoPanelByHeadquarterManager(headquarterId);
    }
    @GetMapping("/panel-manager/stats/{headquarterId}")
    public ResponseEntity<ClientStatsPanelDTO> getClientStatsByHeadquarter(
            @PathVariable Long headquarterId) {
        return ResponseEntity.ok(clientService.getClientStatsByHeadquarter(headquarterId));
    }


    /****** Panel RECEPCIONIST ********/
    @GetMapping("/panel-receptionist/stats")
    public ResponseEntity<ClientStatsTodayDTO> getClientStatsToday() {
        return ResponseEntity.ok(clientService.getClientStatsToday());
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