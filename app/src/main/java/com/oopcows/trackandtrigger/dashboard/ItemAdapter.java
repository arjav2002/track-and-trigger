package com.oopcows.trackandtrigger.dashboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.oopcows.trackandtrigger.R;
import com.oopcows.trackandtrigger.helpers.CategoryItem;

import java.util.ArrayList;

import static com.oopcows.trackandtrigger.helpers.CowConstants.CHOOSE_PICTURE_REQUEST_CODE;
import static com.oopcows.trackandtrigger.helpers.CowConstants.TAKE_PHOTO_REQUEST_CODE;

public class ItemAdapter extends ResultRecyclerView {

    private final ArrayList<CategoryItem> items;
    private final CategoryActivity categoryActivity;
    private ItemHolder selectedHolder;

    public ItemAdapter(CategoryActivity categoryActivity, RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager, ArrayList<CategoryItem> items) {
        super(recyclerView, layoutManager, items);
        this.items = items;
        this.categoryActivity = categoryActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemHolder) {
            ItemHolder itemHolder = (ItemHolder) holder;
            int pos = holder.getAdapterPosition();
            itemHolder.quantity.setText(String.valueOf(items.get(pos).getQuantity()));
            itemHolder.incQuantity.setOnClickListener((v) -> {
                items.get(pos).increaseQuantity();
                itemHolder.quantity.setText(String.valueOf(items.get(pos).getQuantity()));
            });
            itemHolder.decQuantity.setOnClickListener((v) -> {
                items.get(pos).decreaseQuantity();
                itemHolder.quantity.setText(String.valueOf(items.get(pos).getQuantity()));
            });
            itemHolder.itemName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    items.set(pos, new CategoryItem(String.valueOf(itemHolder.itemName.getText()), items.get(pos).getImgPath(), items.get(pos).getQuantity()));
                }

                @Override
                public void afterTextChanged(Editable editable) {}
            });
            if(items.get(pos).getImgPath().isEmpty()) {
                itemHolder.itemImage.setClickable(true);
                itemHolder.itemImage.setOnClickListener((v) -> {
                    selectImage(categoryActivity);
                    selectedHolder = itemHolder;
                });
                itemHolder.itemImage.setBackgroundResource(R.drawable.ic_baseline_add_a_photo_24);
            }
            else {
                itemHolder.itemImage.setClickable(false);
                setImage(itemHolder, items.get(pos).getImgPath());
            }
        }
    }

    private void setImage(ItemHolder itemHolder, String imgPath) {
        Bitmap bm = null; // @vraj fill this using imgPath
        itemHolder.itemImage.setImageBitmap(bm);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static class ItemHolder extends RecyclerView.ViewHolder {
        private final ImageButton itemImage;
        private final ImageButton incQuantity, decQuantity;
        private final EditText itemName;
        private final TextView quantity;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_photo);
            incQuantity = itemView.findViewById(R.id.increase_quantity);
            decQuantity = itemView.findViewById(R.id.decreaseQuantity);
            itemName = itemView.findViewById(R.id.item_name);
            quantity = itemView.findViewById(R.id.quantity);
        }
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose item picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    categoryActivity.startActivityForResult(takePicture, TAKE_PHOTO_REQUEST_CODE);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    categoryActivity.startActivityForResult(pickPhoto , CHOOSE_PICTURE_REQUEST_CODE);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public ImageButton getSelectedImageButton() {
        return selectedHolder.itemImage;
    }
}
