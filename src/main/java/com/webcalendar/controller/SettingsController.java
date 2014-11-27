package com.webcalendar.controller;

import com.webcalendar.domain.Event;
import com.webcalendar.domain.EventAdapter;
import com.webcalendar.domain.User;
import com.webcalendar.service.CalendarService;
import com.webcalendar.service.EmailService;
import com.webcalendar.service.UserService;
import com.webcalendar.util.EncodeHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Provides handling requests from client for settings
 *
 *
 * @author Ruslan Borisov
 */
@Controller
public class SettingsController {

    private CalendarService calendarService;
    private UserService userService;
    private EmailService emailService;
    private EncodeHelper encodeHelper;

    @Inject
    public SettingsController(CalendarService calendarService, UserService userService,
                              EmailService emailService, EncodeHelper encodeHelper) {
        this.calendarService = calendarService;
        this.userService = userService;
        this.emailService = emailService;
        this.encodeHelper = encodeHelper;
    }
    public SettingsController(){
    }

    /**
     * Handling request from client for display settings page.
     * Ajax request.
     *
     * @return view of settings page
     */
    @RequestMapping(value = "/settingsPage", method = RequestMethod.POST)
    public String settingPage() {
        return "views/settings";
    }

    /**
     * Handling request from client for display aside section of settings page.
     * Ajax request.
     *
     * @return view aside section of settings page
     */
    @RequestMapping(value = "/settingsAside", method = RequestMethod.POST)
    public String settingsAside(ModelMap model, HttpSession session) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("username",  user.getUsername());
        model.addAttribute("amountEvent",  calendarService.searchAllEventsFromDataStore().size());
        model.addAttribute("since",  user.getRegisterDate());
        model.addAttribute("email",  user.getEmail());
        String photo = (String) session.getAttribute("photo");

        if (photo!=null)
            model.addAttribute("photo", photo);
        else
            model.addAttribute("photo", SecurityController.PATH + "resources/images/user.png");

        return "views/settingsAside";
    }

    /**
     * Handling request from client for delete account.
     * Ajax request.
     *
     * @param checkPass : Password of the user
     * @return @ResponseBody String status of the result of delete account.
     * "success" if event successful deleted or "exception" otherwise.
     */
    @RequestMapping(value = "/deleteAcc", method = RequestMethod.POST)
    public @ResponseBody
    String deleteAcc(@RequestParam(value = "checkPass") String checkPass) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getPassword().equals(encodeHelper.encode(checkPass))) {
            Collection<Event> eventList = calendarService.searchAllEventsFromDataStore();

            List<EventAdapter> eventAdapters = eventList
                    .stream()
                    .map(EventAdapter::new)
                    .collect(Collectors.toList());

            user.setEvents(eventAdapters);
            calendarService.clearDataStore();
            userService.deleteUser(user);
            return "success";

        } else
            return "exception";
    }

    /**
     * Handling request from client for change email address.
     * Ajax request.
     *
     * @param newEmail : New email address of the user.
     * @return String "success" if email changed or "exception" otherwise.
     */
    @RequestMapping(value = "/changeEmail", method = RequestMethod.POST)
    public @ResponseBody
    String changeEmail(@RequestParam(value = "newEmail") String newEmail) {

        User user = (User) userService.loadUserByEmail(newEmail);
        if (user==null) {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            StringBuilder sb = new StringBuilder("To confirm your email click the link below: \n" +
                    SecurityController.PATH + "confirmEmail?uuid=");
            sb.append(currentUser.getId().toString()).append("&email=").append(newEmail);
            emailService.sendEmail("Confirm", sb.toString(), newEmail);
            return "success";

        } else
            return "exception";
    }

    /**
     * Handling request from client for confirm change email address.
     *
     * @param uuid : Id of the user
     * @param email : New email address of the user
     * @return general view of the application
     */
    @RequestMapping(value = "/confirmEmail", method = RequestMethod.GET)
    public String confirmEmail(@RequestParam(value = "uuid") String uuid,
                        @RequestParam(value = "email") String email) {

        User user = (User) userService.loadUserById(UUID.fromString(uuid));

        if (user!=null) {
            user.setEmail(email);
            if(userService.updateUser(user)) {

                Authentication authentication =  new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return "redirect:init";

            } else
                return "redirect:home";
        } else
            return "redirect:home";
    }

    /**
     * Handling request from client for reload page after logout.
     *
     * @return general view of the application
     */
    @RequestMapping(value = "/deleteAccReload", method = RequestMethod.POST)
    public String deleteAccReload() {
        return "redirect:j_spring_security_logout";
    }
}
