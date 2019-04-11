package com.example.alejandrotorresruiz.taller.AdapterReciclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alejandrotorresruiz.taller.Entities.Vehiculos;
import com.example.alejandrotorresruiz.taller.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by alejandrotorresruiz on 13/01/2019.
 */

public class MyAdapterVehiculos extends RecyclerView.Adapter<MyAdapterVehiculos.ViewHolder> {

    private List<Vehiculos> vehiculos;
    private int layout;
    private Context context;
    private MyAdapterVehiculos.OnItemClickListener itemClickListener;

    public MyAdapterVehiculos(Context context,List<Vehiculos> vehiculos, int layout, MyAdapterVehiculos.OnItemClickListener itemClickListener){
        this.context = context;
        this.vehiculos = vehiculos;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MyAdapterVehiculos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        MyAdapterVehiculos.ViewHolder vh = new MyAdapterVehiculos.ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(MyAdapterVehiculos.ViewHolder holder, int position) {
        holder.bind(vehiculos.get(position),itemClickListener,context);
    }

    @Override
    public int getItemCount() {
        return vehiculos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNombreVehiculo,txtMatriculaCoche;
        private ImageView imagenVehiculo;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtNombreVehiculo = (TextView) itemView.findViewById(R.id.txtNombreVehiculo);
            this.txtMatriculaCoche = (TextView) itemView.findViewById(R.id.txtMatriculaCoche);

            this.imagenVehiculo = (ImageView) itemView.findViewById(R.id.imagenVehiculo);
        }

        public void bind(final Vehiculos vehiculos, final MyAdapterVehiculos.OnItemClickListener listener, Context context){
            this.txtNombreVehiculo.setText(vehiculos.getMarca());
            this.txtMatriculaCoche.setText(vehiculos.getMatricula());
            Picasso.with(context).load("http://andresterol.int.elcampico.org:8080/taller/"+vehiculos.getFoto()).resize(250,250).centerCrop().error(R.mipmap.ic_launcher).into(imagenVehiculo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(vehiculos,getAdapterPosition());
                }
            });
        }
    }


    public interface OnItemClickListener{
        void onItemClick(Vehiculos name, int position);
    }
}