package com.klh.smsfowarder.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.klh.smsfowarder.activity.LogDetailActivity;
import com.klh.smsfowarder.database.DBHelper;
import com.klh.smsfowarder.dtc.ForwardingLog;
import com.klh.smsfowarder.smsfowarder.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class LogFragment extends Fragment implements AdapterView.OnItemClickListener{

    private OnListFragmentInteractionListener mListener;
    private ArrayList<ForwardingLog> logArray;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LogFragment() {
    }


    public static LogFragment newInstance() {
        return new LogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DBHelper dbh = new DBHelper(getActivity(), "SMSForwarder.db", null, 1);
        logArray = dbh.getForwardingLog();

        dbh.close();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_log, container, false);
        ListView lvLog = (ListView)view.findViewById(R.id.lv_logs);
        lvLog.setOnItemClickListener(this);
        lvLog.setAdapter(new LogAdapter());


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ForwardingLog log = logArray.get(position);
        Intent i = new Intent(getActivity(), LogDetailActivity.class);
        i.putExtra("date", log.getDate());
        i.putExtra("log", log.getLog());
        i.putExtra("message", log.getMessage());

        startActivity(i);


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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Uri uri);
    }

    private class LogAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return logArray.size();
        }

        @Override
        public Object getItem(int position) {
            return logArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater(null).inflate(R.layout.row_log_listview, null, false);

            TextView tvDate = (TextView)view.findViewById(R.id.cell_tv_date);
            TextView tvMsg = (TextView)view.findViewById(R.id.cell_tv_msg);
            TextView tvLog = (TextView)view.findViewById(R.id.cell_tv_log);

            tvDate.setText(logArray.get(position).getDate());
            tvMsg.setText(logArray.get(position).getMessage());
            tvLog.setText(logArray.get(position).getLog());

            return view;
        }
    }


}
