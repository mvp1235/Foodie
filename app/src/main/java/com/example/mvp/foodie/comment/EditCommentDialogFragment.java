package com.example.mvp.foodie.comment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mvp.foodie.R;
import com.example.mvp.foodie.models.Comment;

import static com.example.mvp.foodie.UtilHelper.COMMENT_ID;
import static com.example.mvp.foodie.UtilHelper.COMMENT_TEXT;

public class EditCommentDialogFragment extends DialogFragment {

    private String commentText;
    private String commentID;
    private AppCompatEditText commentEditText;
    private AppCompatButton saveBtn, cancelBtn;

    public EditCommentDialogFragment() {
        // Required empty public constructor
    }

    public interface EditListener {
        void onSaveClick(String commentText, String commentID);
    }

    static EditCommentDialogFragment newInstance(String commentText, String commentID) {
        EditCommentDialogFragment f = new EditCommentDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(COMMENT_TEXT, commentText);
        args.putString(COMMENT_ID, commentID);
        f.setArguments(args);

        return f;
    }

    private EditListener editListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            editListener = (EditListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commentText = getArguments().getString(COMMENT_TEXT);
        commentID = getArguments().getString(COMMENT_ID);

        if (commentText.contains("(edited)")) {
            int size = commentText.length();
            Log.i("TEST", commentText.substring(size-8, size));
            if (commentText.substring(size-8).equals("(edited)")) {
                commentText = commentText.substring(0, size-9);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_comment_dialog, container, false);

        saveBtn = view.findViewById(R.id.saveBtn_id);
        cancelBtn = view.findViewById(R.id.cancelBtn_id);
        commentEditText = view.findViewById(R.id.commentEditText_id);

        commentEditText.setText(commentText);
        commentEditText.setSelection(commentText.length());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editListener.onSaveClick(commentEditText.getText().toString(), commentID);
                EditCommentDialogFragment.this.getDialog().cancel();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCommentDialogFragment.this.getDialog().cancel();
            }
        });


        return view;
    }

}
