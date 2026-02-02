package by.instruction.profsouz.ui.fpb;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import by.instruction.profsouz.databinding.FragmentInfoListBinding;
import by.instruction.profsouz.ui.common.InfoCard;
import by.instruction.profsouz.ui.common.InfoCardAdapter;
import by.instruction.profsouz.ui.home.NewsDetailActivity;

public class FpbFragment extends Fragment {
    private FragmentInfoListBinding binding;
    private InfoCardAdapter adapter;

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
        adapter.submit(buildCards());

        adapter.setOnItemClickListener(item -> {
            if (item.getSiteUrl() == null || item.getSiteUrl().trim().isEmpty()) {
                return;
            }
            startActivity(NewsDetailActivity.createIntent(requireContext(), item.getSiteUrl()));
        });
    }

    private List<InfoCard> buildCards() {
        List<InfoCard> cards = new ArrayList<>();
        cards.add(new InfoCard(
                "fpb_brest",
                "БРЕСТСКОЕ ОБЛАСТНОЕ ОБЪЕДИНЕНИЕ ПРОФСОЮЗОВ",
                null,
                "https://fpb.1prof.by/organizacionnaya-struktura-fpb/brestskoe-oblastnoe-obedinenie-profsoyuzov/",
                null,
                null,
                null,
                null
        ));
        cards.add(new InfoCard(
                "fpb_vitebsk",
                "ВИТЕБСКОЕ ОБЛАСТНОЕ ОБЪЕДИНЕНИЕ ПРОФСОЮЗОВ",
                null,
                "https://fpb.1prof.by/organizacionnaya-struktura-fpb/vitebskoe-oblastnoe-obedinenie-profsoyuzov/",
                null,
                null,
                null,
                null
        ));
        cards.add(new InfoCard(
                "fpb_gomel",
                "ГОМЕЛЬСКОЕ ОБЛАСТНОЕ ОБЪЕДИНЕНИЕ ПРОФСОЮЗОВ",
                null,
                "https://fpb.1prof.by/organizacionnaya-struktura-fpb/gomelskoe-oblastnoe-obedinenie-profsoyuzov/",
                null,
                null,
                null,
                null
        ));
        cards.add(new InfoCard(
                "fpb_grodno",
                "ГРОДНЕНСКОЕ ОБЛАСТНОЕ ОБЪЕДИНЕНИЕ ПРОФСОЮЗОВ",
                null,
                "https://fpb.1prof.by/organizacionnaya-struktura-fpb/grodnenskoe-oblastnoe-obedinenie-profsoyuzov/",
                null,
                null,
                null,
                null
        ));
        cards.add(new InfoCard(
                "fpb_minsk_city",
                "МИНСКОЕ ГОРОДСКОЕ ОБЪЕДИНЕНИЕ ПРОФСОЮЗОВ",
                null,
                "https://fpb.1prof.by/organizacionnaya-struktura-fpb/minskoe-gorodskoe-obedinenie-profsoyuzov/",
                null,
                null,
                null,
                null
        ));
        cards.add(new InfoCard(
                "fpb_minsk_region",
                "МИНСКОЕ ОБЛАСТНОЕ ОБЪЕДИНЕНИЕ ПРОФСОЮЗОВ",
                null,
                "https://fpb.1prof.by/organizacionnaya-struktura-fpb/minskoe-oblastnoe-obedinenie-profsoyuzov/",
                null,
                null,
                null,
                null
        ));
        cards.add(new InfoCard(
                "fpb_mogilev",
                "МОГИЛЕВСКОЕ ОБЛАСТНОЕ ОБЪЕДИНЕНИЕ ПРОФСОЮЗОВ",
                null,
                "https://fpb.1prof.by/organizacionnaya-struktura-fpb/mogilevskoe-oblastnoe-obedinenie-profsoyuzov/",
                null,
                null,
                null,
                null
        ));
        return cards;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
