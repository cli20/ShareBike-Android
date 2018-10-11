package com.esprit.bikeit;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment  extends Fragment
        implements SceneChangeListener {

protected static final String KEY_POSITION = "KEY_POSITION";

// we have to set a position tag to the root layout of every scene fragment
// this is so the transformer will know who to make a callback to
protected void setRootPositionTag(@NonNull View root) {
        root.setTag(getArguments().getInt(KEY_POSITION));
        }

@Override
public abstract void enterScene(@Nullable ImageView sharedElement, float position);

@Override
public abstract void centerScene(@Nullable ImageView sharedElement);

@Override
public abstract void exitScene(@Nullable ImageView sharedElement, float position);

@Override
public abstract void notInScene();

}
