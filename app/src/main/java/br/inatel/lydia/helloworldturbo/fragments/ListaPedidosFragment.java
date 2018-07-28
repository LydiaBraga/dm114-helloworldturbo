package br.inatel.lydia.helloworldturbo.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.inatel.lydia.helloworldturbo.R;
import br.inatel.lydia.helloworldturbo.adapters.PedidoAdapter;
import br.inatel.lydia.helloworldturbo.models.Pedido;

public class ListaPedidosFragment extends Fragment {

    private ListView listViewPedidos;
    private List<Pedido> pedidos;

    public ListaPedidosFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lista_pedidos, container, false);
        getActivity().setTitle("Pedidos");

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

        return rootView;
    }
}
