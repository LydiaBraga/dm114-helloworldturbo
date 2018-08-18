package br.inatel.lydia.helloworldturbo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.inatel.lydia.helloworldturbo.R;
import br.inatel.lydia.helloworldturbo.models.Order;
import br.inatel.lydia.helloworldturbo.models.OrderItem;
import br.inatel.lydia.helloworldturbo.tasks.OrderEvents;
import br.inatel.lydia.helloworldturbo.tasks.OrderTasks;
import br.inatel.lydia.helloworldturbo.utils.CheckNetworkConnection;
import br.inatel.lydia.helloworldturbo.webservice.WebServiceResponse;

public class OrderDetailFragment extends Fragment implements OrderEvents {

    private TextView orderId;
    private TextView email;
    private TextView freight;
    private ListView orderItensList;

    public OrderDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.order_details, container, false);
        getActivity().setTitle("Order Details");

        Bundle bundle = this.getArguments();
        if ((bundle != null) && (bundle.containsKey("order_id"))) {
            long orderIdParam = bundle.getLong("order_id");

            orderId = (TextView) rootView.findViewById(R.id.orderId);
            email = (TextView) rootView.findViewById(R.id.orderEmail);
            freight = (TextView) rootView.findViewById(R.id.orderFreightPrice);
            orderItensList = (ListView) rootView.findViewById(R.id.orderItensList);

            if (CheckNetworkConnection.isNetworkConnected(getActivity())) {
                OrderTasks orderTasks = new OrderTasks(getActivity(), this);
                orderTasks.getOrderById(orderIdParam);
            }
        }

        return rootView;
    }

    @Override
    public void getOrdersFinished(List<Order> orders) {

    }

    @Override
    public void getOrdersFailed(WebServiceResponse webServiceResponse) {

    }

    @Override
    public void getOrderByIdFinished(Order order) {
        orderId.setText(String.valueOf(order.getId()));
        email.setText(order.getEmail());
        freight.setText(String.valueOf(order.getFreightPrice()));

        orderItensList.setAdapter(new ArrayAdapter<OrderItem>(
                getActivity(), android.R.layout.simple_list_item_1,
                order.getOrderItems()));
    }

    @Override
    public void getOrderByIdFailed(WebServiceResponse webServiceResponse) {
        Toast.makeText(getActivity(),
                "Falha na consulta ao pedido" +
                        webServiceResponse.getResultMessage() + " - Código do erro: " +
                        webServiceResponse.getResponseCode(), Toast.LENGTH_SHORT).show();
    }
}
