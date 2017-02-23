package com.example.aditya.popularmovies;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class moview_list extends Fragment {

    private static GridView gv;
    private static movie_adapter gridadapter;
    private static Context context;
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view= inflater.inflate(R.layout.fragment_moview_list, container, false);
        gv=(GridView)view.findViewById(R.id.gridView);
        ArrayList<movie> m=new ArrayList<movie>();
        m.add(new movie("hello","hello",3,3,"del"));
        gridadapter=new movie_adapter(context=getContext(),m);
        gv.setAdapter(gridadapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                movie mov = (movie) gridadapter.getItem(position);
                Intent intent = new Intent(getContext(), MovieDetail.class);
                intent.putExtra("object", mov);
                startActivity(intent);
            }
        });
        updateMovies();
        return view;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void updateMovies(){
        movieFetcher mf=new movieFetcher();
        SharedPreferences sharepref=PreferenceManager.getDefaultSharedPreferences(getActivity());
        String no = sharepref.getString(getString(R.string.pref_page_key), getString(R.string.pref_page_default));
        int nos=Integer.parseInt(no);
        if(nos>30){
            nos=30;
            Toast.makeText(context,"Unable to fetch more than 600 movies",Toast.LENGTH_SHORT).show();
        }
        if(isNetworkAvailable())
            mf.execute(nos);
        else
            Toast.makeText(context,"Internet not Accessible",Toast.LENGTH_SHORT).show();
    }
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(getContext(),SettingsActivity.class);
            startActivity(intent);
        }
        if(id==R.id.refresh){
            updateMovies();
        }
        if(id==R.id.exit){

        }

        return super.onOptionsItemSelected(item);
    }

    public class movieFetcher extends AsyncTask<Integer,Void,ArrayList<movie>>{
        @Override
        protected ArrayList<movie> doInBackground(Integer...params) {
            HttpURLConnection urlConnection=null;
            BufferedReader reader=null;
            String[] movieStrings=new String[params[0]];
            for(int i=1;i<params[0]+1;i++) {
                try {

                    final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/popular?";
                    final String APPID = "api_key";
                    final String PAGE = "page";

                    Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                            .appendQueryParameter(APPID, "978805eb048b6080d2268be12c9d9142")
                            .appendQueryParameter(PAGE, "" + i)
                            .build();

                    URL url = new URL(builtUri.toString());

                    Log.v("LOG", "Built URI: " + builtUri.toString());

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    movieStrings[i - 1] = buffer.toString();

                    Log.v("LOG", "Movie JSON String: " + movieStrings[i - 1]);

                }
                catch (IOException e) {
                    Toast.makeText(context,"Internet not Accessible",Toast.LENGTH_SHORT).show();
                }
                finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e("LOG", "Error closing stream", e);
                        }
                    }
                }
            }
            try {
                return getMovieDataFromJson(movieStrings);
            }
            catch (JSONException e) {
                Log.e("LOG", e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
        public ArrayList<movie> getMovieDataFromJson(String[] s) throws JSONException{
            ArrayList<movie> al=new ArrayList<movie>();
            for(int i=0;i<s.length;i++){
                JSONObject jo=new JSONObject(s[i]);
                JSONArray arr=jo.getJSONArray("results");
                for(int j=0;j<arr.length();j++){
                    JSONObject mov=arr.getJSONObject(j);
                    al.add(new movie(mov.getString("original_title"),mov.getString("poster_path"),
                            mov.getLong("vote_average"),mov.getLong("popularity"),mov.getString("overview")));
                }
            }
            return al;
        }

        @Override
        protected void onPostExecute(ArrayList<movie> movies) {
            SharedPreferences sharepref=PreferenceManager.getDefaultSharedPreferences(context);
            String sort = sharepref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));
            if(sort.equals("popularity")){
                //Toast.makeText(context,"Sort by popularity",Toast.LENGTH_SHORT).show();
                Collections.sort(movies, new Comparator<movie>() {
                    @Override
                    public int compare(movie lhs, movie rhs) {
                        if (lhs.popularity < rhs.popularity) {
                            return 1;
                        }
                        return -1;
                    }
                });
            }
            if(sort.equals("rating")){
                //Toast.makeText(context,"Sort by rating",Toast.LENGTH_SHORT).show();
                Collections.sort(movies, new Comparator<movie>() {
                    @Override
                    public int compare(movie lhs, movie rhs) {
                        if(lhs.rating<rhs.rating){
                            return 1;
                        }
                        return -1;
                    }
                });
            }
            if(sort.equals("random")){
                //Toast.makeText(context,"Not Sorted",Toast.LENGTH_SHORT).show();
                Collections.shuffle(movies);
            }
            gridadapter=new movie_adapter(context,movies);
            gv.setAdapter(gridadapter);
        }
    }

}
