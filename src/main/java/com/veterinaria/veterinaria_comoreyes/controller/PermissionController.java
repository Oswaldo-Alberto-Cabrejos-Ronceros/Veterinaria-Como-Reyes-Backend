package com.veterinaria.veterinaria_comoreyes.controller;
import com.veterinaria.veterinaria_comoreyes.dto.PermissionDTO;
import com.veterinaria.veterinaria_comoreyes.service.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final IPermissionService permissionService;

    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@RequestBody PermissionDTO permissionDTO) {
        PermissionDTO createdPermission = permissionService.createPermission(permissionDTO);
        return ResponseEntity.ok(createdPermission);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Long id) {
        PermissionDTO permission = permissionService.getPermissionById(id);
        return ResponseEntity.ok(permission);
    }

    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        List<PermissionDTO> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/active")
    public ResponseEntity<List<PermissionDTO>> getAllActivePermissions() {
        List<PermissionDTO> permissions = permissionService.getAllActivePermissions();
        return ResponseEntity.ok(permissions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermissionDTO> updatePermission(
            @PathVariable Long id,
            @RequestBody PermissionDTO permissionDTO) {
        PermissionDTO updatedPermission = permissionService.updatePermission(id, permissionDTO);
        return ResponseEntity.ok(updatedPermission);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Void> togglePermissionStatus(@PathVariable Long id) {
        permissionService.togglePermissionStatus(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{permissionId}/roles")
    public ResponseEntity<PermissionDTO> assignRolesToPermission(
            @PathVariable Long permissionId,
            @RequestBody List<Long> roleIds) {
        PermissionDTO updatedPermission = permissionService.assignRolesToPermission(permissionId, roleIds);
        return ResponseEntity.ok(updatedPermission);
    }

    @DeleteMapping("/{permissionId}/roles")
    public ResponseEntity<PermissionDTO> removeRolesFromPermission(
            @PathVariable Long permissionId,
            @RequestBody List<Long> roleIds) {
        PermissionDTO updatedPermission = permissionService.removeRolesFromPermission(permissionId, roleIds);
        return ResponseEntity.ok(updatedPermission);
    }
}