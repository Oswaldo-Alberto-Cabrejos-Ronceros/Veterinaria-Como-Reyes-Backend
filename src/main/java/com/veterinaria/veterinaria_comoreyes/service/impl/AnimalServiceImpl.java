    package com.veterinaria.veterinaria_comoreyes.service.impl;

    import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalDTO;
    import com.veterinaria.veterinaria_comoreyes.dto.Animal.AnimalInfoForClientDTO;
    import com.veterinaria.veterinaria_comoreyes.entity.Animal;
    import com.veterinaria.veterinaria_comoreyes.entity.Breed;
    import com.veterinaria.veterinaria_comoreyes.entity.Client;
    import com.veterinaria.veterinaria_comoreyes.entity.Specie;
    import com.veterinaria.veterinaria_comoreyes.mapper.AnimalMapper;
    import com.veterinaria.veterinaria_comoreyes.repository.AnimalRepository;
    import com.veterinaria.veterinaria_comoreyes.repository.BreedRepository;
    import com.veterinaria.veterinaria_comoreyes.repository.ClientRepository;
    import com.veterinaria.veterinaria_comoreyes.service.IAnimalService;
    import com.veterinaria.veterinaria_comoreyes.service.IClientService;
    import com.veterinaria.veterinaria_comoreyes.util.FilterStatus;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.math.BigDecimal;
    import java.math.RoundingMode;
    import java.time.LocalDate;
    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    public class AnimalServiceImpl implements IAnimalService {

        private final AnimalRepository animalRepository;
        private final ClientRepository clientRepository;
        private final FilterStatus filterStatus;
        private final AnimalMapper animalMapper;
        private final IClientService clientService;
        private final BreedRepository breedRepository;

        @Autowired
        public AnimalServiceImpl(
                AnimalRepository animalRepository,
                ClientRepository clientRepository,
                FilterStatus filterStatus,
                AnimalMapper animalMapper, IClientService clientService,
                BreedRepository breedRepository
        ) {
            this.animalRepository = animalRepository;
            this.clientRepository = clientRepository;
            this.filterStatus = filterStatus;
            this.animalMapper = animalMapper;
            this.clientService = clientService;
            this.breedRepository = breedRepository;
        }

        @Transactional(readOnly = true)
        @Override
        public AnimalDTO getAnimalById(Long id) {
            filterStatus.activeFilterStatus(true);
            Animal animal = animalRepository.findByAnimalIdAndStatusIsTrue(id)
                    .orElseThrow(() -> new RuntimeException("Animal not found with id: " + id));
            return animalMapper.mapToAnimalDTO(animal);
        }

        @Transactional(readOnly = true)
        @Override
        public List<AnimalDTO> getAllAnimals() {
            filterStatus.activeFilterStatus(true);
            return animalRepository.findAll()
                    .stream()
                    .map(animalMapper::mapToAnimalDTO)
                    .collect(Collectors.toList());
        }

        @Transactional(readOnly = true)
        @Override
        public List<AnimalDTO> getAnimalsByClient(Long clientId) {
            filterStatus.activeFilterStatus(true);
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
            return animalRepository.findByClient(client)
                    .stream()
                    .map(animalMapper::mapToAnimalDTO)
                    .collect(Collectors.toList());
        }

        @Transactional
        @Override
        public AnimalDTO createAnimal(AnimalDTO animalDTO) {
            // validar cliente
            Long clientId = animalDTO.getClientId();
            clientRepository.findById(clientId)
                    .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
            Animal animal = animalMapper.mapToAnimal(animalDTO);

            // Si la imagen viene vacía o null, usamos la imagen de la especie asociada al breed
            if (animalDTO.getUrlImage() == null || animalDTO.getUrlImage().trim().isEmpty()) {
                Long breedId = animalDTO.getBreed().getBreedId();

                // Obtener el breed y la especie
                Breed breed = breedRepository.findById(breedId)
                        .orElseThrow(() -> new RuntimeException("Breed not found with id: " + breedId));

                Specie specie = breed.getSpecie();
                if (specie == null || specie.getImagePath() == null) {
                    throw new RuntimeException("Specie or its image not found for breed id: " + breedId);
                }

                animal.setUrlImage(specie.getImagePath());
            }
            animal.setStatus(true);
            return animalMapper.mapToAnimalDTO(animalRepository.save(animal));
        }

        @Transactional
        @Override
        public AnimalDTO updateAnimal(Long id, AnimalDTO animalDTO) {
            filterStatus.activeFilterStatus(true);
            Animal animal = animalRepository.findByAnimalIdAndStatusIsTrue(id)
                    .orElseThrow(() -> new RuntimeException("Animal not found with id: " + id));

            animal.setName(animalDTO.getName());
            animal.setBreed(animalDTO.getBreed());
            animal.setGender(animalDTO.getGender());
            animal.setAnimalComment(animalDTO.getAnimalComment());
            animal.setBirthDate(animalDTO.getBirthDate());
            animal.setWeight(animalDTO.getWeight());
            animal.setUrlImage(animalDTO.getUrlImage());

            return animalMapper.mapToAnimalDTO(animalRepository.save(animal));
        }

        @Transactional
        @Override
        public void deleteAnimal(Long id) {
            filterStatus.activeFilterStatus(true);
            Animal animal = animalRepository.findByAnimalIdAndStatusIsTrue(id)
                    .orElseThrow(() -> new RuntimeException("Animal not found with id: " + id));
            animal.setStatus(false); // inactivo
            animalRepository.save(animal);
        }
        @Override
        public void validateAnimalExistAndStatus(Long id) {
            boolean exist = animalRepository.existsByAnimalIdAndStatusIsTrue(id);
            if (!exist) {
                throw new RuntimeException("Animal muerto");
            }
        }

        @Override
        public void validateClientExistAndStatusForAnimalId(Long animalId) {
            Long clientId = animalRepository.clientIdForAnimalId(animalId);
            clientService.validateClientExistsAndStatus(clientId);
        }

        @Override
        public String findSpecieNameByAnimalId(Long id) {
            return animalRepository.findSpecieNameByAnimalId(id)
                    .orElseThrow(() -> new RuntimeException("No se obtuvo el nombre de especie relacionada con el animal"));
        }

        @Override
        public List<AnimalInfoForClientDTO> getAnimalsByClientId(Long clientId) {
            List<Object[]> rows = animalRepository.findAnimalInfoRawByClientIdForPanel(clientId);

            return rows.stream().map(row -> new AnimalInfoForClientDTO(
                    ((Number) row[0]).longValue(), // animal_id

                    row[1] instanceof LocalDate ? (LocalDate) row[1]
                            : row[1] instanceof java.sql.Date ? ((java.sql.Date) row[1]).toLocalDate()
                            : row[1] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) row[1]).toLocalDateTime().toLocalDate()
                            : LocalDate.parse(row[1].toString().substring(0, 10)),

                    row[2].toString(), // gender
                    row[3].toString(), // name
                    row[4].toString(), // url_image
                    row[5] != null ? new BigDecimal(row[5].toString()).setScale(2, RoundingMode.HALF_UP) : null, // weight
                    row[6] instanceof Number ? ((Number) row[6]).longValue() : Long.parseLong(row[6].toString()), // speciesId
                    row[7].toString(), // breed_name
                    row[8].toString(), // species_name
                    row[9] != null ? row[9].toString() : null // animal_comment
            )).toList();

        }



    }
