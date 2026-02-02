package by.instruction.profsouz.data.network;

import java.util.List;

import by.instruction.profsouz.data.model.FpbSection;
import by.instruction.profsouz.data.model.NewsItem;
import by.instruction.profsouz.data.model.TourismItem;
import by.instruction.profsouz.data.model.UnionInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Cloudflare Worker API contract (disabled for now, kept for quick rollback)
public interface ApiService {
    @GET("news")
    Call<List<NewsItem>> getNews(@Query("union") String unionId);

    @GET("fpb")
    Call<List<FpbSection>> getFpbSections();

    @GET("unions")
    Call<List<UnionInfo>> getUnions();

    @GET("tourism")
    Call<List<TourismItem>> getTourism();
}
