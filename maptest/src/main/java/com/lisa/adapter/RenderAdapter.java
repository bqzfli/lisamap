package com.lisa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.haha.maptest.R;
import com.lisa.bean.Bean_business_child;
import com.lisa.bean.Bean_business_group;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Caesa on 2016/3/16.
 */
public class RenderAdapter extends BaseExpandableListAdapter {
    List<Bean_business_group> bean_business_groups;
    List<Bean_business_child> bean_business_groups1;
    Context context;

    public RenderAdapter(List<Bean_business_group> bean_business_groups, Context context) {
        this.bean_business_groups = bean_business_groups;
        this.context = context;
        bean_business_groups1 = new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return bean_business_groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return bean_business_groups.get(groupPosition).getSon().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return bean_business_groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return bean_business_groups.get(groupPosition).getSon().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_business_group, null);
        TextView tv_group_id = (TextView) convertView.findViewById(R.id.tv_business);
        tv_group_id.setText(bean_business_groups.get(groupPosition).getMC());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_render_child, null);
        TextView tv_child_text = (TextView) convertView.findViewById(R.id.tv_child_text);
        final CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
        List<Bean_business_child> son = bean_business_groups.get(groupPosition).getSon();
        final Bean_business_child bean_business_child = son.get(childPosition);
        tv_child_text.setText(bean_business_child.getMC());
        checkbox.setChecked(bean_business_groups.get(groupPosition).getSon().get(childPosition).isRenderShow());
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bean_business_groups.get(groupPosition).getSon().get(childPosition).setRenderShow(isChecked);
                if (bean_business_groups1.contains(bean_business_groups.get(groupPosition).getSon().get(childPosition))) {
                    bean_business_groups1.remove(bean_business_groups.get(groupPosition).getSon().get(childPosition));
                } else {
                    bean_business_groups1.add(bean_business_groups.get(groupPosition).getSon().get(childPosition));
                }
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public List<Bean_business_child> getResult() {
        return bean_business_groups1;
    }
}
