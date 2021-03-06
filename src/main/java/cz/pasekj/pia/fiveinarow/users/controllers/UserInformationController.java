package cz.pasekj.pia.fiveinarow.users.controllers;

import cz.pasekj.pia.fiveinarow.support.ResourceNotFoundException;
import cz.pasekj.pia.fiveinarow.users.UserInfo;
import cz.pasekj.pia.fiveinarow.users.services.OnlineUsersService;
import cz.pasekj.pia.fiveinarow.users.services.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest API endpoint for querying information about the specified user - used in administration
 */
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserInformationController {

    /** UserInformation service */
    private final UserInfoService userInfoService;
    /** OnlineUsers service */
    private final OnlineUsersService onlineUsersService;

    /**
     * Get information about the specified user as UserInfo serialized to JSON
     * @param username username of the user to be queried
     * @return serialized UserInfo about the specified user
     */
    @GetMapping("/users/details/{username}")
    UserInfo getUserInfo(@PathVariable String username) {
        String email = userInfoService.getEmailOf(username);
        List<String> roles = userInfoService.getRolesOf(username);

        if(email == null) throw new ResourceNotFoundException("User does not exist");

        UserInfo userInfo = new UserInfo(username, onlineUsersService.isUserLoggedIn(username), false);
        userInfo.setRoles(roles);
        userInfo.email = email;

        return userInfo;
    }

}
