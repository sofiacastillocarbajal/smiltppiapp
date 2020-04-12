package pe.work.karique.smiltppiapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.smiltppiapp.R;
import pe.work.karique.smiltppiapp.models.UserTravelState;


public class UserTravelStatesAdapter extends RecyclerView.Adapter<UserTravelStatesAdapter.UserViewHolder> {

    private List<UserTravelState> userTravelStates;

    public UserTravelStatesAdapter(List<UserTravelState> userTravelStates) {
        this.userTravelStates = userTravelStates;
    }

    public UserTravelStatesAdapter() {
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.card_user_travel_state, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final UserTravelState userTravelState = userTravelStates.get(position);

        holder.stateTextView.setText(userTravelState.getDescription());
        holder.numberTextView.setText(userTravelState.getNumberOfUsers());
        holder.stateCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onUserClickedListener != null)
                    onUserClickedListener.OnUserClicked(userTravelState);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userTravelStates.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        TextView stateTextView;
        TextView numberTextView;
        CardView stateCardView;

        public UserViewHolder(View itemView) {
            super(itemView);
            stateTextView = itemView.findViewById(R.id.stateTextView);
            numberTextView = itemView.findViewById(R.id.numberTextView);
            stateCardView = itemView.findViewById(R.id.stateCardView);
        }
    }

    public interface OnUserClickedListener {
        void OnUserClicked(UserTravelState userTravelState);
    }

    private OnUserClickedListener onUserClickedListener;

    public void setOnUserClicked(OnUserClickedListener onUserClickedListener) {
        this.onUserClickedListener = onUserClickedListener;
    }
}
