package assessment.macys.com.acronymgenerator.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Debasis on 2/1/2016.
 */
public class AcronymListAdapter extends BaseAdapter{
    private Context context;
    private List<String> acronymList;

    public AcronymListAdapter(Context context,List<String> acronymList){
        this.context = context;
        this.acronymList = acronymList;
    }

    @Override
    public int getCount() {
        return this.acronymList.size();
    }

    @Override
    public String getItem(int position) {
        return this.acronymList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.acronymList.indexOf(this.acronymList.get(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(android.R.layout.simple_list_item_1,parent,false);
            TextView acronymTextView = (TextView) convertView.findViewById(android.R.id.text1);
            acronymTextView.setText(this.acronymList.get(position));
        //}
        return convertView;
    }
}
