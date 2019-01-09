package edu.upc.citm.android.speakerfeedback;

import java.util.Date;
import java.util.List;

public class Poll {


    private List<String> list_options;
    private String id;
    private String questions;

    private Date start, end;
    private List<Integer> results;
    private int optionClicked = -1;
    private boolean open;

    Poll() {}

    public String getOptionsAsString() {
        StringBuilder b = new StringBuilder();

        // Suma de molts strings a Java
        for (String opt : list_options) {
            b.append(opt);
            b.append("\n");
        }

        return b.toString();
    }

    public void setOptions(List<String> options) {
        this.list_options = options;
    }

    public int getOptionClicked() {
        return optionClicked;
    }

    public void setOptionClicked(int optionClicked) {
        this.optionClicked = optionClicked;
    }

    public boolean isOpen() {
        return open;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return questions;
    }

    public void setQuestion(String question) {
        this.questions = question;
    }

    public List<String> getOptions() {
        return list_options;
    }

    public List<Integer> getResults() {
        return results;
    }

    public void setResults(List<Integer> results) {
        this.results = results;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }



    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }




}
