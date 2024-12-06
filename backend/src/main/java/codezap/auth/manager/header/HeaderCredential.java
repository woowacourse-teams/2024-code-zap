package codezap.auth.manager.header;

public record HeaderCredential(String type, String value) {

    private static final String DELIMITER = " ";
    private static final int AUTHORIZATION_TYPE_INDEX = 0;
    private static final int CREDENTIAL_TYPE_INDEX = 1;

    public static HeaderCredential from(String authorizationHeader) {
        String[] typeAndCredential = authorizationHeader.split(DELIMITER);
        return new HeaderCredential(typeAndCredential[AUTHORIZATION_TYPE_INDEX], typeAndCredential[CREDENTIAL_TYPE_INDEX]);
    }

    public String toAuthorizationHeader() {
        return type() + DELIMITER + value();
    }
}
