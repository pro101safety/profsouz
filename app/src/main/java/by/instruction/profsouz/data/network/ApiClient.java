package by.instruction.profsouz.data.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Cloudflare Worker backend (disabled for now, kept for quick rollback)
    private static final String BASE_URL = "https://fpb-aggregator.killman-instruct.workers.dev/";
    private static ApiService instance;

    public static ApiService getInstance() {
        if (instance == null) {
            OkHttpClient client = new OkHttpClient.Builder().build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            instance = retrofit.create(ApiService.class);
        }
        return instance;
    }
}
