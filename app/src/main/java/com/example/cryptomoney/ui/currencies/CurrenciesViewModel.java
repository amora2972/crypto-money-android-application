package com.example.cryptomoney.ui.currencies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CurrenciesViewModel extends ViewModel {

  private MutableLiveData<String> mText;

  public CurrenciesViewModel() {
    mText = new MutableLiveData<>();
    mText.setValue("This is currencies fragment");
  }

  public LiveData<String> getText() {
    return mText;
  }
}