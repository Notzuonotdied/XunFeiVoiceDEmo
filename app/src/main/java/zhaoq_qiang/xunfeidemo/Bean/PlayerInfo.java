package zhaoq_qiang.xunfeidemo.Bean;

public class PlayerInfo {
    private String headIcon;
    private String textContent;

    private boolean isMine;

    public String getTextContent() {
        return textContent;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }
}
