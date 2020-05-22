package com.example.livros;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ListaLivroFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_livros, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();

        RecyclerView recyclerViewLivros = (RecyclerView) view.findViewById(R.id.recycleViewLivros);
        AdaptadorLivros adaptadorLivros= new AdaptadorLivros();
        recyclerViewLivros.setAdapter(adaptadorLivros);
        recyclerViewLivros.setLayoutManager(new LinearLayoutManager(context));
    }
    private void alterarLivro(){
    NavController navController = NavHostFragment.findNavController(ListaLivroFragment.this);
    navController.navigate(R.id.action_alterar_livro);
    }
    private void novoLivro(){
    NavController navController = NavHostFragment.findNavController(ListaLivroFragment.this);
    navController.navigate(R.id.action_novoLivro);
    }
}
