package br.inatel.lydia.helloworldturbo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.inatel.lydia.helloworldturbo.R;
import br.inatel.lydia.helloworldturbo.adapters.PedidoAdapter;
import br.inatel.lydia.helloworldturbo.models.Order;
import br.inatel.lydia.helloworldturbo.models.Pedido;
import br.inatel.lydia.helloworldturbo.tasks.OrderEvents;
import br.inatel.lydia.helloworldturbo.tasks.OrderTasks;
import br.inatel.lydia.helloworldturbo.webservice.WebServiceResponse;

public class ListaPedidosFragment extends Fragment implements OrderEvents {

    private ListView listViewPedidos;
    private List<Pedido> pedidos;

    public ListaPedidosFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lista_pedidos, container, false);
        getActivity().setTitle("Pedidos");

        setHasOptionsMenu(true);

        pedidos = new ArrayList<Pedido>();

        for (int j = 0; j < 5; j++) {
            Pedido pedidoAux = new Pedido();
            pedidoAux.setOrderId(j);
            pedidoAux.setDataPedido("10/04/2016 11:50:00");

            pedidos.add(pedidoAux);
        }

        listViewPedidos = (ListView) rootView.findViewById(R.id.lista_pedidos);
        PedidoAdapter pedidoAdapter = new PedidoAdapter(getActivity(), pedidos);
        listViewPedidos.setAdapter(pedidoAdapter);

        OrderTasks orderTasks = new OrderTasks(getActivity(), this);
        orderTasks.getOrders();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pedidos_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.novo_pedido:
                Toast.makeText(getActivity(), "Novo pedido", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void getOrdersFinished(List<Order> orders) {
        Toast.makeText(getActivity(), "Número de pedidos: " + orders.size(), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void getOrdersFailed(WebServiceResponse webServiceResponse) {
        Toast.makeText(getActivity(), "Erro: " + webServiceResponse.getResponseMessage(), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void getOrderByIdFinished(Order order) {

    }

    @Override
    public void getOrderByIdFailed(WebServiceResponse webServiceResponse) {

    }
}
