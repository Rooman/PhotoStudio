package com.photostudio.web.servlet.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;

import com.photostudio.service.UserLanguageService;
import com.photostudio.security.SecurityService;
import com.photostudio.security.entity.Session;
import com.photostudio.service.UserService;
import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(urlPatterns = {"/user", "/user/*"})
@Slf4j
public class UserServlet extends HttpServlet {
    private UserService userService = ServiceLocator.getService(UserService.class);
    private UserLanguageService userLanguageService = ServiceLocator.getService(UserLanguageService.class);
    private ObjectMapper mapper = ServiceLocator.getService(ObjectMapper.class);
    private SecurityService securityService = ServiceLocator.getService(SecurityService.class);


    private static boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isNotEmpty(request.getParameter("id"))) {
            log.info("Request of registration form received");
            long userId = Long.parseLong(request.getParameter("id"));

            Session session = (Session) request.getAttribute("session");
            User loggedInUser = session.getUser();
            long loggedInUserId = loggedInUser.getId();

            User user = userService.getUserById(userId);
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("user", user);
            paramsMap.put("isMe", userId == loggedInUserId);
            paramsMap.put("userLanguages", userLanguageService.getAllLanguages());

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            TemplateEngineFactory.process(request, response, "user-info", paramsMap);
        } else {
            Map<String, Object> paramsMap = new HashMap<>();
            CommonVariableAppendService.appendUser(paramsMap, request);
            response.setContentType("text/html;charset=utf-8");
            paramsMap.put("userLanguages", userLanguageService.getAllLanguages());
            TemplateEngineFactory.process(request, response, "add-user", paramsMap);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Request for registration user received");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String country = request.getParameter("country");
        String city = request.getParameter("city");
        String zip = request.getParameter("zipCode");
        String address = request.getParameter("address");
        String title = request.getParameter("title");
        String additionalInfo = request.getParameter("additionalInfo");
        int langId = Integer.parseInt(request.getParameter("langId"));

        User newUser = new User();

        newUser.setEmail(email);
        newUser.setPhoneNumber(phoneNumber);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setCountry(country);
        newUser.setCity(city);
        newUser.setZip(isNotEmpty(zip) ? Integer.parseInt(zip) : 0);
        newUser.setAddress(address);
        newUser.setTitle(title);
        newUser.setAdditionalInfo(additionalInfo);
        newUser.setLangId(langId);
        log.debug("Request for registration user: {} received", newUser);

        securityService.register(newUser);

        response.sendRedirect(request.getContextPath() + "/admin/users");
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        log.info("Request for delete user received");
        String id = request.getParameter("id");
        if (id == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            long userId = Long.parseLong(id);
            userService.delete(userId);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            log.error("UserServlet doDelete() error", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            throw new RuntimeException("Error trying to delete user", e);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("Request for edit user received");

        String id = request.getParameter("id");
        if (id == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        log.info("Edit user with id {}", id);

        User updatedUser = mapper.readValue(request.getReader(), User.class);

        log.debug("New User {}", updatedUser);

        try {
            userService.edit(updatedUser);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            log.error("Error trying to edit user", e);
            throw new RuntimeException("Error trying to edit user", e);
        }
    }
}
