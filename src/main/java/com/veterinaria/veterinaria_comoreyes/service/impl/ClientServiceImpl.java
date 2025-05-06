package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.*;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria_comoreyes.mapper.ClientMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.repository.ClientRepository;
import com.veterinaria.veterinaria_comoreyes.repository.HeadquarterRepository;
import com.veterinaria.veterinaria_comoreyes.service.IClientService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import com.veterinaria.veterinaria_comoreyes.util.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;
    private final IUserService userService;
    private final PhoneUtil phoneUtil;
    private final HeadquarterUtil headquarterUtil;
    private final ReniecUtil reniecUtil;
    private final HeadquarterRepository headquarterRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, HeadquarterUtil headquarterUtil, IUserService userService, PhoneUtil phoneUtil, ReniecUtil reniecUtil, HeadquarterRepository headquarterRepository, JwtTokenUtil jwtTokenUtil) {
        this.clientRepository = clientRepository;
        this.userService = userService;
        this.phoneUtil = phoneUtil;
        this.headquarterUtil = headquarterUtil;
        this.reniecUtil = reniecUtil;
        this.headquarterRepository = headquarterRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public Page<ClientListDTO> searchClients(String dni, String name, String lastName, Boolean status, Long headquarterId, Pageable pageable) {
        return clientRepository.searchClients(dni, name, lastName, status, headquarterId, pageable);
    }

    @Override
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(()-> new RuntimeException("Client not found with id: " + id));
        return ClientMapper.mapToClientDTO(client);
    }

    @Override
    public ClientDTO getClientByUser(UserDTO userDTO) {
        Client client = clientRepository.findByUser(UserMapper.maptoUser(userDTO));
        return ClientMapper.mapToClientDTO(client);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream().map(ClientMapper::mapToClientDTO).collect(Collectors.toList());
    }
    @Transactional
    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {
        // Validar número de teléfono
        phoneUtil.validatePhoneAvailable(clientDTO.getPhone(), "cliente");

        // Validar que no haya otro cliente con ese dni
        if (clientRepository.existsByDni(clientDTO.getDni())) {
            throw new RuntimeException("Cliente ya registrado con ese DNI: " + clientDTO.getDni());
        }
        // Validar que no los datos ingresados coincidan con la reniec
        reniecUtil.validateData(
                clientDTO.getDni(),
                clientDTO.getName(),
                clientDTO.getLastName()
        );

        // Validar sede (Headquarter) que exista y esté activa
        headquarterUtil.validateHeadquarterAvailable(clientDTO.getHeadquarter().getHeadquarterId());

        //Crear un usuario relacionado con este cliente si hay datos
        if (clientDTO.getUser() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setType("C");
            userDTO.setEmail(clientDTO.getUser().getEmail());
            userDTO.setPassword(clientDTO.getUser().getPassword());


            // Crear el usuario
            UserDTO savedUserDTO = userService.createUser(userDTO);
            User savedUser = UserMapper.maptoUser(savedUserDTO);  // Tienes que mapearlo tú
            clientDTO.setUser(savedUser);

        }

        // Mapear DTO a entidad Client
        Client client = ClientMapper.mapToClient(clientDTO);

        client.setStatus(true);
        // Guardar el cliente
        Client savedClient = clientRepository.save(client);

        // Retornar el DTO del cliente guardado
        return ClientMapper.mapToClientDTO(savedClient);
    }

    //necesitas los datos en formato de un dto (el usuario no se cambia asi ingreses otros datos en el formato)
    @Transactional
    @Override
    public ClientDTO updateClient(Long clientId, ClientDTO clientDTO) {
        // 1. Buscar el cliente existente
        Client existingClient = clientRepository.findByClientId(clientId);

        // 2. Validar si el nuevo teléfono ya existe en otro cliente
            phoneUtil.validatePhoneAvailable(clientDTO.getPhone(), "cliente");


        // 3. Validar si se desea actualizar el DNI
        if (!existingClient.getDni().equals(clientDTO.getDni()) &&
                clientRepository.existsByDni(clientDTO.getDni())) {
            throw new RuntimeException("Otro cliente ya registrado con el mismo DNI: " + clientDTO.getDni());
        }else{
            // Validar que los datos ingresados coincidan con la reniec
            reniecUtil.validateData(
                    clientDTO.getDni(),
                    clientDTO.getName(),
                    clientDTO.getLastName()
            );
        }


        // 4. Si quiere cambiar de sede, validar que la nueva sede exista y esté activa
            headquarterUtil.validateHeadquarterAvailable(clientDTO.getHeadquarter().getHeadquarterId());
            // Cambiar solo la sede
            existingClient.getHeadquarter().setHeadquarterId(clientDTO.getHeadquarter().getHeadquarterId());


        // 5. Actualizar los campos permitidos
        existingClient.setDni(clientDTO.getDni());
        existingClient.setName(clientDTO.getName());
        existingClient.setLastName(clientDTO.getLastName());
        existingClient.setAddress(clientDTO.getAddress());
        existingClient.setPhone(clientDTO.getPhone());
        existingClient.setBirthDate(clientDTO.getBirthDate());
        existingClient.setDirImage(clientDTO.getDirImage());

        // ¡NO actualizamos nada de User!

        // 6. Guardar el cliente actualizado
        Client updatedClient = clientRepository.save(existingClient);

        // 7. Devolver el DTO actualizado
        return ClientMapper.mapToClientDTO(updatedClient);
    }


    //Eliminar logica cambia el estado a 0 = bloqueado
    @Transactional
    @Override
    public void blockClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(()-> new RuntimeException("Client not found with id: " + id));
        client.setStatus(false);
        clientRepository.save(client);
    }

    //Eliminar definitivamente al cliente y sus relaciones con otras tablas
    @Transactional
    @Override
    public void deleteClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(()-> new RuntimeException("Client not found with id: " + id));
        client.setStatus(false);
        clientRepository.save(client);
    }

    //ACTIONS DEL CLIENT
    @Override
    public MyInfoClientDTO myInfoAsClient(String token, Long id) {
        // Extraer el ID real del JWT
        Long clientIdFromToken = Long.valueOf(jwtTokenUtil.getEntityIdFromJwt(token));

        // Verificar que el ID enviado por el frontend coincida con el del token
        if (!clientIdFromToken.equals(id)) {
            throw new RuntimeException("No tienes permiso para acceder a esta información.");
        }

        // Buscamos el cliente por su ID
        Client client = clientRepository.findByClientId((id));

        // Construir el DTO solo con los datos necesarios
        MyInfoClientDTO dto = new MyInfoClientDTO();
        dto.setClientId(client.getClientId());
        dto.setUserId(client.getUser().getUserId());
        dto.setDni(client.getDni());
        dto.setNames(client.getName());
        dto.setLastNames(client.getLastName());
        dto.setPhone(client.getPhone());
        dto.setHeadquarterName(client.getHeadquarter().getName());
        // dto.setRole("cliente"); // ya viene por defecto

        return dto;
    }

    @Override
    public void updateInfoAsClient(String token, Long id, DataUpdateAsClientDTO data) {

        Long clientId = Long.valueOf(jwtTokenUtil.getEntityIdFromJwt(token));

        if (!clientId.equals(id)) {
            throw new RuntimeException("No tienes permiso para modificar esta información.");
        }

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        client.setAddress(data.getAddress());
        client.setPhone(data.getPhone());

        // Verificar y obtener la nueva sede
        headquarterUtil.validateHeadquarterAvailable(data.getHeadquarterId());
        Headquarter newHeadquarter = headquarterRepository.findByHeadquarterId((data.getHeadquarterId()));
        client.setHeadquarter(newHeadquarter);

        clientRepository.save(client);

    }

    // update the lock note field
    @Override
    public void updateBlockNote(Long clientId, String BlockNote) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + clientId));

        client.setBlockNote(BlockNote);
        clientRepository.save(client);
    }

}
