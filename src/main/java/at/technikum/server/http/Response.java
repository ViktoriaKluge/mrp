package at.technikum.server.http;

public class Response {

    private ContentType contentType;
    private String body;
    private Status status;
    private String token;

    public Response() {

    }

   public Response(ContentType contentType, String body, Status status) {
        this.contentType = contentType;
        this.body = body;
        this.status = status;
   }

   public void setStatus(Status status) {
        this.status = status;
   }

   public int getStatusCode() {
        return status.getCode();
   }

   public String getStatusMessage() {
        return status.getMessage();
   }

   public String getContentType() {
        return contentType.getMimeType();
   }

   public void setContentType(ContentType contentType) {
        this.contentType = contentType;
   }

   public String getBody() {
        return body;
   }

   public void setBody(String body) {
        this.body = body;
   }

    public Status getStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
