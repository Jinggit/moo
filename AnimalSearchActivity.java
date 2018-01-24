package com.moocall.moocall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import com.moocall.moocall.adapter.AnimalListAdapter;
import com.moocall.moocall.db.AnimalDb;
import com.moocall.moocall.db.AnimalDbDao.Properties;
import com.moocall.moocall.domain.Animal;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.util.StorageContainer;
import java.util.List;
import org.greenrobot.greendao.query.WhereCondition;

public class AnimalSearchActivity extends Activity {
    private AnimalListAdapter animalListAdapter;
    private ListView animalsListView;
    private Boolean offline;
    private SearchView search;
    private String searchText;
    private Toolbar toolbar;

    class C04301 implements OnClickListener {
        C04301() {
        }

        public void onClick(View v) {
            AnimalSearchActivity.this.onBackPressed();
        }
    }

    class C04312 implements OnFocusChangeListener {
        C04312() {
        }

        public void onFocusChange(View view, boolean b) {
            System.out.println("onFocusChange");
        }
    }

    class C04323 implements OnQueryTextListener {
        C04323() {
        }

        public boolean onQueryTextSubmit(String query) {
            AnimalSearchActivity.this.searchText = query;
            AnimalSearchActivity.this.searchAnimals();
            return false;
        }

        public boolean onQueryTextChange(String newText) {
            AnimalSearchActivity.this.searchText = newText;
            AnimalSearchActivity.this.searchAnimals();
            return false;
        }
    }

    class C04334 implements OnItemClickListener {
        C04334() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (position < AnimalSearchActivity.this.animalListAdapter.getAnimals().size()) {
                AnimalDb animalDb = (AnimalDb) AnimalSearchActivity.this.animalListAdapter.getAnimals().get(position);
                Intent intent;
                if (AnimalSearchActivity.this.offline == null || !AnimalSearchActivity.this.offline.booleanValue()) {
                    Animal animal = new Animal(animalDb.getAnimal_id(), animalDb.getTag_number(), animalDb.getType(), animalDb.getName());
                    intent = new Intent(AnimalSearchActivity.this, AnimalDetailsActivity.class);
                    intent.putExtra("animal", animal);
                    intent.putExtra("subtype", 5);
                    AnimalSearchActivity.this.startActivity(intent);
                    return;
                }
                intent = new Intent(AnimalSearchActivity.this, OfflineAnimalDetailsActivity.class);
                intent.putExtra("id", animalDb.getId());
                intent.putExtra("subtype", 5);
                AnimalSearchActivity.this.startActivity(intent);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0530R.layout.activity_animal_search);
        this.offline = (Boolean) getIntent().getSerializableExtra("offline");
        onResume();
        setupToolbar();
        setupLayout();
        implementListeners();
    }

    private void setupToolbar() {
        this.toolbar = (Toolbar) findViewById(C0530R.id.toolbar);
        ((ImageView) findViewById(C0530R.id.toolbarTitle)).setVisibility(8);
        this.toolbar.setNavigationOnClickListener(new C04301());
        this.toolbar.setTitle(getString(C0530R.string.search));
    }

    private void setupLayout() {
        this.animalsListView = (ListView) findViewById(C0530R.id.animalsListView);
        this.animalListAdapter = new AnimalListAdapter(this);
        this.animalsListView.setAdapter(this.animalListAdapter);
        this.search = (SearchView) findViewById(C0530R.id.search);
        this.search.setActivated(true);
        this.search.onActionViewExpanded();
        this.search.clearFocus();
        this.searchText = "";
    }

    private void searchAnimals() {
        List<AnimalDb> animalList = ((MoocallAnalyticsApplication) getApplication()).getDaoSession().getAnimalDbDao().queryBuilder().where(Properties.Status.in(Integer.valueOf(1), Integer.valueOf(4)), new WhereCondition[0]).whereOr(Properties.Name.like("%" + this.searchText + "%"), Properties.Tag_number.like("%" + this.searchText + "%"), new WhereCondition[0]).orderAsc(Properties.Tag_number).list();
        if (this.animalListAdapter != null) {
            this.animalListAdapter.setAnimals(animalList);
        }
    }

    private void implementListeners() {
        this.search.setOnQueryTextFocusChangeListener(new C04312());
        this.search.setOnQueryTextListener(new C04323());
        this.animalsListView.setOnItemClickListener(new C04334());
    }

    public void onBackPressed() {
        finish();
    }

    protected void onResume() {
        super.onResume();
        StorageContainer.wakeApp(this);
        searchAnimals();
    }
}
