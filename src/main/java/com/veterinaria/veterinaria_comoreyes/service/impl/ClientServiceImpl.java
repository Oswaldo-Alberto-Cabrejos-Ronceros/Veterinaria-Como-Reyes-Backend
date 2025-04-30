package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.ClientDTO;
import com.veterinaria.veterinaria_comoreyes.dto.ClientListDTO;
import com.veterinaria.veterinaria_comoreyes.dto.UserDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria_comoreyes.mapper.ClientMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.repository.ClientRepository;
import com.veterinaria.veterinaria_comoreyes.service.IClientService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import com.veterinaria.veterinaria_comoreyes.util.HeadquarterUtil;
import com.veterinaria.veterinaria_comoreyes.util.PhoneUtil;
import com.veterinaria.veterinaria_comoreyes.util.ReniecUtil;
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


    @Override
    public Page<ClientListDTO> searchClients(String dni, String name, String lastName, Byte status, Long headquarterId, Pageable pageable) {
        return clientRepository.searchClients(dni, name, lastName, status, headquarterId, pageable);
    }

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, HeadquarterUtil headquarterUtil, IUserService userService, PhoneUtil phoneUtil, ReniecUtil reniecUtil) {
        this.clientRepository = clientRepository;
        this.userService = userService;
        this.phoneUtil = phoneUtil;
        this.headquarterUtil = headquarterUtil;
        this.reniecUtil = reniecUtil;
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
            userDTO.setStatus((byte) 1);

            // Crear el usuario
            UserDTO savedUserDTO = userService.createUser(userDTO);
            User savedUser = UserMapper.maptoUser(savedUserDTO);  // Tienes que mapearlo tú
            clientDTO.setUser(savedUser);

        }

        System.out.println("🛠️ TODO ESTA VALIDADO");
        System.out.println(clientDTO.getDni());
        // Mapear DTO a entidad Client
        Client client = ClientMapper.mapToClient(clientDTO);
        System.out.println(clientDTO.getDni());
        System.out.println("DNI antes de guardar: " + client.getDni() + " (longitud: " + client.getDni().length() + ")");
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
        Client existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + clientId));

        // 2. Validar si el nuevo teléfono ya existe en otro cliente
            phoneUtil.validatePhoneAvailable(clientDTO.getPhone(), "cliente");


        // 3. Validar si se desea actualizar el DNI
            if (clientRepository.existsByDni(clientDTO.getDni())) {
                throw new RuntimeException("Otro cliente ya registrado con el mismo DNI: " + clientDTO.getDni());
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
        existingClient.setStatus(clientDTO.getStatus());

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
        client.setStatus((byte) 0);
        clientRepository.save(client);
    }

    //Eliminar definitivamente al cliente y sus relaciones con otras tablas
    @Transactional
    @Override
    public void deleteClientById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(()-> new RuntimeException("Client not found with id: " + id));
        client.setStatus((byte) 0);
        clientRepository.save(client);
    }


}
