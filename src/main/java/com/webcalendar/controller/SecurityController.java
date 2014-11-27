package com.webcalendar.controller;

import com.webcalendar.dao.EventDAO;
import com.webcalendar.domain.Event;
import com.webcalendar.domain.User;
import com.webcalendar.domain.UserRole;
import com.webcalendar.service.CalendarService;
import com.webcalendar.service.EmailService;
import com.webcalendar.service.UserService;
import com.webcalendar.util.DefaultEvents;
import com.webcalendar.util.EncodeHelper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/**
 * Provides handling requests from client for login, logout, registration
 * and other features for security.
 *
 *
 * @author Ruslan Borisov
 */
@Controller
public class SecurityController {

    public final static String PATH = "http://185.25.116.77:8080/webcalendar/";
    private final static String VK_APP_ID = "4599932";
    private final static String VK_APP_SECRET = "u0RsNI88GBLNq8p8nr1y";
    private final static String VK_URL_AUTH = "http://oauth.vk.com/authorize";
    private final static String VK_REDIRECT_URI = PATH + "vkresponse";
    private final static String VK_URL_ACCESS_TOKEN = "https://oauth.vk.com/access_token";
    private final static String VK_URL_VK_API = "https://api.vk.com/method/";
    private final static String VK_API_VERSION = "5.25";

    private final static String FB_APP_ID = "754286764636704";
    private final static String FB_APP_SECRET = "7a4edde36c1be7c0ab00159823652907";
    private final static String FB_URL_AUTH = "https://www.facebook.com/dialog/oauth";
    private final static String FB_REDIRECT_URI = PATH + "fbresponse";
    private final static String FB_URL_ACCESS_TOKEN = "https://graph.facebook.com/oauth/access_token";
    private final static String FB_URL_API = "https://graph.facebook.com/me";

    private CalendarService calendarService;
    private EmailService emailService;
    private UserService userService;
    private EncodeHelper encodeHelper;
    private EventDAO eventDAO;

    @Inject
    public SecurityController(CalendarService calendarService, EmailService emailService,
                              UserService userService, EncodeHelper encodeHelper, EventDAO eventDAO) {
        this.calendarService = calendarService;
        this.emailService = emailService;
        this.eventDAO = eventDAO;
        this.userService = userService;
        this.encodeHelper = encodeHelper;
    }
    public SecurityController(){
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public String loginPageReDirect(){
        return "redirect:home";
    }

    /**
     * Invoke after successful authorization user.
     * Fills data store events from the database of the current user
     *
     * @return {@link #home}
     */
    @RequestMapping(value="/init", method = {RequestMethod.GET, RequestMethod.POST})
    public String init(){
        calendarService.clearDataStore();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Event> eventList = calendarService.searchAllUserEventsFromDB(user);

        calendarService.fillDataStore(eventList);
        return "redirect:home";
    }

    /**
     * Display general view of the application
     *
     * @return general view of the application
     */
    @RequestMapping(value="/home", method = RequestMethod.GET)
    public String home(ModelMap model) {

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("guest"))
            model.addAttribute("login", "true");

        return "index";
    }

    /**
     * Handling request from client for login. Ajax request.
     *
     * @param username : Username of the user
     * @param password : Password of the user
     * @return @ResponseBody string status of result login:
     * "success" if login successful or "exception" otherwise.
     */
    @RequestMapping(value="/userLogin", method = RequestMethod.POST)
    public @ResponseBody
    String userLogin(@RequestParam(value = "username") String username,
                     @RequestParam(value = "password") String password) {

        User user = (User) userService.loadUserByUsername(username);
        if (user!=null && encodeHelper.matches(password, user.getPassword())) {

            Authentication authentication =  new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return "success";

        } else
            return "exception";
    }

    /**
     * Handling request from client for login with Vkontakte.
     * Creates redirect request to remote Vkontakte server to obtain the code
     * which after that will be able to exchange for access token.
     *
     * @return String for redirect request
     */
    @RequestMapping(value = "/vkAuth" , method = {RequestMethod.GET, RequestMethod.POST})
    public String vkAuth() {

        StringBuilder sb = new StringBuilder(VK_URL_AUTH);
        sb.append("?client_id=").append(VK_APP_ID)
                .append("&scope=").append("notify,photos,status,email")
                .append("&redirect_uri=").append(VK_REDIRECT_URI)
                .append("&response_type=code")
                .append("&v=").append(VK_API_VERSION);

        return "redirect:" + sb.toString();
    }

