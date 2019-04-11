package com.example.alejandrotorresruiz.taller.AdapterReciclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alejandrotorresruiz.taller.Entities.Citas;
import com.example.alejandrotorresruiz.taller.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by alejandrotorresruiz on 23/12/2018.
 */

public class MyAdapterCitas extends RecyclerView.Adapter<MyAdapterCitas.ViewHolder> {

    private List<Citas> citas;
    private int layout;
    private Context context;
    private OnItemClickListener itemClickListener;

    public MyAdapterCitas(Context context,List<Citas> citas, int layout, OnItemClickListener itemClickListener){
        this.context = context;
        this.citas = citas;
        this.layout = layout;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(citas.get(position),itemClickListener,context);
    }

    @Override
    public int getItemCount() {
        return citas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtNombreTaller,txtMatriculaCoche,txtFecha,txtHora;
        private ImageView imagenTaller;

        public ViewHolder(View itemView) {
            super(itemView);
            this.txtNombreTaller = (TextView) itemView.findViewById(R.id.txtNombreTaller);
            this.txtMatriculaCoche = (TextView) itemView.findViewById(R.id.txtMatriculaCoche);
            this.txtFecha = (TextView) itemView.findViewById(R.id.txtFecha);
            this.txtHora = (TextView) itemView.findViewById(R.id.txtHora);

            this.imagenTaller = (ImageView) itemView.findViewById(R.id.imagenTaller);
            //this.imagenTaller = (ImageView) itemView.findViewById(R.id.imagenTaller);
        }

        public void bind(final Citas citas, final OnItemClickListener listener,Context context){
            this.txtNombreTaller.setText(citas.getNombre());
            this.txtMatriculaCoche.setText(citas.getId_vehiculo());
            Picasso.with(context).load("http://andresterol.int.elcampico.org:8080/taller/"+citas.getFoto()).resize(250,250).centerCrop().error(R.mipmap.ic_launcher).into(imagenTaller);
            this.txtFecha.setText(citas.getFecha());
            this.txtHora.setText(citas.getHora());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(citas,getAdapterPosition());
                }
            });
        }
    }


    public interface OnItemClickListener{
        void onItemClick(Citas name, int position);
    }
}
