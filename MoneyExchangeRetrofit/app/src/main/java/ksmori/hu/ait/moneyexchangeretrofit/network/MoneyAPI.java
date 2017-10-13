package ksmori.hu.ait.moneyexchangeretrofit.network;

import ksmori.hu.ait.moneyexchangeretrofit.data.MoneyResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sophie on 04-May-17.
 */

public class MoneyAPI {

    @GET("lates")
    Call<MoneyResult> getRatesForUsd(@Query("base") String base);
}