    /**
     * Handling request from remote server Vkontakte with response for obtain code ({@link #vkAuth}).
     * Creates request for exchange code to access token.
     * Creates user and add it to database after obtain access token and
     * redirect to {@link #init} url.
     *
     * @return String fot redirect request
     */
    @RequestMapping(value = "/vkresponse", method = {RequestMethod.GET, RequestMethod.POST})
    public String vkresponse(HttpSession session,
                             @RequestParam(value = "code", required = false) String code,
                             @RequestParam(value = "error", required = false) String error) throws IOException, SQLException {
        if (error !=null)
            return "redirect:home";
        if (code != null)
            getTokenVK(code, session);

        String username = null;
        String email = (String) session.getAttribute("email");
        session.removeAttribute("email");
        if (session.getAttribute("access_token") != null) {
            StringBuilder sb = new StringBuilder(VK_URL_VK_API);
            sb.append("/users.get?user_id=").append(session.getAttribute("user_id"))
                    .append("&v=").append(VK_API_VERSION)
                    .append("&lang=en")
                    .append("&fields=photo_200")
                    .append("&access_token=").append(session.getAttribute("access_token"));

            String informationOfUser = requestGET(sb.toString());
            JSONObject jsonObject = new JSONObject(informationOfUser);
            JSONObject response = (JSONObject) (jsonObject.getJSONArray("response")).get(0);
            session.setAttribute("photo", response.getString("photo_200"));

            username = response.getString("first_name") + " (vk:" + session.getAttribute("user_id") + ")";
        }
        return springSecurityAuth(username, email);
    }

    /**
     * Handling request from client for login with Facebook.
     * Creates redirect request to remote Facebook server to obtain the code
     * which after that will be able to exchange for access token.
     *
     * @return String for redirect request
     */
    @RequestMapping(value = "/fbAuth" , method = {RequestMethod.GET, RequestMethod.POST})
    public String fbAuth() {

        StringBuilder sb = new StringBuilder(FB_URL_AUTH);
        sb.append("?client_id=").append(FB_APP_ID)
                .append("&redirect_uri=").append(FB_REDIRECT_URI)
                .append("&response_type=code")
                .append("&scope=email,user_birthday");

        return "redirect:" + sb.toString();
    }

    /**
     * Handling request from remote server Facebook with response for obtain code ({@link #fbAuth}).
     * Creates request for exchange code to access token.
     * Creates user and add it to database after obtain access token and
     * redirect to {@link #init} url.
     *
     * @return String fot redirect request
     */
    @RequestMapping(value = "/fbresponse", method = {RequestMethod.GET, RequestMethod.POST})
    public String fbresponse(HttpSession session,
                             @RequestParam(value = "code", required = false) String code,
                             @RequestParam(value = "error", required = false) String error) throws IOException, SQLException {
        if (error !=null)
            return "redirect:home";
        if (code != null)
            getTokenFB(code, session);

        String username = null;
        String email = null;
        if (session.getAttribute("access_token") != null) {
            StringBuilder sb = new StringBuilder(FB_URL_API);
            sb.append("?fields=id,first_name,last_name,email,picture.width(200).height(200)")
            .append("&access_token=").append(session.getAttribute("access_token"));

            String informationOfUser = requestGET(sb.toString());
            JSONObject jsonObject = new JSONObject(informationOfUser);

            email = jsonObject.getString("email");
            session.setAttribute("user_id", jsonObject.getLong("id"));
            session.setAttribute("photo", jsonObject.getJSONObject("picture").getJSONObject("data").getString("url"));

            username = jsonObject.getString("first_name") + " (fb:" + jsonObject.getLong("id") + ")";
        }
        return springSecurityAuth(username, email);
    }

    /**
     * Handling request from client for register new user. Ajax request
     * After successful registration send confirm link on the entered email
     *
     * @param username : Username of the user
     * @param password : Password of the user
     * @param email : Email of the user
     * @return @ResponseBody String status of result registration.
     * "success" if registration was successful, "exception" if registration was failed.
     * @throws SQLException if happen database access error or other errors
     */
    @RequestMapping(value="/registration", method = RequestMethod.POST)
    public @ResponseBody
    String regSubmit(@RequestParam("username") String username,
                     @RequestParam("password") String password,
                     @RequestParam("email") String email) throws SQLException {

        User userUsername = (User) userService.loadUserByUsername(username);
        User userEmail = (User) userService.loadUserByEmail(email);

        if (userUsername==null && userEmail==null) {
            UUID id = UUID.randomUUID();
            UserRole userRole =  new UserRole(1, UserRole.EnumRole.ROLE_USER);
            Set<UserRole> userRoles = new HashSet<UserRole>();
            userRoles.add(userRole);
            User user = new User(id, username, encodeHelper.encode(password), email, false, LocalDate.now(), userRoles);

            if(userService.addUser(user)) {
                String text = "To confirm your account click the link below: \n" + PATH +"confirm?uuid=" +
                        id.toString();
                emailService.sendEmail("Confirm", text, email);

            // Set default events
                List<Event> eventList = DefaultEvents.getDefaultEvents(user);
                eventList.forEach(eventDAO::addEvent);
            //
                return "success";

            } else
                return "error";
        } else
            return "exception";
    }

