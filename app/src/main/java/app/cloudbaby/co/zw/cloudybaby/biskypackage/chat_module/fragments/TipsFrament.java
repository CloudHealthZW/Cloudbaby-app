package app.cloudbaby.co.zw.cloudybaby.biskypackage.chat_module.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.cloudbaby.co.zw.cloudybaby.R;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.adapters.TipsAdapter;
import app.cloudbaby.co.zw.cloudybaby.biskypackage.models.TipsModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A simple {@link Fragment} subclass.
 */
public class TipsFrament extends Fragment {

    @BindView(R.id.rcvTips)
    RecyclerView recyclerView;

    @BindView(R.id.txtTips)
    TextView tips;

    TipsAdapter tipsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tips_frament, container, false);
        ButterKnife.bind(this, view);


        Realm realm = Realm.getDefaultInstance();
        RealmResults<TipsModel> tipsmod = realm.where(TipsModel.class).findAll();

        tipsmod = tipsmod.sort("time", Sort.DESCENDING);

        tipsAdapter = new TipsAdapter(getContext(), tipsmod);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tipsAdapter);
        if (tipsmod.size() > 0) {
            tips.setVisibility(View.INVISIBLE);
        } else {
            tips.setVisibility(View.VISIBLE);
        }

        // Listeners will be notified when data changes
        tipsmod.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<TipsModel>>() {
            @Override
            public void onChange(RealmResults<TipsModel> results, OrderedCollectionChangeSet changeSet) {
                // Query results are updated in real time with fine grained notifications.
                changeSet.getInsertions(); // => [0] is added.
                tipsAdapter.setList(results);

            }
        });


        return view;
    }

}
