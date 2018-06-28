package io.apptizer.nsdclientapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Order> orderList;

    public OrderListAdapter(Context mContext, List<Order> orderList) {
        this.mContext = mContext;
        this.orderList = orderList;
        this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = mInflater.inflate(R.layout.order_item, parent, false);

        TextView orderListItemId = (TextView) rowView.findViewById(R.id.orderListItemId);
        TextView orderListItemStatus = (TextView) rowView.findViewById(R.id.orderListItemStatus);

        Order order = (Order) getItem(position);
        orderListItemId.setText(order.getOrderId());
        orderListItemStatus.setText(order.getOrderStatus());

        return rowView;
    }
}
