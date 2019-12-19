package com.stolz.placessearch;

import androidx.annotation.NonNull;

/**
 * This callback alerts the caller that a type ahead suggestion was selected
 */
public interface TypeAheadSuggestionClickedListener {
    void onSuggestionClicked(@NonNull String clickedSuggestion);
}
