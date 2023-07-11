package com.shelfassessment.ratelimiter.service;


import com.shelfassessment.ratelimiter.model.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserGroupService {

    private static final Logger log = LoggerFactory.getLogger(UserGroupService.class);

    public List<Group> getGroups(String userId) {
        log.info(String.format("Get groups: %s", userId));
        return new ArrayList<>();
    }

}
