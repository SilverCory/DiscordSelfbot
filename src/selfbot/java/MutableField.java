package selfbot.java;

import net.dv8tion.jda.core.entities.MessageEmbed;

public class MutableField {
    private String name = "";
    private String value = "";
    private boolean inline = false;

    public MutableField() {
    }

    public MessageEmbed.Field asField() {
        return new MessageEmbed.Field(name, value, inline);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }
}
