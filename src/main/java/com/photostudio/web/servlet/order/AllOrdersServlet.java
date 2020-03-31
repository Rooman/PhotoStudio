package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.entity.user.User;
import com.photostudio.service.OrderService;
import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.photostudio.entity.user.UserRole.ADMIN;

@WebServlet(urlPatterns = "/orders")
@Slf4j
public class AllOrdersServlet extends HttpServlet {
    private OrderService orderService = ServiceLocator.getService(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        log.info("Request show all orders received");
        Map<String, Object> paramsMap = new HashMap<>();

        CommonVariableAppendService.appendUser(paramsMap, request);
        response.setContentType("text/html;charset=utf-8");

        User user = (User) paramsMap.get("user");

        FilterParameters filterParameters;
        if (user.getUserRole() == ADMIN) {
            log.info("Show all orders, Admin page");
            filterParameters = getFilterParameters(request);
            paramsMap.put("orders", orderService.getOrdersByParameters(filterParameters));
        } else {
            long userId = user.getId();
            log.info("Show all orders for user {}", userId);
            paramsMap.put("orders", orderService.getOrdersWithOrderStatusNotNewByUserId(userId));
        }

        TemplateEngineFactory.process(request, response, "all-orders", paramsMap);
    }

    private FilterParameters getFilterParameters(HttpServletRequest request) {
        String filterPhoneNumber = request.getParameter("filterPhoneNumber");
        String filterOrderStatus = request.getParameter("filterOrderStatus");
        String filterFromDate = request.getParameter("filterFromDate");
        String filterToDate = request.getParameter("filterToDate");

        FilterParameters.FilterParametersBuilder filterParametersBuilder = FilterParameters.builder()
                .email(request.getParameter("filterEmail"));

        if (filterPhoneNumber != null) {
            filterParametersBuilder.phoneNumber(filterPhoneNumber);
        }

        if (filterOrderStatus != null) {
            filterParametersBuilder.orderStatus(OrderStatus.getOrderStatus(filterOrderStatus));
        }

        if (filterFromDate != null) {
            filterParametersBuilder.fromDate(LocalDate.parse(filterFromDate).atStartOfDay());
        }

        if (filterToDate != null) {
            filterParametersBuilder.toDate(LocalDate.parse(filterToDate).atStartOfDay());
        }

        return filterParametersBuilder.build();
    }
}