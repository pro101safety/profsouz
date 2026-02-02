package by.instruction.profsouz.ui.unions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import by.instruction.profsouz.data.db.UnionEntity;

import by.instruction.profsouz.data.prefs.UserPreferences;
import by.instruction.profsouz.databinding.FragmentInfoListBinding;
import by.instruction.profsouz.ui.common.InfoCard;
import by.instruction.profsouz.ui.common.InfoCardAdapter;
import by.instruction.profsouz.ui.home.NewsDetailActivity;

public class UnionsFragment extends Fragment {
    private FragmentInfoListBinding binding;
    private InfoCardAdapter adapter;
    private UnionsViewModel viewModel;
    private final Map<String, String> unionLinks = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInfoListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new InfoCardAdapter();
        binding.infoList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.infoList.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(UnionsViewModel.class);
        viewModel.getUnions().observe(getViewLifecycleOwner(), unions -> adapter.submit(mapUnions(unions)));

        UserPreferences preferences = new UserPreferences(requireContext());
        adapter.setOnItemClickListener(item -> {
            String unionId = item.getId() != null ? item.getId() : item.getTitle();
            preferences.setSelectedUnionId(unionId);
            String link = unionLinks.get(unionId);
            if (link != null && !link.trim().isEmpty()) {
                startActivity(NewsDetailActivity.createIntent(requireContext(), link));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refresh();
    }

    private List<InfoCard> mapUnions(List<UnionEntity> unions) {
        List<InfoCard> cards = new ArrayList<>();
        unionLinks.clear();
        if (unions == null) {
            return cards;
        }
        for (UnionEntity union : unions) {
            unionLinks.put(union.getId(), union.getSiteUrl());
            cards.add(new InfoCard(
                    union.getId(),
                    union.getName(),
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            ));
        }
        return cards;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
