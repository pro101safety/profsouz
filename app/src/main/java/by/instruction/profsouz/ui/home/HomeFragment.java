package by.instruction.profsouz.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.instruction.profsouz.databinding.FragmentHomeBinding;
import by.instruction.profsouz.ui.common.InfoCard;
import by.instruction.profsouz.ui.common.InfoCardAdapter;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private NewsAdapter newsAdapter;
    private InfoCardAdapter categoryAdapter;
    private NewsViewModel viewModel;
    private final Map<String, String> categoryLinks = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryAdapter = new InfoCardAdapter();
        categoryAdapter.submit(buildCategoryCards());
        categoryAdapter.setOnItemClickListener(item -> {
            String link = categoryLinks.get(item.getId());
            if (link != null && !link.trim().isEmpty()) {
                startActivity(NewsDetailActivity.createIntent(requireContext(), link));
            }
        });
        newsAdapter = new NewsAdapter();
        ConcatAdapter concatAdapter = new ConcatAdapter(categoryAdapter, newsAdapter);
        binding.newsList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.newsList.setAdapter(concatAdapter);

        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        viewModel.getNews().observe(getViewLifecycleOwner(), newsAdapter::submit);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private List<InfoCard> buildCategoryCards() {
        List<InfoCard> cards = new ArrayList<>();
        addCategory(cards, "news_v_strane", "В стране", "https://1prof.by/news/v-strane/");
        addCategory(cards, "news_v_mire", "В мире", "https://1prof.by/news/v-mire/");
        addCategory(cards, "news_obshhestvo", "Общество и профсоюзы", "https://1prof.by/news/obshhestvo-i-profsoyuzy/");
        addCategory(cards, "news_economy", "Экономика и бизнес", "https://1prof.by/news/ekonomika-i-biznes/");
        addCategory(cards, "news_life", "Стиль жизни", "https://1prof.by/news/stil-zhizni/");
        addCategory(cards, "news_sport", "Спорт", "https://1prof.by/news/sport/");
        addCategory(cards, "news_help", "Помог профсоюз", "https://1prof.by/news/pomog-profsoyuz/");
        return cards;
    }

    private void addCategory(List<InfoCard> cards, String id, String title, String link) {
        categoryLinks.put(id, link);
        cards.add(new InfoCard(
                id,
                title,
                null,
                null,
                null,
                null,
                null,
                null
        ));
    }
}
