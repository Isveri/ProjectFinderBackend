package com.example.project.controllers;

import com.example.project.model.GroupRoomUpdateDTO;
import com.example.project.model.JoinCodeDTO;
import com.example.project.model.GroupRoomDTO;
import com.example.project.model.SearchCriteria;
import com.example.project.services.GroupRoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupRoomController {
    private final GroupRoomService groupRoomService;

    public GroupRoomController(GroupRoomService groupRoomService) {
        this.groupRoomService = groupRoomService;
    }

    @GetMapping
    public ResponseEntity<GroupRoomDTO> getGroupByName(@RequestParam String name) {
        return ResponseEntity.ok(groupRoomService.getGroupByName(name));
    }

    @GetMapping("/all")
    public ResponseEntity<List<GroupRoomDTO>> getAllGroups() {
        return ResponseEntity.ok(groupRoomService.getAllGroups());
    }

    @PostMapping("/all/filter")
    public ResponseEntity<Page<GroupRoomDTO>> getGroupsByCriteria(@RequestBody SearchCriteria criteria,Pageable pageable){
        return ResponseEntity.ok(groupRoomService.getGroupsByCriteria(criteria,pageable));
    }
    @GetMapping("/all/{game}")
    public ResponseEntity<Page<GroupRoomDTO>> getGroupsByGame(@PathVariable String game,Pageable pageable){
        return ResponseEntity.ok(groupRoomService.getGroupsByGame(game,pageable));
    }

    @PostMapping("/add")
    public ResponseEntity<?> createGroupRoom(@RequestBody GroupRoomDTO groupRoomDTO){
        return ResponseEntity.ok(groupRoomService.save(groupRoomDTO));
    }

    @GetMapping("/deleted")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<GroupRoomDTO>> getDeletedGroups(){
        return ResponseEntity.ok(groupRoomService.getDeletedGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupRoomDTO> getGroupRoomById(@PathVariable Long id){
        return ResponseEntity.ok(groupRoomService.getGroupById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroupRoom(@PathVariable Long id, @RequestBody GroupRoomUpdateDTO groupRoomUpdateDTO){
        return ResponseEntity.ok(groupRoomService.updateGroupRoomByDTO(id,groupRoomUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroupRoomById(@PathVariable Long id){
        groupRoomService.deleteGroupRoomById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/changeVisibility/{groupId}/{value}")
    public ResponseEntity<Void> changeVisibility(@PathVariable Long groupId, @PathVariable boolean value){
        groupRoomService.updateVisibility(groupId,value);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/generateCode/{groupId}")
    public ResponseEntity<JoinCodeDTO> generateJoinCode(@PathVariable Long groupId){
        return ResponseEntity.ok(groupRoomService.generateJoinCode(groupId));
    }

    @PatchMapping("/joinByCode/{code}")
    public ResponseEntity<GroupRoomDTO> joinGroupByCode(@PathVariable String code){
        return ResponseEntity.ok(groupRoomService.joinGroupByCode(code));
    }

    @PatchMapping("/makeLeader/{groupId}/{userId}")
    public ResponseEntity<GroupRoomDTO> makeGroupRoomLeader(@PathVariable Long groupId, @PathVariable Long userId){
        GroupRoomDTO groupRoomDTO = groupRoomService.makePartyLeader(groupId,userId);
        return ResponseEntity.ok(groupRoomDTO);
    }

    @PatchMapping("/removeUser/{groupId}/{userId}")
    public ResponseEntity<GroupRoomDTO> removeUserFromGroup(@PathVariable Long groupId, @PathVariable Long userId){
        GroupRoomDTO groupRoomDTO = groupRoomService.removeUserFromGroup(groupId,userId);
        return ResponseEntity.ok(groupRoomDTO);
    }
}
