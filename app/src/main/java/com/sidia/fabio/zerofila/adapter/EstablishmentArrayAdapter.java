package com.sidia.fabio.zerofila.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sidia.fabio.zerofila.R;
import com.sidia.fabio.zerofila.model.Establishment;

import java.util.List;

public class EstablishmentArrayAdapter extends RecyclerView.Adapter<EstablishmentArrayAdapter.EstablishmentViewHolder> {

    private List<Establishment> mRecipeList;

    final private ListItemClickListener mOnClickListener;

    public Establishment getItem(int clickedItemIndex) {
        return mRecipeList.get(clickedItemIndex);
    }


    public interface ListItemClickListener {
        void onListItemClick(Establishment establishment);
    }

    public EstablishmentArrayAdapter(List<Establishment> recipes, ListItemClickListener listener) {
        mRecipeList = recipes;
        mOnClickListener = listener;
    }

    @Override
    public EstablishmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_array_establishment_adapter, viewGroup, false);
        EstablishmentViewHolder viewHolder = new EstablishmentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EstablishmentViewHolder holder, int position) {
        holder.bind(mRecipeList.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    class EstablishmentViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView mName;
        TextView mLocation;


        public EstablishmentViewHolder(View viewContext) {
            super(viewContext);


            mName = viewContext.findViewById(R.id.tv_name);
            mLocation = viewContext.findViewById(R.id.tv_location);

            itemView.setOnClickListener(this);
        }

        void bind(Establishment recipe) {
            mName.setText(recipe.name);

            mLocation.setText(recipe.local);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(getItem(clickedPosition));
        }
    }
}
