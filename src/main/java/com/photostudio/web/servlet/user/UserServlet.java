package com.photostudio.web.servlet.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.photostudio.ServiceLocator;
import com.photostudio.entity.user.User;
import com.photostudio.service.UserService;
import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private UserService userService = ServiceLocator.getService(UserService.class);
    private CommonVariableAppendService commonVariableAppendService = new CommonVariableAppendService();
    private ObjectMapper mapper = ServiceLocator.getService(ObjectMapper.class);

    private static boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isNotEmpty(request.getParameter("id"))) {
            LOG.info("Request of registration form received");
            long userId = Long.parseLong(request.getParameter("id"));

            User user = userService.getUserById(userId);
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("user", user);

            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            TemplateEngineFactory.process("user-info", paramsMap, response.getWriter());
        } else {
            Map<String, Object> paramsMap = new HashMap<>();
            commonVariableAppendService.appendUser(paramsMap, request);
            response.setContentType("text/html;charset=utf-8");
            TemplateEngineFactory.process("add-user", paramsMap, response.getWriter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.info("Request for registration user received");
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
        LOG.debug("Request for registration user: {} received", newUser);

        //refactor! waiting for email notification
        newUser.setPasswordHash("96cae35ce8a9b0244178bf28e4966c2ce1b8385723a96a6b838858cdd6ca0a1e");
        newUser.setSalt("123");

        userService.add(newUser);
        response.sendRedirect(request.getContextPath() + "/admin/users");
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Request for delete user received");
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
            LOG.error("UserServlet doDelete() error", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            throw new RuntimeException("Error trying to delete user", e);
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOG.info("Request for edit user received");

        String id = request.getParameter("id");
        if (id == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        LOG.info("Edit user with id {}", id);

        User updatedUser = mapper.readValue(request.getReader(), User.class);

        LOG.debug("New User {}", updatedUser);

        try {
            userService.edit(updatedUser);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            LOG.error("Error trying to edit user", e);
            throw new RuntimeException("Error trying to edit user", e);
        }
    }
}
