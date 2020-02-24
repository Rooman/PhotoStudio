package com.photostudio.web.servlet.order;

import com.photostudio.ServiceLocator;
import com.photostudio.entity.order.FilterParameters;
import com.photostudio.entity.order.OrderStatus;
import com.photostudio.service.OrderService;
import com.photostudio.web.templater.TemplateEngineFactory;
import com.photostudio.web.util.CommonVariableAppendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AllOrdersServlet extends HttpServlet {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private OrderService defaultOrderService = ServiceLocator.getService(OrderService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("Request show all orders received");
        try {
            Map<String, Object> paramsMap = new HashMap<>();

            long userId;

            try {
                String user = request.getParameter("user");
                LOG.debug("user=" + user);
                userId = Long.parseLong(user);
            }
            catch (Exception e) {
                userId = 0;
                LOG.error("get parameter user error:" + e.getStackTrace());
            }


            new CommonVariableAppendService().appendUser(paramsMap, request);
            response.setContentType("text/html;charset=utf-8");

            if (userId==0) {
                LOG.info("Show all orders, Admin page");
                FilterParameters filterParameters = getFilterParameters(request);
                paramsMap.put("orders", defaultOrderService.getOrdersByParameters(filterParameters));

                TemplateEngineFactory.process("all-orders", paramsMap, response.getWriter());
            }
            else {
                LOG.info("Show all orders for user {}", userId);
                paramsMap.put("orders", defaultOrderService.getOrdersByUserId(userId));

                TemplateEngineFactory.process("user-all-orders", paramsMap, response.getWriter());

            }
        } catch (IOException e) {
            LOG.error("AllOrdersServlet error", e);
            throw new RuntimeException("AllOrdersServlet error", e);
        }

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