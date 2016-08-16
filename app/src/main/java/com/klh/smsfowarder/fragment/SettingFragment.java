package com.klh.smsfowarder.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.klh.smsfowarder.PinnedSectionListView.PinnedSectionListView;
import com.klh.smsfowarder.activity.MainScreenActivity;
import com.klh.smsfowarder.activity.NewPhoneNumber;
import com.klh.smsfowarder.database.DBHelper;
import com.klh.smsfowarder.dtc.PhoneNumber;
import com.klh.smsfowarder.smsfowarder.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    String result;

    ArrayList<PhoneNumber> senderArray = new ArrayList<PhoneNumber>();
    ArrayList<PhoneNumber> receiverArray = new ArrayList<PhoneNumber>();


    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getNumberFromDatabase();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting, container, false);
//        PinnedSectionListView lvPhoneNumber = (PinnedSectionListView)root.findViewById(R.id.lv_phonenumbers);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 20;

        LinearLayout senderLayout = (LinearLayout)root.findViewById(R.id.layout_senders);
        for (PhoneNumber p : senderArray) {
            Button b = new Button(getActivity());
            b.setText(p.getName() + "\n" + p.getNumber());
            b.setBackgroundResource(R.drawable.rowlayout);
            b.setLayoutParams(lp);
            b.setId(p.getId());
            b.setTag(DBHelper.TABLE_NAME_SENDER);
            b.setOnClickListener(this);
            senderLayout.addView(b);
        }
//        senderLayout.refreshDrawableState();

        LinearLayout receiverLayout = (LinearLayout)root.findViewById(R.id.layout_receivers);
        for (PhoneNumber p : receiverArray) {
            Button b = new Button(getActivity());
            b.setText(p.getName() + "\n" + p.getNumber());
            b.setBackgroundResource(R.drawable.rowlayout);
            b.setLayoutParams(lp);
            b.setId(p.getId());
            b.setTag(DBHelper.TABLE_NAME_RECEIVER);
            b.setOnClickListener(this);
            receiverLayout.addView(b);
        }


        // Inflate the layout for this fragment
        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        final Button pushed = (Button)v;
        String [] info = pushed.getText().toString().split("\n");
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(info[0]);
        dialog.setMessage(info[1]);
        dialog.setPositiveButton("Modify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getActivity(), NewPhoneNumber.class);
                i.putExtra("_id", pushed.getId());
                i.putExtra("data", pushed.getText().toString());
                i.putExtra("which_table", pushed.getTag().toString());
                i.putExtra("activity_mode", "modify");
                startActivityForResult(i, MainScreenActivity.REQ_CODE_MODIFY_NUMBER);
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void getNumberFromDatabase() {
        DBHelper dbh = new DBHelper(getActivity().getApplicationContext(), "SMSForwarder.db", null, 1);
        senderArray = dbh.getPhoneNumbers(DBHelper.TABLE_NAME_SENDER);
        receiverArray = dbh.getPhoneNumbers(DBHelper.TABLE_NAME_RECEIVER);

    }




    private class PhonenumberListAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return false;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }
    }
}
