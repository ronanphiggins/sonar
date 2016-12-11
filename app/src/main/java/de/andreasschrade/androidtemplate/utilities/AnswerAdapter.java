package de.andreasschrade.androidtemplate.utilities;

import android.content.Context;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.backendless.Answer;

public class AnswerAdapter extends ArrayAdapter<Answer> {

    public AnswerAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AnswerAdapter(Context context, int resource, List<Answer> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.itemlistrow, null);
        }

        Answer p = getItem(position);


        if (p != null) {

            AnswerTag tag = new AnswerTag(p.getObjectId());

            v.setTag(tag);

            TextView tt2 = (TextView) v.findViewById(R.id.answer);


            if (tt2 != null) {
                tt2.setText(p.getAnswer());
            }


        }


        return v;
    }

}