package com.dfhrms.SpinnerModels;

public class SubTopic_SM {
    private String subTopicName,subTopicId;

    public SubTopic_SM(String subTopicId, String subTopicName) {
        this.subTopicId = subTopicId;
        this.subTopicName = subTopicName;
    }

    public String getSubTopicId() {
        return subTopicId;
    }

    public String getSubTopicName() {
        return subTopicName;
    }

    @Override
    public String toString() {
        return subTopicName; // This is what will be displayed in the ListView
    }
}
