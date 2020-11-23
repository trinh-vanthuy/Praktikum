public enum MessageType {
    LOGIN("Wenn die Nachricht mit '/login ' beginnt"),
    MESSAGE("normale Nachricht. Im Spiel mit /chat"),
    LOGOUT("Logout wenn bye geschrieben wird"),
    EMPTY("keine Nachricht"),
    PRIVATE("wenn die Nachrich mit /dm beginnt"),
    CREATEGAME("Nachricht beginnt mit '/spielerzeugen'"),
    JOIN("Ein Client tritt mit /spielbeitreten einem Spiel bei"),
    STARTGAME("Das Spiel soll gestartet werden."),
    SPIELZUG("mit /play beginnt");


    private String description;
    MessageType(String description) {
        this.description = description;
    }
}
