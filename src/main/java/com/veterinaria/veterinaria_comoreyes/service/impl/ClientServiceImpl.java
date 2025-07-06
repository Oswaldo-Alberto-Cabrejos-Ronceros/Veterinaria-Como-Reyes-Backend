package com.veterinaria.veterinaria_comoreyes.service.impl;

import com.veterinaria.veterinaria_comoreyes.dto.Client.*;
import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterBasicDTO;
import com.veterinaria.veterinaria_comoreyes.dto.Headquarter.HeadquarterDTO;
import com.veterinaria.veterinaria_comoreyes.dto.User.UserDTO;
import com.veterinaria.veterinaria_comoreyes.dto.User.UserEmailDTO;
import com.veterinaria.veterinaria_comoreyes.entity.Client;
import com.veterinaria.veterinaria_comoreyes.entity.Headquarter;
import com.veterinaria.veterinaria_comoreyes.entity.User;
import com.veterinaria.veterinaria_comoreyes.security.auth.exception.AuthException;
import com.veterinaria.veterinaria_comoreyes.security.auth.exception.ErrorCodes;
import com.veterinaria.veterinaria_comoreyes.exception.PhoneAlreadyExistsException;
import com.veterinaria.veterinaria_comoreyes.exception.ResourceNotFoundException;
import com.veterinaria.veterinaria_comoreyes.external.reniec.service.IReniecService;
import com.veterinaria.veterinaria_comoreyes.mapper.ClientMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.HeadquarterMapper;
import com.veterinaria.veterinaria_comoreyes.mapper.UserMapper;
import com.veterinaria.veterinaria_comoreyes.repository.ClientRepository;
import com.veterinaria.veterinaria_comoreyes.security.auth.util.JwtTokenUtil;
import com.veterinaria.veterinaria_comoreyes.service.IClientService;
import com.veterinaria.veterinaria_comoreyes.service.IHeadquarterService;
import com.veterinaria.veterinaria_comoreyes.service.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements IClientService {

    private final ClientRepository clientRepository;
    private final IUserService userService;
    private final IHeadquarterService headquarterService;
    private final IReniecService reniecService;
    private final JwtTokenUtil jwtTokenUtil;
    private final ClientMapper clientMapper;
    private final UserMapper userMapper;
    private final HeadquarterMapper headquarterMapper;

    @Autowired
    public ClientServiceImpl(
        ClientRepository clientRepository,
        IUserService userService,
        IReniecService reniecService,
        IHeadquarterService headquarterService,
        JwtTokenUtil jwtTokenUtil,
        HeadquarterMapper headquarterMapper,
        ClientMapper clientMapper,
        UserMapper userMapper
    ) {
        this.clientRepository = clientRepository;
        this.userService = userService;
        this.headquarterService = headquarterService;
        this.reniecService = reniecService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.clientMapper = clientMapper;
        this.headquarterMapper = headquarterMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Page<ClientListDTO> searchClients(String dni, String name, String lastName, Boolean status, Long headquarterId, Pageable pageable) {
        return clientRepository.searchClients(dni, name, lastName, status, headquarterId, pageable);
    }

    @Override
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        return clientMapper.mapToClientDTO(client);
    }

    @Override
    public ClientDTO getClientByUser(UserDTO userDTO) {
        Client client = clientRepository.findByUser(userMapper.maptoUser(userDTO));

        return clientMapper.mapToClientDTO(client);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll()
            .stream()
            .map(clientMapper::mapToClientDTO)
            .collect(Collectors.toList());
    }

    private void validatePhoneAvailable(String phone){
        boolean exist = clientRepository.existsByPhone(phone);
        if (exist) {
            throw new PhoneAlreadyExistsException("El número de teléfono ya está registrado en otro empleado");
        }
    }

    @Transactional
    @Override
    public ClientDTO createClient(ClientDTO clientDTO) {


        validatePhoneAvailable(clientDTO.getPhone());

        // Verificar si el DNI ya está registrado
        if (clientRepository.existsByDni(clientDTO.getDni())) {
            throw new RuntimeException("Cliente ya registrado con ese DNI: " + clientDTO.getDni());
        }

        reniecService.validateIdentityReniec(clientDTO.getDni(), clientDTO.getName(), clientDTO.getLastName());

        headquarterService.validateHeadquarterAvailable(clientDTO.getHeadquarter().getHeadquarterId());

        System.out.println("Client ID antes de guardar: " + clientDTO.getClientId());
        if (clientDTO.getUser() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setType("C");
            userDTO.setEmail(clientDTO.getUser().getEmail());
            userDTO.setPassword(clientDTO.getUser().getPassword());

            UserDTO savedUserDTO = userService.createUser(userDTO);
            User savedUser = userMapper.maptoUser(savedUserDTO);
            clientDTO.setUser(savedUser);
        }

        Client client = clientMapper.mapToClient(clientDTO);
        client.setStatus(true);
        LocalDate createDate = LocalDate.now();
        client.setCreateDate(createDate);
        return clientMapper.mapToClientDTO(clientRepository.save(client));
    }

    @Transactional
    @Override
    public ClientDTO updateClient(Long clientId, ClientDTO clientDTO) {
        Client existingClient = clientRepository.findByClientId(clientId);

        validatePhoneAvailable(clientDTO.getPhone());

        if (!existingClient.getDni().equals(clientDTO.getDni()) &&
                clientRepository.existsByDni(clientDTO.getDni())) {
            throw new RuntimeException("Otro cliente ya registrado con el mismo DNI: " + clientDTO.getDni());
        } else {
            reniecService.validateIdentityReniec(clientDTO.getDni(), clientDTO.getName(), clientDTO.getLastName());
        }

        headquarterService.validateHeadquarterAvailable(clientDTO.getHeadquarter().getHeadquarterId());
        existingClient.getHeadquarter().setHeadquarterId(clientDTO.getHeadquarter().getHeadquarterId());

        existingClient.setDni(clientDTO.getDni());
        existingClient.setName(clientDTO.getName());
        existingClient.setLastName(clientDTO.getLastName());
        existingClient.setAddress(clientDTO.getAddress());
        existingClient.setPhone(clientDTO.getPhone());
        existingClient.setBirthDate(clientDTO.getBirthDate());
        existingClient.setDirImage(clientDTO.getDirImage());

        return clientMapper.mapToClientDTO(clientRepository.save(existingClient));
    }

    @Transactional
    @Override
    public void blockClientById(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        client.setStatus(false);
        clientRepository.save(client);
    }

    @Transactional
    @Override
    public void deleteClientById(Long id) {
        Client client = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        client.setStatus(false); // eliminación lógica
        clientRepository.save(client);
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

        headquarterService.validateHeadquarterAvailable(data.getHeadquarterId());
        HeadquarterDTO hqDto = headquarterService.getHeadquarterById(data.getHeadquarterId());
        Headquarter newHeadquarter = headquarterMapper.mapToHeadquarter(hqDto);
        client.setHeadquarter(newHeadquarter);

        clientRepository.save(client);
    }

    @Override
    public void updateBlockNote(Long clientId, String blockNote) {
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + clientId));
        client.setBlockNote(blockNote);
        client.setStatus(false);
        userService.blockUser(client.getUser().getUserId());
        clientRepository.save(client);
    }

    //metodos para el auth
    @Override
    public Client getClientByUserForAuth(User user) {
        Client client = clientRepository.findByUser(user);
        if (client == null) {
            throw new AuthException("Cliente no encontrado", ErrorCodes.INVALID_CREDENTIALS.getCode());
        }
        return client;
    }
    @Override
    public void validateClientExistsAndStatus(Long clientId) {
        boolean exist = clientRepository.existsByClientIdAndStatusIsTrue(clientId);
        if (!exist) {
            throw new RuntimeException("Cliente Bloqueado");
        }
    }

        /***************************************************************
         * Metodos solo para el CLient
         ****************************************************************/
        @Override
        public nMyInfoClientDTO getMyInfoAsClient(Long id) {
            Client client = clientRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

            //creamos y intoducimos los datos necesarios en user (id y name)
            HeadquarterBasicDTO headquarterBasicDTO = new HeadquarterBasicDTO();
            headquarterBasicDTO.setId(client.getHeadquarter().getHeadquarterId());
            headquarterBasicDTO.setName(client.getHeadquarter().getName());

            //creamos el myInfo y le introducimos los valores
            nMyInfoClientDTO dto = new nMyInfoClientDTO();

            dto.setClientId(client.getClientId());
            dto.setHeadquarter(headquarterBasicDTO);
            dto.setDni(client.getDni());
            dto.setNames(client.getName());
            dto.setLastNames(client.getLastName());
            dto.setAddress(client.getAddress());
            dto.setPhone(client.getPhone());

            //verificamos que tenga usuario
            if (client.getUser() == null) {
                dto.setUser(null);
            } else {
                //creamos y intoducimos los datos necesarios en user (id y email)
                UserEmailDTO userEmailDTO = new UserEmailDTO();
                userEmailDTO.setId(client.getUser().getUserId());
                userEmailDTO.setEmail(client.getUser().getEmail());
                dto.setUser(userEmailDTO);
            }

            return dto;
        }

    @Override
    public ClientBasicInfoByDniDTO getClientBasicInfoByDni(String dni) {
        Object result = clientRepository.findBasicInfoByDni(dni);
        if (result == null) {
            return null;
        }
        Object[] row = (Object[]) result;
        ClientBasicInfoByDniDTO dto = new ClientBasicInfoByDniDTO();
        dto.setId(((Number) row[0]).longValue());
        dto.setFullName((String) row[1]);
        return dto;
    }

    @Override
    public List<ClientInfoPanelAdminDTO> getClientInfoPanelAdmin() {
        return clientRepository.findClientInfoPanelAdminRaw().stream()
                .map(obj -> new ClientInfoPanelAdminDTO(
                        ((Number) obj[0]).longValue(),
                        (String) obj[1],
                        (String) obj[2],
                        (String) obj[3]
                ))
                .collect(Collectors.toList());
    }
    @Override
    public List<ClientInfoPanelAdminDTO> getClientInfoPanelByHeadquarterManager(Long headquarterId) {
        return clientRepository.findClientInfoPanelByHeadquarterId(headquarterId).stream()
                .map(obj -> new ClientInfoPanelAdminDTO(
                        ((Number) obj[0]).longValue(),
                        (String) obj[1],
                        (String) obj[2],
                        (String) obj[3]
                ))
                .collect(Collectors.toList());
    }

    @Override
    public ClientStatsPanelDTO getClientStats() {
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        LocalDate previousMonthDate = today.minusMonths(1);
        int previousMonth = previousMonthDate.getMonthValue();
        int previousYear = previousMonthDate.getYear();

        List<Object[]> results = clientRepository.getClientStatsPanel(
                currentMonth,
                currentYear,
                previousMonth,
                previousYear
        );

        Object[] row = results.get(0); // Extraemos la única fila esperada

        Long totalClients = row[0] != null ? ((Number) row[0]).longValue() : 0L;
        Long currentMonthClients = row[1] != null ? ((Number) row[1]).longValue() : 0L;
        Long previousMonthClients = row[2] != null ? ((Number) row[2]).longValue() : 0L;

        long difference = currentMonthClients - previousMonthClients;
        String differenceWithSign = difference > 0 ? "+" + difference
                : difference < 0 ? String.valueOf(difference)
                : "0";

        return new ClientStatsPanelDTO(
                totalClients,
                currentMonthClients,
                previousMonthClients,
                differenceWithSign
        );
    }

    @Override
    public ClientStatsPanelDTO getClientStatsByHeadquarter(Long headquarterId) {
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        LocalDate previousMonthDate = today.minusMonths(1);
        int previousMonth = previousMonthDate.getMonthValue();
        int previousYear = previousMonthDate.getYear();

        List<Object[]> results = clientRepository.getClientStatsByHeadquarterPanel(
                currentMonth,
                currentYear,
                previousMonth,
                previousYear,
                headquarterId
        );

        Object[] row = results.get(0); // Solo esperamos una fila

        Long totalClients = row[0] != null ? ((Number) row[0]).longValue() : 0L;
        Long currentMonthClients = row[1] != null ? ((Number) row[1]).longValue() : 0L;
        Long previousMonthClients = row[2] != null ? ((Number) row[2]).longValue() : 0L;

        long difference = currentMonthClients - previousMonthClients;
        String differenceWithSign = difference > 0 ? "+" + difference
                : difference < 0 ? String.valueOf(difference)
                : "0";

        return new ClientStatsPanelDTO(
                totalClients,
                currentMonthClients,
                previousMonthClients,
                differenceWithSign
        );
    }





}
