package com.dfhrms.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.dfhrms.Class.Class_URL;
import com.dfhrms.R;
import com.dfhrms.SpinnerModels.SubTopic_SM;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Subtopic_list_Activity extends AppCompatActivity {
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items;
    private ArrayList<String> filteredItems;
    private Set<String> checkedItems;
    ProgressDialog progressDialog;
    private List<String> newCheckedItems;
    String topic_id;
    ArrayList<String> subTopicNamesList;
    private List<SubTopic_SM> subTopicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dropdown_layout);
        Bundle extras = getIntent().getExtras();
        topic_id = extras.getString("Topic_id");
        listView = findViewById(R.id.listView);
        //items = new ArrayList<>(Arrays.asList("Purnima", "Satya", "Anjali", "Vijay", "Wasim", "Raghavendra", "Anand", "Sharad", "Abhinay", "Prathamesh", "Mallikarjun", "Kavana", "Soumya", "Johnson")); // Your items here
        Get_subtopic_list get_subtopic_list = new Get_subtopic_list();
        get_subtopic_list.execute();

        SearchView searchView = findViewById(R.id.searchView);
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        if (searchEditText != null) {
            searchEditText.setTextColor(ContextCompat.getColor(this, R.color.black));
            searchEditText.setHintTextColor(ContextCompat.getColor(this, R.color.black));
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterItems(newText);
                return false;
            }
        });

/*        Button okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(v -> {
            getSelectedItems();
            returnSelectedItems();
        });*/

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = filteredItems.get(position);
            if (listView.isItemChecked(position)) {
                checkedItems.add(selectedItem);
            } else {
                checkedItems.remove(selectedItem);
            }
        });
    }

    private void filterItems(String searchText) {
        filteredItems = subTopicList.stream()
                .filter(subTopicName -> subTopicName.getSubTopicName().toLowerCase().contains(searchText.toLowerCase()))
                .map(SubTopic_SM::getSubTopicName)
                .collect(Collectors.toCollection(ArrayList::new));

        updateListView(filteredItems);
    }

    private void updateListView(ArrayList<String> itemList) {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, itemList);
        listView.setAdapter(adapter);

        for (int i = 0; i < itemList.size(); i++) {
            if (checkedItems.contains(itemList.get(i))) {
                listView.setItemChecked(i, true);
            }
        }
    }

    private void getSelectedItems() {
        newCheckedItems = new ArrayList<>();
        for (int i = 0; i < listView.getCount(); i++) {
            if (listView.isItemChecked(i)) {
                newCheckedItems.add(filteredItems.get(i));
            }
        }
    }

    private void returnSelectedItems() {
        checkedItems.clear();
        checkedItems.addAll(newCheckedItems);
        List<SubTopic_SM> selected_substopicnames = subTopicList.stream()
                .filter(subtopicname -> newCheckedItems.contains(subtopicname.getSubTopicName()))
                .collect(Collectors.toList());

        // Convert to JSON format (you may use a library like Gson for this)
        List<SubTopic_SM> selectedTopics = selected_substopicnames.stream()
                .map(subtopics -> new SubTopic_SM(subtopics.getSubTopicId(), subtopics.getSubTopicName()))
                .collect(Collectors.toList());

        Gson gson = new Gson();
        String jsonResult = gson.toJson(selectedTopics);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("selectedItems", jsonResult);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    //Get sub-topic list

    public class Get_subtopic_list extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(Class_URL.API_URL+"getsubtopiclist?TopicMain_Id="+topic_id);
                    System.out.println("myurl: "+url);
                    //open a URL coonnection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    if (urlConnection.getResponseCode() == 200) {
                        System.out.println("I am running");
                    } else {
                        System.out.println("API Connection Denied");
                    }
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    while (data != -1) {
                        result += (char) data;
                        data = isw.read();
                    }

                    // return the data to onPostExecute method
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog to show the user what is happening
            progressDialog = new ProgressDialog(Subtopic_list_Activity.this);
            progressDialog.setMessage("processing results");
            progressDialog.setCancelable(false);
        }
        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                super.onPostExecute(s);
                JSONArray subTopicsArray = jsonObject.getJSONArray("subTopics");
                subTopicList = new ArrayList<>();

                for (int i = 0; i < subTopicsArray.length(); i++) {
                    JSONObject subTopicObj = subTopicsArray.getJSONObject(i);
                    String subTopicId = subTopicObj.getString("subTopicId");
                    String subTopicName = subTopicObj.getString("subTopicName");
                    SubTopic_SM subTopic = new SubTopic_SM(subTopicId, subTopicName);
                    subTopicList.add(subTopic);
                }
                filteredItems = subTopicList.stream().map(SubTopic_SM::getSubTopicName).collect(Collectors.toCollection(ArrayList::new));
                checkedItems = new HashSet<>();
                adapter = new ArrayAdapter<>(Subtopic_list_Activity.this, android.R.layout.simple_list_item_multiple_choice, filteredItems);
                listView.setAdapter(adapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                for (SubTopic_SM subTopic : subTopicList) {
                    System.out.println("Subtopic_list" + subTopic.getSubTopicName() + " - " + subTopic.getSubTopicId());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
