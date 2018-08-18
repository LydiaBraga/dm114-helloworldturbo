package br.inatel.lydia.helloworldturbo.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import br.inatel.lydia.helloworldturbo.R;
import br.inatel.lydia.helloworldturbo.adapters.OrderAdapter;
import br.inatel.lydia.helloworldturbo.models.Order;
import br.inatel.lydia.helloworldturbo.tasks.OrderEvents;
import br.inatel.lydia.helloworldturbo.tasks.OrderTasks;
import br.inatel.lydia.helloworldturbo.utils.CheckNetworkConnection;
import br.inatel.lydia.helloworldturbo.webservice.WebServiceResponse;

public class OrdersFragment extends Fragment implements OrderEvents {

    private static final String ORDERS_LIST = "ORDERS_LIST";

    private ListView listViewOrders;
    private List<Order> orders;
    private OrderAdapter orderAdapter;

    public OrdersFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_orders_list, container, false);
        getActivity().setTitle("Orders");
        listViewOrders = (ListView) rootView.findViewById(R.id.orders_list);
        setHasOptionsMenu(true);

        listViewOrders.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent,
                                        View view, int position, long id) {
                    startOrderDetail(id);
                }
            }
        );

        if (savedInstanceState != null && savedInstanceState.containsKey(ORDERS_LIST)) {
            this.orders = new Gson().fromJson(savedInstanceState.getString(ORDERS_LIST),
                    new TypeToken<List<Order>>() {}.getType());
            setListAdapter(this.orders);
        } else {
            if (CheckNetworkConnection.isNetworkConnected(getActivity())) {
                OrderTasks orderTasks = new OrderTasks(getActivity(), this);
                orderTasks.getOrders();
            }
        }

        return rootView;
    }

    private void getOrders() {
        listViewOrders.setAdapter(null);
        OrderTasks orderTasks = new OrderTasks(getActivity(), OrdersFragment.this);
        orderTasks.getOrders();
    }

    private void setListAdapter(List<Order> orders) {
        orderAdapter = new OrderAdapter(getActivity(), orders);
        listViewOrders.setAdapter(orderAdapter);
    }

    private void startOrderDetail(long orderId) {
        Class fragmentClass;
        Fragment fragment = null;
        fragmentClass = OrderDetailFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();

            if (orderId >= 0) {
                Bundle args = new Bundle();
                args.putLong("order_id", orderId);
                fragment.setArguments(args);
            }

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.container, fragment, OrderDetailFragment.class.getCanonicalName());
            transaction.addToBackStack(OrdersFragment.class.getCanonicalName());
            transaction.commit();
        } catch (Exception e) {
            try {
                Toast.makeText(getActivity(), "Erro ao tentar abrir detalhes do pedido",
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e1) {

            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.refresh_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refreshOrders:
                if (CheckNetworkConnection.isNetworkConnected(getActivity())) {
                    getOrders();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        String ordersJson = new Gson().toJson(orders);
        outState.putString(ORDERS_LIST, ordersJson);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void getOrdersFinished(List<Order> orders) {
        this.orders = orders;
        setListAdapter(orders);
    }

    @Override
    public void getOrdersFailed(WebServiceResponse webServiceResponse) {
        Toast.makeText(getActivity(),
                "Falha na consulta da lista de pedidos" +
                webServiceResponse.getResultMessage() + " - Código do erro: " +
                webServiceResponse.getResponseCode(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getOrderByIdFinished(Order order) {
    }

    @Override
    public void getOrderByIdFailed(WebServiceResponse webServiceResponse) {
    }
}
