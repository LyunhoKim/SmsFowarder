package com.klh.smsfowarder.activity;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.klh.smsfowarder.smsfowarder.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSave;
    EditText etSender, etTarget;
    TextView tvLogs;

//    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

        btnSave = (Button)findViewById(R.id.btnSave);
        etSender = (EditText)findViewById(R.id.etSender);
        etTarget = (EditText)findViewById(R.id.etTarget);
        tvLogs = (TextView)findViewById(R.id.tvLastLogs);

        btnSave.setOnClickListener(this);

        SharedPreferences sp = getSharedPreferences("logs", MODE_PRIVATE);
        String receivedSender = sp.getString("receivedSender", "N/A");
        String receivedTimestamp = sp.getString("receivedTimestamp", "N/A");
        String receivedMsg = sp.getString("receivedMsg", "N/A");

        String sentTimestamp = sp.getString("sentTimestamp", "N/A");
        String sentMsg = sp.getString("sentMsg", "N/A");
        String sentTarget = sp.getString("sentTarget", "N/A");

        String lastSentLog = sp.getString("lastSentLog", "N/A");
        String lastDeliveryLog = sp.getString("lastDeliveryLog", "N/A");

        String exception = sp.getString("exception", "N/A");


        tvLogs.setText("#Received Log " + "\n" +
                "-Timestamp: " +  receivedTimestamp + "\n" +
                "-Sender: " + receivedSender + "\n" +
                "-Message: " + receivedMsg +
                "\n\n" +

                "#Send Log" + "\n" +
                "-Timestamp: " + sentTimestamp + "\n" +
                "-Target: " + sentTarget + "\n" +
                "-Message: " + sentMsg + "\n" +
                "-LastSentLog: " + lastSentLog + "\n" +
                "-LastDeliverLog: " + lastDeliveryLog +
                "\n\n" +

                "#Exception " + "\n" + exception);


        SharedPreferences numSp = getSharedPreferences("numbers", MODE_PRIVATE);
        String sender = numSp.getString("sender", "");
        String target = numSp.getString("target", "");

        etSender.setText(sender);
        etTarget.setText(target);


    }

    @Override
    public void onClick(View v) {
        SharedPreferences sp = getSharedPreferences("numbers", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sender", etSender.getText().toString());
        editor.commit();
        editor.putString("target", etTarget.getText().toString());
        editor.commit();

        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
    }


//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        public PlaceholderFragment() {
//        }
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
//            return rootView;
//        }
//    }


//    /**
//     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
//     * one of the sections/tabs/pages.
//     */
//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
//
//        public SectionsPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            // getItem is called to instantiate the fragment for the given page.
//            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
//        }
//
//        @Override
//        public int getCount() {
//            // Show 3 total pages.
//            return 3;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "SECTION 1";
//                case 1:
//                    return "SECTION 2";
//                case 2:
//                    return "SECTION 3";
//            }
//            return null;
//        }
//    }
}
