package com.example.alejandrotorresruiz.taller.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alejandrotorresruiz.taller.R;
import com.squareup.picasso.Picasso;

import static com.example.alejandrotorresruiz.taller.R.layout.fragment_detalle_cita;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleCitaFragment extends Fragment {


    public DetalleCitaFragment() {
        // Required empty public constructor
    }

    private String nombreTaller,fotoTaller,fotoCoche,diaCita,horaCita;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(fragment_detalle_cita, container, false);

        TextView textNombreTaller = (TextView) view.findViewById(R.id.textNombreTaller);
        TextView textTitulo = (TextView) view.findViewById(R.id.textTitulo);
        TextView textDiaCita = (TextView) view.findViewById(R.id.textDiaCita);
        TextView textHoraCita = (TextView) view.findViewById(R.id.textHoraCita);

        ImageView imagenTaller = (ImageView) view.findViewById(R.id.imageTaller);
        ImageView imagenCoche = (ImageView) view.findViewById(R.id.imageCoche);

        nombreTaller = getArguments().getString("nombreTaller");
        fotoTaller = getArguments().getString("fotoTaller");
        fotoCoche = getArguments().getString("fotoCoche");
        diaCita = getArguments().getString("diaCita");
        horaCita = getArguments().getString("horaCita");

        textNombreTaller.setText(nombreTaller);
        textDiaCita.setText("Dia :"+diaCita);
        textHoraCita.setText("Hora :"+horaCita);
        Toast.makeText(view.getContext(), "Hola -"+fotoTaller, Toast.LENGTH_SHORT).show();

        Picasso.with(view.getContext()).load("http://andresterol.int.elcampico.org:8080/taller/"+fotoTaller).fit().centerCrop().into(imagenTaller);
        Picasso.with(view.getContext()).load("http://andresterol.int.elcampico.org:8080/taller/"+fotoCoche).into(imagenCoche);

        return view;
    }

}
