package com.example.project.controllers;

import com.example.project.model.JoinCodeDTO;
import com.example.project.model.MessageDTO;
import com.example.project.model.GroupRoomDTO;
import com.example.project.services.GroupRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<GroupRoomDTO> getUserByName(@RequestParam String name) {
        return ResponseEntity.ok(groupRoomService.getGroupByName(name));
    }

    @GetMapping("/all")
    public ResponseEntity<List<GroupRoomDTO>> getAllGroups() {
        return ResponseEntity.ok(groupRoomService.getAllGroups());
    }

    @GetMapping("/all/{game}")
    public ResponseEntity<List<GroupRoomDTO>> getGroupsByGame(@PathVariable String game){
        return ResponseEntity.ok(groupRoomService.getGroupsByGame(game));
    }

    @PostMapping("/add")
    public ResponseEntity<?> createGroupRoom(@RequestBody GroupRoomDTO groupRoomDTO){
        return ResponseEntity.ok(groupRoomService.save(groupRoomDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupRoomDTO> getGroupRoomById(@PathVariable Long id){
        return ResponseEntity.ok(groupRoomService.getGroupById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroupRoom(@PathVariable Long id, @RequestBody GroupRoomDTO groupRoomDTO){
        return ResponseEntity.ok(groupRoomService.updateGroupRoomByDTO(id,groupRoomDTO));
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

    @GetMapping("/G&C/{gameId}/{categoryId}")
    public ResponseEntity<List<GroupRoomDTO>> getGroupsByGameCategory(@PathVariable Long gameId, @PathVariable Long categoryId){
        return ResponseEntity.ok(groupRoomService.getGroupsByGameCategory(gameId,categoryId));
    }

    @GetMapping("/G&R/{gameId}/{inGameRoleId}")
    public ResponseEntity<List<GroupRoomDTO>> getGroupsByGameInGameRole(@PathVariable Long gameId, @PathVariable Long inGameRoleId){
        return ResponseEntity.ok(groupRoomService.getGroupsByGameRole(gameId,inGameRoleId));
    }

    @GetMapping("/G&C&R/{gameId}/{categoryId}/{roleId}")
    public ResponseEntity<List<GroupRoomDTO>> getGroupsByGameCategoryRole(@PathVariable Long gameId, @PathVariable Long categoryId, @PathVariable Long roleId){
        return ResponseEntity.ok(groupRoomService.getGroupsByGameCategoryRole(gameId,categoryId,roleId));
    }

    @GetMapping("/generateCode/{groupId}")
    public ResponseEntity<JoinCodeDTO> generateJoinCode(@PathVariable Long groupId){
        JoinCodeDTO joinCodeDTO = groupRoomService.generateJoinCode(groupId);
        if(joinCodeDTO==null){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }else{
        return ResponseEntity.ok(joinCodeDTO);}
    }

    @PatchMapping("/joinByCode/{code}")
    public ResponseEntity<GroupRoomDTO> joinGroupByCode(@PathVariable String code){
        GroupRoomDTO groupRoomDTO = groupRoomService.joinGroupByCode(code);
        if(groupRoomDTO == null){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }else{
        return ResponseEntity.ok(groupRoomService.joinGroupByCode(code));
    }
    }

    @PatchMapping("/makeLeader/{groupId}/{userId}")
    public ResponseEntity<GroupRoomDTO> makeGroupRoomLeader(@PathVariable Long groupId, @PathVariable Long userId){
        GroupRoomDTO groupRoomDTO = groupRoomService.makePartyLeader(groupId,userId);
        if (groupRoomDTO == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }else {
            return ResponseEntity.ok(groupRoomDTO);
        }
    }

    @PatchMapping("/removeUser/{groupId}/{userId}")
    public ResponseEntity<GroupRoomDTO> removeUserFromGroup(@PathVariable Long groupId, @PathVariable Long userId){
        GroupRoomDTO groupRoomDTO = groupRoomService.removeUserFromGroup(groupId,userId);
        if (groupRoomDTO == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }else {
            return ResponseEntity.ok(groupRoomDTO);
        }
    }
}
