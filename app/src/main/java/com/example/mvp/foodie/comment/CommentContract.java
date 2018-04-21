package com.example.mvp.foodie.comment;

import com.example.mvp.foodie.BaseActivity;
import com.example.mvp.foodie.models.Comment;
import com.example.mvp.foodie.models.User;

import java.util.List;

public interface CommentContract {
    interface View {
        void onCommentsLoadSuccess(Comment comment);
        void onCommentsLoadFailure(String error);
        void onCommentSuccess(Comment comment);
        void onCommentFailure(String error);
        void onCommentEditSuccess(Comment comment);
        void onCommentEditFailure(String error);
    }


    interface Presenter {
        void editComment(BaseActivity activity, String commentID, String newContent);
        void loadComments(BaseActivity activity, String postID);
        void postComment(BaseActivity activity, String postID, String commentText, String commenterID);
    }

}
