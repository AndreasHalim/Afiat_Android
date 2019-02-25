package com.example.afiat.screen.achievement;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afiat.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AchievementFragment extends Fragment implements AchievementContract.View {
    @BindView(R.id.tv_max_steps) TextView tvMaxSteps;
    @BindView(R.id.tv_max_speed) TextView tvMaxSpeed;
    @BindView(R.id.tv_max_distance) TextView tvMaxDistance;
    @BindView(R.id.btn_share) Button btnShare;

    private AchievementBus bus;
    private Context context;

    public static AchievementFragment newInstance(AchievementBus bus) {
        AchievementFragment fragment = new AchievementFragment();
        fragment.bus = bus;
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bus.onStart();
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bus.onShare();
            }
        });
    }

    @Override
    public void setMaxSteps(int steps) {
        tvMaxSteps.setText(String.valueOf(steps));
    }

    @Override
    public void setHighestSpeed(float speed) {
        tvMaxSpeed.setText(String.valueOf(speed));
    }

    @Override
    public void setLongestDistance(float distance) {
        tvMaxDistance.setText(String.valueOf(distance));
    }

    @Override
    public void sendTwitterPost(String twit) {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, twit);
        tweetIntent.setType("text/plain");

        PackageManager packManager = context.getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent,  PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for(ResolveInfo resolveInfo: resolvedInfoList){
            if(resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")){
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name );
                resolved = true;
                break;
            }
        }

        if(resolved){
            startActivity(tweetIntent);
        }else{
            Toast.makeText(context, "Twitter app isn't found", Toast.LENGTH_LONG).show();
        }
    }
}
