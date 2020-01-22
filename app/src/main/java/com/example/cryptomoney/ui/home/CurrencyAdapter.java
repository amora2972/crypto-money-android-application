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

public class CurrencyAdapter extends ArrayAdapter {

  List list = new ArrayList();

  public CurrencyAdapter(Context context, int resource) {
    super(context, resource);
  }

  public void add(Currency object) {
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
    CurrencyHolder currencyHolder;
    if (row == null) {
      LayoutInflater layoutInflater = (LayoutInflater) this.getContext()
          .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      row = layoutInflater.inflate(R.layout.row_layout, parent, false);
      currencyHolder = new CurrencyHolder();
      currencyHolder.tx_id = row.findViewById(R.id.tx_id);
      currencyHolder.tx_name = row.findViewById(R.id.tx_name);
      currencyHolder.tx_symbol = row.findViewById(R.id.tx_symbol);
      currencyHolder.tx_price = row.findViewById(R.id.tx_price);
      row.setTag(currencyHolder);
    } else {
      currencyHolder = (CurrencyHolder) row.getTag();
    }

    Currency currency = (Currency) this.getItem(position);
    currencyHolder.tx_name.setText(currency.getName());
    currencyHolder.tx_symbol.setText(currency.getSymbol());
    currencyHolder.tx_id.setText(currency.getId());
    currencyHolder.tx_price.setText(currency.getPrice());

    return row;
  }

  static class CurrencyHolder {

    TextView tx_symbol, tx_name, tx_id, tx_price;
  }
}
