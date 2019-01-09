package edu.upc.citm.android.speakerfeedback;

public class Vote {

    String poll_id;
    int option;

    public Vote(String pollId, int option) {
        this.poll_id = pollId;
        this.option = option;
    }

    public String getPollid() {
        return poll_id;
    }

    public void setPollid(String poll_id) {
        this.poll_id = poll_id;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }
}
