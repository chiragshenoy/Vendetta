package vendetta.emc.com.vendetta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static vendetta.emc.com.vendetta.RowData.*;

/**
 * Created by Venkatesh on 5/16/2015.
 */
public class CustomArrayAdapter extends ArrayAdapter<RowData>
{
    private ArrayList<RowData> list,list1;int count=0;
    private  Context x;int posG;
    //this custom adapter receives an ArrayList of RowData objects.
//RowData is my class that represents the data for a single row and could be anything.
    public CustomArrayAdapter(Context context, int textViewResourceId, ArrayList<RowData> rowDataList)
    {
        //populate the local list with data.
        super(context, textViewResourceId, rowDataList);
        this.x=context;
        this.list = new ArrayList<RowData>();
        this.list.addAll(rowDataList);
        list1=rowDataList;
    }



    public View getView(final int position, View convertView, ViewGroup parent)
    {
        //creating the ViewHolder we defined earlier.
        final ViewHolder holder = new ViewHolder();

        //creating LayoutInflator for inflating the row layout.
       LayoutInflater inflator = (LayoutInflater)x.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        posG=position;
        //inflating the row layout we defined earlier.
        convertView = inflator.inflate(R.layout.row, null);

        //setting the views into the ViewHolder.
        holder.title = (TextView) convertView.findViewById(R.id.label);
        holder.counter = (TextView) convertView.findViewById(R.id.counter);
        holder.up = (Button) convertView.findViewById(R.id.upvote);
        holder.down = (Button) convertView.findViewById(R.id.downvote);

        //define an onClickListener for the ImageView.
        holder.up.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                count++;
                holder.counter.setText(String.valueOf(count));
                Toast.makeText(x, "Image from row " + position + " was pressed", Toast.LENGTH_LONG).show();
            }
        });
        holder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                holder.counter.setText(String.valueOf(count));
                Toast.makeText(x, "Image from row " + position + " was pressed", Toast.LENGTH_LONG).show();
            }
        });


        //define an onClickListener for the CheckBox.


        //setting data into the the ViewHolder.
        ;


       holder.title.setText(list1.get(position).getTex());


        //return the row view.
        return convertView;
    }


     static class ViewHolder
    {

        TextView title;
        TextView counter;
        Button up,down;
        ImageView changeRowStatus;
    }
}
