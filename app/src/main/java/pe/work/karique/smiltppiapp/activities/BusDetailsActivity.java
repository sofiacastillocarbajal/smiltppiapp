package pe.work.karique.smiltppiapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pe.work.karique.smiltppiapp.R;
import pe.work.karique.smiltppiapp.adapters.UserTravelStatesAdapter;
import pe.work.karique.smiltppiapp.models.UserTravelState;
import pe.work.karique.smiltppiapp.repositories.UserTravelStateRepositories;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BusDetailsActivity extends AppCompatActivity {

    private static final int UI_ANIMATION_DELAY = 10;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    AppCompatImageButton logOutAppCompatImageButton;

    private RecyclerView userTravelStatesRecyclerView;
    private UserTravelStatesAdapter userTravelStatesAdapter;
    private RecyclerView.LayoutManager userTravelStateLayoutManager;
    private List<UserTravelState> userTravelStates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_details);
        hide();
        mContentView = findViewById(R.id.busDetailsFullscreenConstraintLayout);

        logOutAppCompatImageButton = findViewById(R.id.logOutAppCompatImageButton);
        logOutAppCompatImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        userTravelStatesRecyclerView = findViewById(R.id.userTravelStatesRecyclerView);
        userTravelStates = UserTravelStateRepositories.getInstance().getUserTravelStates();
        userTravelStatesAdapter = new UserTravelStatesAdapter(userTravelStates);
        userTravelStatesAdapter.setOnUserClicked(new UserTravelStatesAdapter.OnUserClickedListener() {
            @Override
            public void OnUserClicked(UserTravelState userTravelState) {

            }
        });
        userTravelStateLayoutManager = new LinearLayoutManager(this);
        userTravelStatesRecyclerView.setAdapter(userTravelStatesAdapter);
        userTravelStatesRecyclerView.setLayoutManager(userTravelStateLayoutManager);
    }

    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @Override
    protected void onResume() {
        hide();
        super.onResume();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
