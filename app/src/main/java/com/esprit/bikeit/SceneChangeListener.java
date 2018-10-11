package com.esprit.bikeit;

import android.support.annotation.Nullable;
import android.widget.ImageView;

public interface SceneChangeListener {
    void enterScene(@Nullable ImageView sharedElement, float position);

    void centerScene(@Nullable ImageView sharedElement);

    void exitScene(@Nullable ImageView sharedElement, float position);

    void notInScene();
}
