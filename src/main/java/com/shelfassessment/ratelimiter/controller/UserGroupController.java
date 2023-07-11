package com.shelfassessment.ratelimiter.controller;

import com.shelfassessment.ratelimiter.model.Group;
import com.shelfassessment.ratelimiter.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/v1")
public class UserGroupController {
    @Autowired
    private UserGroupService userService;

    @GetMapping("/getGroups")
    public List<Group> getGroups(String userId) {
        return userService.getGroups(userId);
    }

    @PostMapping("/createGroup")
    public String createUserGroup(String userId) {
        return String.format("Group created for user %s", userId);
    }

    @PutMapping("/update")
    public String updateGroup(String userId) {
        return String.format("Group details updated for user %s", userId);
    }

    @DeleteMapping("/delete")
    public String deleteGroupDetails(String userId) {
        return String.format("Group details deleted for user %s", userId);
    }
}
