package com.example.gaurav.mtargetclient;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.qozix.tileview.TileView;
import com.qozix.tileview.detail.DetailLevel;
import com.qozix.tileview.detail.DetailLevelManager;
import com.qozix.tileview.geom.CoordinateTranslater;
import com.qozix.tileview.markers.MarkerLayout;


public class IiiitvFirstFloor extends TileViewActivity {

    double tilewidth;
    double tileheight;
    View markerview;
    int no_row,no_col=0;
    public View markerviewleft, markerviewright, markerviewup, markerviewdown;
    int tileactual = -1, tileleft = -1, tileright = -1, tileup = -1, tiledown = -1;
    private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.layout_locate);
        Intent mintent = getIntent();
        int tile_number = mintent.getIntExtra("tile_number",800);

        // multiple references
        final TileView tileView = getTileView();

        // size of original image at 100% mScale
        tileView.setSize(3200, 2825);

        // we're running from assets, should be fairly fast decodes, go ahead and render asap
        tileView.setShouldRenderWhilePanning(true);

        // detail levels
        tileView.addDetailLevel(1.000f, "images_first_5000_23_26/%d_%d.png", 118, 123);

        tileView.setScaleLimits(5, 5);
        // disable zooming
        //tileView.setShouldScaleToFit(false);
        //tileView.setScaleLimits(1f,1f);

        // define bound for relatice postioning
        tileView.defineBounds(0.0f, 0.0f, 3200.0f, 2825.0f);

        // lets center all markers both horizontally and vertically
        tileView.setMarkerAnchorPoints(-0.5f, -0.5f);

/*
        // set markerTap listener
        tileView.setMarkerTapListener(new MarkerLayout.MarkerTapListener() {
            @Override
            public void onMarkerTap(View view, int x, int y) {
                // get reference to the TileView
                TileView tileView = getTileView();
                // we saved the coordinate in the marker's tag
                CoordinateTranslater coordinateTranslater = tileView.getCoordinateTranslater();
                double cx = coordinateTranslater.translateAndScaleAbsoluteToRelativeX((float) x, tileView.getScale());
                double cy = coordinateTranslater.translateAndScaleAbsoluteToRelativeY((float) y, (tileView).getScale());
                double position[] = new double[]{cx, cy};
                // lets center the screen to that coordinate
                tileView.slideToAndCenter(position[0], position[1]);
                Log.d("position", position[0] + " " + position[1] + " ");
                // create a simple callout
                SampleCallout callout = new SampleCallout(view.getContext());
                // add it to the view tree at the same position and offset as the marker that invoked it
                tileView.addCallout(callout, position[0], position[1], -0.5f, -1.0f);
                // a little sugar
                callout.transitionIn();
                // stub out some text
                callout.setTitle("MAP CALLOUT");
                callout.setSubtitle("Info window at coordinate:\n" + position[1] + ", " + position[0]);
            }
        });
*/

        DetailLevelManager detailLevelManager = tileView.getDetailLevelManager();
        DetailLevel detailLevel = detailLevelManager.getCurrentDetailLevel();

        tilewidth = detailLevel.getTileWidth();
        tileheight = detailLevel.getTileHeight();
        no_row = (int) Math.ceil (tileView.getBaseHeight() / tileheight);
        no_col = (int) Math.ceil(tileView.getBaseWidth()/tilewidth);
        System.out.println("col and row is " + no_col + "  " + no_row);

        placeMarker(R.drawable.reds, tile_number);
        //((RelativeLayout) findViewById(R.id.groundfloormaplayout)).addView(tileView);
        setContentView(tileView);

    }

    private void placeMarker(int resId, int tile_number) {
        ImageView imageView = new ImageView(this);
        MarkerLayout.LayoutParams params = new MarkerLayout.LayoutParams((int)(tilewidth*getTileView().getScale())-10, (int)(tileheight*getTileView().getScale())-10);
        imageView.setLayoutParams(params);
        imageView.setImageResource(resId);

        Pair<Integer, Integer> tilenum = gettileindex(tile_number);
        System.out.println("tile is " +tilenum.first + "  : " +tilenum.second);
        //coordinate of  tile
        double xon = tilewidth * tilenum.first - tilewidth / 2;
        double yon = tileheight * tilenum.second - tileheight / 2;
        System.out.println("tile coordinate : " + xon + "  " + yon);

        // frame the troll
        frameTo(xon, yon);
        markerview = getTileView().addMarker(imageView, xon, yon, null, null);
    }

    // get tile index as col and row number
    private Pair<Integer, Integer> gettileindex(int tile_number) {
        int Y = (int) Math.ceil((float)tile_number/no_col);
        System.out.println("ceuk  : "+Y+ " : tile number is " +tile_number+" float division" + (float)tile_number/no_col);
        int X = tile_number%no_col;
        if (X==0)
            X=no_col;
        Pair<Integer, Integer> tile_no = new Pair<>(X, Y);
        return tile_no;
    }

}
