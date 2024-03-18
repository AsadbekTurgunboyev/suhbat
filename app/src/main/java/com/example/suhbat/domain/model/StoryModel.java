package com.example.suhbat.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class StoryModel implements Parcelable {

    String name;
    String story;
    String storyKey;
    boolean isSeen;

    public StoryModel() {
    }

    public StoryModel(String name, String story, String storyKey, boolean isSeen) {
        this.name = name;
        this.story = story;
        this.storyKey = storyKey;
        this.isSeen = isSeen;
    }

    protected StoryModel(Parcel in) {
        name = in.readString();
        story = in.readString();
        storyKey = in.readString();
        isSeen = in.readByte() != 0;
    }

    public static final Creator<StoryModel> CREATOR = new Creator<StoryModel>() {
        @Override
        public StoryModel createFromParcel(Parcel in) {
            return new StoryModel(in);
        }

        @Override
        public StoryModel[] newArray(int size) {
            return new StoryModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getStoryKey() {
        return storyKey;
    }

    public void setStoryKey(String storyKey) {
        this.storyKey = storyKey;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(story);
        dest.writeString(storyKey);
        dest.writeByte((byte) (isSeen ? 1 : 0));
    }
}
