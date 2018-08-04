package br.inatel.lydia.helloworldturbo.tasks;

import java.util.List;

import br.inatel.lydia.helloworldturbo.models.Order;
import br.inatel.lydia.helloworldturbo.webservice.WebServiceResponse;

public interface OrderEvents {

    void getOrdersFinished(List<Order> orders);

    void getOrdersFailed(WebServiceResponse webServiceResponse);

    void getOrderByIdFinished(Order order);

    void getOrderByIdFailed(WebServiceResponse webServiceResponse);
}
