package org.success.isp.workers.pojos;

import java.io.Serializable;

public class QuestionAnswer implements Serializable {
    private int question;

    private int answer;

    public QuestionAnswer() {
    }

    public QuestionAnswer(int question, int answer) {
        this.question = question;
        this.answer = answer;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }
}