    /**
     * Handling request from client for change email. Ajax request
     * After successful change email send confirm link on the entered email
     *
     * @param email : New email address
     * @return @ResponseBody String status of result for change email.
     * "success" if change email was successful, "exception" if change email was failed.
     */
    @RequestMapping(value="/remindPass", method = RequestMethod.POST)
    public @ResponseBody
    String remindPass(@RequestParam("email") String email) {

        User user = (User) userService.loadUserByEmail(email);

        if (user!=null) {
            String newPass = RandomStringUtils.randomAlphanumeric(8);

            user.setPassword(encodeHelper.encode(newPass));
            userService.updateUser(user);

            StringBuilder sb = new StringBuilder();
            sb.append("Username: ").append(user.getUsername()).append("\n")
                    .append("Password: ").append(newPass);

            emailService.sendEmail("New Password", sb.toString(), email);

            return "success";

        } else
            return "exception";
    }

    /**
     * Handling request from client for confirm change email.
     *
     * @param uuid : Id of the user
     * @return {@link #init}
     */
    @RequestMapping(value="/confirm", method = RequestMethod.GET)
    public String confirm(@RequestParam(value = "uuid") String uuid) {

        User user = (User) userService.loadUserById(UUID.fromString(uuid));

        if (user!=null) {
           user.setActive(true);
            if(userService.updateUser(user)) {

                Authentication authentication =  new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                return "redirect:init";

            } else
                return "redirect:home";
        } else
            return "redirect:home";
    }

    private String springSecurityAuth(String username, String email) throws SQLException {

        User userUsername = (User) userService.loadUserByUsername(username);
        User userEmail = (User) userService.loadUserByEmail(email);
        User user;

        if (userUsername==null && userEmail==null) {
            UserRole userRole = new UserRole(1, UserRole.EnumRole.ROLE_USER);
            Set<UserRole> userRoles = new HashSet<>();
            userRoles.add(userRole);

            user = new User(UUID.randomUUID(), username,  encodeHelper.encode(RandomStringUtils.randomAlphanumeric(8)),
                    email, true, LocalDate.now(), userRoles);
            userService.addUser(user);

        // Set default events
            List<Event> eventList = DefaultEvents.getDefaultEvents(user);
            eventList.forEach(eventDAO::addEvent);
         //

        } else {

            if(userUsername != null && userEmail != null || userUsername == null)
                user = userEmail;
            else
                user = userUsername;
        }

        Authentication authentication =  new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:init";
    }
    private void getTokenVK(String code, HttpSession session) throws IOException {

        StringBuilder sb = new StringBuilder(VK_URL_ACCESS_TOKEN);
        sb.append("?client_id=").append(VK_APP_ID)
                .append("&client_secret=").append(VK_APP_SECRET)
                .append("&code=").append(code)
                .append("&redirect_uri=").append(VK_REDIRECT_URI);

        String result = requestGET(sb.toString());
        JSONObject jsonObject = new JSONObject(result);

        session.setAttribute("access_token", jsonObject.getString("access_token"));
        session.setAttribute("user_id", jsonObject.getInt("user_id"));
        session.setAttribute("email", jsonObject.getString("email"));
    }
    private void getTokenFB(String code, HttpSession session) throws IOException {

        StringBuilder sb = new StringBuilder(FB_URL_ACCESS_TOKEN);
        sb.append("?client_id=").append(FB_APP_ID)
                .append("&client_secret=").append(FB_APP_SECRET)
                .append("&code=").append(code)
                .append("&redirect_uri=").append(FB_REDIRECT_URI);

        String result = requestGET(sb.toString());

        String[] resultTemp = result.split("=");
        String[] resultTemp2 = resultTemp[1].split("&");

        session.setAttribute("access_token", resultTemp2[0]);
    }
    private String requestGET(String url) throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
}
