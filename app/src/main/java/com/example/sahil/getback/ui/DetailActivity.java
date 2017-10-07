package com.example.sahil.getback.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sahil.getback.R;
import com.example.sahil.getback.adapters.DetailPagerAdapter;
import com.example.sahil.getback.backend.Contract;
import com.example.sahil.getback.extras.Movie;
import com.example.sahil.getback.extras.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.sahil.getback.R.id.fab;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.detail_image) ImageView backdrop_image;
    @BindView(R.id.detail_title) TextView titleView;
    @BindView(R.id.detail_synopsis) TextView synopsisView;
    @BindView(R.id.detail_date) TextView dateView;
    @BindView(R.id.detail_rating) RatingBar ratingBar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(fab) CheckBox fav;

    private ActionProvider mShareActionProvider;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NestedScrollView nestedScrollView = (NestedScrollView) findViewById(R.id.nest_scrollview);
        nestedScrollView.setFillViewport(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        movie = getIntent().getParcelableExtra("movie");
        titleView.setText(movie.getTitle());
        synopsisView.setText(movie.getSynopsis());
        dateView.setText(movie.getRelease_date());
        ratingBar.setRating(Utility.getRating(movie.getMovie_rating()));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        getLoaderManager().initLoader(3, null, this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.detail_view_pager);
        setUpViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.detail_tabs);
        tabLayout.setupWithViewPager(viewPager);

        Picasso.with(this).load(Utility.buildBackdropUrl(movie.getBackdrop_url())).into(backdrop_image, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) backdrop_image.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        applyPalette(palette);
                    }
                });
            }

            @Override
            public void onError() {}
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(fav.isChecked()){
                    getContentResolver().insert(Contract.Movies.CONTENT_URI, Utility.getValuesForMovie(movie));
                }else {
                    getContentResolver().delete(Contract.Movies.getUriForId(Long.parseLong(movie.getId())),
                            null, null);
                }
            }
        });
    }

    private void setUpViewPager(ViewPager viewPager){
        DetailPagerAdapter pagerAdapter = new DetailPagerAdapter(getSupportFragmentManager(), movie.getId());
        pagerAdapter.addFragment(new TrailerFragment(), "Trailers");
        pagerAdapter.addFragment(new ReviewsFragment(), "Reviews");
        viewPager.setAdapter(pagerAdapter);
    }

    private void applyPalette(Palette palette){
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int primary = getResources().getColor(R.color.colorPrimary);

        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                Contract.Movies.getUriForId(Long.parseLong(movie.getId())),
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.getCount() > 0){
            fav.setChecked(true);
        }else {
            fav.setChecked(false);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.action_share:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, movie.getTitle() + "#" + getResources().getString(R.string.app_name));
                startActivity(Intent.createChooser(sharingIntent, "Sharing Option"));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
