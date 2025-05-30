package com.veterinaria.veterinaria_comoreyes.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/headquarters")
public class HeadquarterController {
    /*

    @Autowired
    private IHeadquarterService headquarterService;

    @GetMapping
    public List<HeadquarterDTO> getAllHeadquarters() {
        return headquarterService.getAllHeadquarters();
    }

    @GetMapping("/{id}")
    public HeadquarterDTO getHeadquarterById(@PathVariable Long id) {
        return headquarterService.getHeadquarterById(id);
    }

    @PostMapping
    public HeadquarterDTO createHeadquarter(@Valid @RequestBody HeadquarterDTO dto) {
        return headquarterService.createHeadquarter(dto);
    }

    @PutMapping("/{id}")
    public HeadquarterDTO updateHeadquarter(@PathVariable Long id, @Valid @RequestBody HeadquarterDTO dto) {
        return headquarterService.updateHeadquarter(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteHeadquarter(@PathVariable Long id) {
        headquarterService.deleteHeadquarter(id);
    }*/
}
