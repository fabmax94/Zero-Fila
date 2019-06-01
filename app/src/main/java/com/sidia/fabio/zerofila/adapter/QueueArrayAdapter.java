package com.sidia.fabio.zerofila.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sidia.fabio.zerofila.R;
import com.sidia.fabio.zerofila.model.Clerk;

import java.util.List;

public class QueueArrayAdapter extends RecyclerView.Adapter<QueueArrayAdapter.EstablishmentViewHolder> {

    private List<Clerk> mRecipeList;

    final private ListItemClickListener mOnClickListener;

    public Clerk getItem(int clickedItemIndex) {
        return mRecipeList.get(clickedItemIndex);
    }


    public interface ListItemClickListener {
        void onListItemClick(Clerk establishment);
    }

    public QueueArrayAdapter(List<Clerk> recipes, ListItemClickListener listener) {
        mRecipeList = recipes;
        mOnClickListener = listener;
    }

    @Override
    public EstablishmentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_array_clerk_adapter, viewGroup, false);
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
        TextView mType;


        public EstablishmentViewHolder(View viewContext) {
            super(viewContext);


            mName = viewContext.findViewById(R.id.tv_name);
            mType = viewContext.findViewById(R.id.tv_type);

            itemView.setOnClickListener(this);
        }

        void bind(Clerk clerk) {
            mName.setText(clerk.name);

            mType.setText(clerk.type);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(getItem(clickedPosition));
        }
    }
}
