package org.success.isp.pojos;

import java.io.Serializable;
import java.util.ArrayList;

public class IssueQuestion implements Serializable {
    private int qid;

    private String question;

    private ArrayList<String> options;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public IssueQuestion(int qid, String question, ArrayList<String> options) {
        this.qid = qid;
        this.question = question;
        this.options = options;
    }

    public int getQid() {
        return qid;
    }
}
