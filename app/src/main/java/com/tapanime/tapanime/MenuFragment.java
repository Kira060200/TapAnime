package com.tapanime.tapanime;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        final String name = args.getString("index", "0");
        Log.d("STRING",name);
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        Button Socialbtn = (Button) v.findViewById(R.id.button5);
        Socialbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Social.class);
                intent.putExtra(Menu.EXTRA_MESSAGE, name);
                startActivity(intent);
            }
        });
        Button Trainingbtn = (Button) v.findViewById(R.id.button6);
        Trainingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Training.class);
                intent.putExtra(Menu.EXTRA_MESSAGE, name);
                startActivity(intent);
            }
        });
                return v;
    }
}