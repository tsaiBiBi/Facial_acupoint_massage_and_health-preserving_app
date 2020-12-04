package com.example.acupuncture;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dataclass.MusicService;

public class homeFragment extends Fragment {

    ImageView gender_image;

    public homeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Intent stopIntent = new Intent(getContext(), MusicService.class);
        getActivity().stopService(stopIntent);

        gender_image = (ImageView) view.findViewById(R.id.image);;
        set_gender_img();
        return view;
    }

    public void set_gender_img() {
        MainActivity activity = (MainActivity) getActivity();
        String vgender = activity.set_gender_img();
        if(vgender.equals("1")) {
            gender_image.setImageResource(R.drawable.man);
        }
        else {
            gender_image.setImageResource(R.drawable.girl);
        }
    }

    public void onResume() {
        super.onResume();
        set_gender_img();
    }
}
