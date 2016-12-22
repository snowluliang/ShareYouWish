package com.snow.shareyourwish;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener {

    private ImageView mImgBg;
    private EditText mTittle;
    private EditText mAddress;
    private LayoutInflater inflater;
    private RelativeLayout mActionBarView;

    private ImageView share;
    private ImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mImgBg = (ImageView) findViewById(R.id.id_img_bg);
        mTittle = (EditText) findViewById(R.id.id_tv_tittle);
        mAddress = (EditText) findViewById(R.id.id_tv_from);

        mActionBarView = (RelativeLayout) findViewById(R.id.id_tittle_rl_view);
        share = (ImageView) findViewById(R.id.id_action_tittle_share);
        photo = (ImageView) findViewById(R.id.id_action_tittle_photo);

        mImgBg.setOnClickListener(this);
        share.setOnClickListener(this);
        photo.setOnClickListener(this);

        inflater = LayoutInflater.from(this);

    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialog = inflater.inflate(R.layout.popup_window, null);
        dialog.setBackgroundColor(Color.WHITE);
        builder.setView(dialog);

        builder.show();
    }

    //选取自定义的照片
    private void customMyPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 100);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            if (data != null) {
                Drawable drawable = new BitmapDrawable(decodeUri2Bitmap(data));
                mImgBg.setBackground(drawable);
                showAnim();
            }
        }
    }

    public void showAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mImgBg, "alpha", 0, 1);
        animator.setDuration(2000);
        animator.start();
    }

    //将Intent回传数据转换为Bitmap
    private Bitmap decodeUri2Bitmap(Intent data) {

        Bitmap bitmap;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Uri uri = data.getData();
        String[] filePathColum = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int columsIndex = cursor.getColumnIndex(filePathColum[0]);
        String picturePath = cursor.getString(columsIndex);
        bitmap = BitmapFactory.decodeFile(picturePath, options);

        return bitmap;
    }

    //屏幕截图
    private Bitmap getShareCard() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tittle, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_picture:
                customMyPhoto();
                break;
            case R.id.menu_share:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void hindView() {
        if (mActionBarView.getVisibility() == View.VISIBLE) {
            mActionBarView.setVisibility(View.GONE);
            showActionViewAnimGone();
        } else {
            mActionBarView.setVisibility(View.VISIBLE);
            showActionViewAnimIn();
        }
    }

    public void showActionViewAnimGone() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mActionBarView, "translationY", 50, 0);
        animator.setDuration(300);
        animator.start();
    }

    public void showActionViewAnimIn() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mActionBarView, "alpha", 0, 1);
        animator.setDuration(500);
        animator.start();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.id_img_bg:
                hindView();
                break;
            case R.id.id_action_tittle_photo:
                customMyPhoto();
                hindView();
                break;
            case R.id.id_action_tittle_share:
                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                break;

        }

    }
}

