package com.example.cryptomoney.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.cryptomoney.R;
import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends ArrayAdapter {

  List list = new ArrayList();

  public TransactionAdapter(Context context, int resource) {
    super(context, resource);
  }

  public void add(Transaction object) {
    super.add(object);
    list.add(object);
  }

  @Override
  public int getCount() {
    return list.size();
  }

  @Override
  public Object getItem(int position) {
    return list.get(position);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View row;
    row = convertView;
    TransactionHolder transactionHolder;
    if (row == null) {
      LayoutInflater layoutInflater = (LayoutInflater) this.getContext()
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      row = layoutInflater.inflate(R.layout.row_layout, parent, false);
      transactionHolder = new TransactionHolder();
      transactionHolder.tx_id = row.findViewById(R.id.tx_id);
      transactionHolder.tx_name = row.findViewById(R.id.tx_name);
      transactionHolder.tx_symbol = row.findViewById(R.id.tx_symbol);
      transactionHolder.tx_price = row.findViewById(R.id.tx_price);
      row.setTag(transactionHolder);
    } else {
      transactionHolder = (TransactionHolder) row.getTag();
    }

    Transaction transaction = (Transaction) this.getItem(position);
    transactionHolder.tx_name.setText(transaction.getAmount());
    transactionHolder.tx_symbol.setText(transaction.getBuying_rate());
    transactionHolder.tx_id.setText(transaction.getId());
    transactionHolder.tx_price.setText(transaction.getTotal());

    return row;
  }

  static class TransactionHolder {
    TextView tx_symbol, tx_name, tx_id, tx_price;
  }
}
