package com.prowo.ydnamic.context;

public class TacitOption implements Option {

    private String value;

    private String text;

    public TacitOption() {
        super();
    }

    public TacitOption(Option option) {
        super();
        this.value = option.getValue();
        this.text = option.getText();
    }

    public TacitOption(String value, String text) {
        super();
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
