package org.success.isp.workers;

import org.success.isp.workers.pojos.QuestionAnswer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QuestionManager {
    private static QuestionManager instance;
    private HashMap<Integer, HashMap<Integer, Integer>> connections;

    // Хранение специальных действий по вопросам и ответам
    private HashMap<Integer, HashMap<Integer, Runnable>> specialActions;

    private ArrayList<QuestionAnswer> uAnswers;

    // Получение последовательности ответов пользователя
    public ArrayList<QuestionAnswer> getUserAnswers() {
        return uAnswers;
    }

    private static int nextQ;

    private QuestionManager() {
        connections = new HashMap<>();
        specialActions = new HashMap<>();
        uAnswers = new ArrayList<>();
    }

    public static QuestionManager getInstance() {
        if (instance == null) {
            instance = new QuestionManager();
        }
        return instance;
    }

    public void setSpecialAction(int qid, int answer, Runnable action) {
        if (!specialActions.containsKey(qid)) {
            specialActions.put(qid, new HashMap<>());
        }
        specialActions.get(qid).put(answer, action);
    }

    public void reset() {
        nextQ = 0;
        uAnswers.clear();
    }

    public void setConnection(int qid, int answer, int nextQid) {
        if (!connections.containsKey(qid)) {
            connections.put(qid, new HashMap<>());
        }
        connections.get(qid).put(answer, nextQid);
    }

    public void answer(int qid, int answer) {
        nextQ = prepareNext(qid, answer);

        if (uAnswers == null)
            uAnswers = new ArrayList<>();

        uAnswers.add(new QuestionAnswer(qid, answer));

        // Проверка наличия специального действия для данного ответа на вопрос
        if (specialActions.containsKey(qid) && specialActions.get(qid).containsKey(answer)) {
            specialActions.get(qid).get(answer).run(); // Выполнение специального действия
        }
    }

    private int prepareNext(int qid, int answer) {
        if (connections.containsKey(qid) && connections.get(qid).containsKey(answer)) {
            return connections.get(qid).get(answer);
        }
        // No connection found
        return -1;
    }

    public int getNextQuestion() {
        return nextQ;
    }
}
