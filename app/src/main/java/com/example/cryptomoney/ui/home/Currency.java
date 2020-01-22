package com.example.cryptomoney.ui.home;

public class Currency {
  private String id;
  private String symbol;
  private String name;
  private String price;

  public Currency(String id, String symbol, String name, String price) {
    this.id = id;
    this.symbol = symbol;
    this.name = name;
    this.price = price;
  }

  public Currency(String id, String symbol, String name) {
    this.id = id;
    this.symbol = symbol;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }
}
