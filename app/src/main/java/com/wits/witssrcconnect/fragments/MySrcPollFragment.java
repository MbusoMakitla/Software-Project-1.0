package com.wits.witssrcconnect.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wits.witssrcconnect.R;
import com.wits.witssrcconnect.managers.UiManager;

import org.json.JSONArray;

public class MySrcPollFragment extends Fragment {


    @SuppressLint("StaticFieldLeak")
    public static View v = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_src_activity_view, container, false);
        return v;
    }

    public static void init(JSONArray myPolls) {
        if (v == null) return;
        UiManager.populateWithPolls(v.findViewById(R.id.src_activities_holder), myPolls, true);
    }

}
