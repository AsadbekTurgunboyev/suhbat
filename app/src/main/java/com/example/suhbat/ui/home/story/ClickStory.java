package com.example.suhbat.ui.home.story;

import com.example.suhbat.domain.model.StoryModel;

import java.util.List;

public interface ClickStory {

    void buttonAddStory();
    void showStory(int position, List<StoryModel> list);
}
