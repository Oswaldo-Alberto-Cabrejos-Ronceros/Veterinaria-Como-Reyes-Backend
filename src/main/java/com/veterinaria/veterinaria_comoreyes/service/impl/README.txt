Contiene clases de utilidad y helpers generales, como:

Validaciones comunes
Funciones repetitivas
Conversores


@Service
public class EmployeeServiceImpl implements IEmployeeService {
    @Autowired
    private final EmployeeRepository employeeRepository;
    private final IUserService userService;
    private final PhoneUtil phoneUtil;
    private final HeadquarterUtil headquarterUtil;
    private final ReniecUtil reniecUtil;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, IUserService userService, PhoneUtil phoneUtil, HeadquarterUtil headquarterUtil, ReniecUtil reniecUtil) {
        this.employeeRepository = employeeRepository;
        this.userService = userService;
        this.phoneUtil = phoneUtil;
        this.headquarterUtil = headquarterUtil;
        this.reniecUtil = reniecUtil;
    }
    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        return null;
    }

    @Override
    public EmployeeDTO getEmployeeByUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return List.of();
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        phoneUtil.validatePhoneAvailable(employeeDTO.getPhone(), "empleado");

        if (employeeRepository.existsByDni(employeeDTO.getDni())) {
            throw new RuntimeException("Ya existe un empleado con ese DNI: " + employeeDTO.getDni());
        }

        reniecUtil.validateData(
                employeeDTO.getDni(),
                employeeDTO.getName(),
                employeeDTO.getLastName()
        );

        headquarterUtil.validateHeadquarterAvailable(employeeDTO.getHeadquarter().getHeadquarterId());

        if (employeeDTO.getUser() != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setType("E");
            userDTO.setEmail(employeeDTO.getUser().getEmail());
            userDTO.setPassword(employeeDTO.getUser().getPassword());
            userDTO.setStatus((byte) 1);

            UserDTO savedUserDTO = userService.createUser(userDTO);
            User savedUser = UserMapper.maptoUser(savedUserDTO);
            employeeDTO.setUser(savedUser);
        }

        Employee employee = EmployeeMapper.mapToEmployee(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDTO(savedEmployee);
    }

    @Override
    public EmployeeDTO updateEmployee(Long employeeId, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + employeeId));

        phoneUtil.validatePhoneAvailable(employeeDTO.getPhone(), "empleado");

        if (!existingEmployee.getDni().equals(employeeDTO.getDni()) &&
                employeeRepository.existsByDni(employeeDTO.getDni())) {
            throw new RuntimeException("Ya existe otro empleado con ese DNI: " + employeeDTO.getDni());
        }

        headquarterUtil.validateHeadquarterAvailable(employeeDTO.getHeadquarter().getHeadquarterId());
        existingEmployee.getHeadquarter().setHeadquarterId(employeeDTO.getHeadquarter().getHeadquarterId());

        existingEmployee.setDni(employeeDTO.getDni());
        existingEmployee.setCmvp(employeeDTO.getCmvp());
        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setLastName(employeeDTO.getLastName());
        existingEmployee.setAddress(employeeDTO.getAddress());
        existingEmployee.setPhone(employeeDTO.getPhone());
        existingEmployee.setBirthDate(employeeDTO.getBirthDate());
        existingEmployee.setDirImage(employeeDTO.getDirImage());
        existingEmployee.setStatus(employeeDTO.getStatus());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return EmployeeMapper.mapToEmployeeDTO(updatedEmployee);
    }
    }

    @Override
    public void deleteEmployee(Long id) {

    }

    @Override
    public EmployeeDTO getEmployeeByDni(String dni) {
        return null;
    }

    @Override
    public EmployeeDTO restoreEmployee(Long employeeId) {
        return null;
    }

    @Override
    public EmployeeDTO assignRolesToEmployee(Long employeeId, List<Long> roleIds) {
        return null;
    }

    @Override
    public EmployeeDTO removeRolesFromEmployee(Long employeeId, List<Long> roleIds) {
        return null;
    }

    @Override
    public EmployeeDTO addRoleToEmployee(Long employeeId, Long roleId) {
        return null;
    }

    @Override
    public Page<EmployeeListDTO> searchEmployees(String dni, String name, String lastName, Byte status, Long headquarterId, Pageable pageable) {
        return employeeRepository.searchEmployees(dni, name, lastName, status, headquarterId, pageable);
    }


}
