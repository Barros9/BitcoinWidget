package com.barros.bitcoinwidget

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivityViewModel : ViewModel() {

    private var _price = MutableLiveData<String>()
    val price: LiveData<String> = _price

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.e(throwable, "Error")
        if (YOUR_KEY.isEmpty()) {
            _price.postValue("NO API KEY")
        } else {
            _price.postValue("Error")
        }
    }

    init {
        fetchPrice()
    }

    private fun fetchPrice() {
        viewModelScope.launch(exceptionHandler) {
            val response = BitcoinService.getBitcoinService().getInfo()
            _price.value =
                response.body()!!.data.first { it.symbol == BTC }.quote.currency.formatPrice()
        }
    }

}