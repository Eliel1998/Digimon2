package com.example.digimon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.digimon.R;
import com.example.digimon.entity.User;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private Context context;
    private List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        super(context, 0, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.list_item_user, parent, false);
        }

        User currentUser = userList.get(position);

        TextView userNameTextView = listItemView.findViewById(R.id.userNameTextView);
        TextView scoreTextView = listItemView.findViewById(R.id.scoreTextView);

        userNameTextView.setText(currentUser.getUserName());
        scoreTextView.setText(String.valueOf(currentUser.getScore()));

        return listItemView;
    }
}

