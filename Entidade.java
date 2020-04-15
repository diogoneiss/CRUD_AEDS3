import java.io.IOException;

public interface Entidade {
   byte[] toByteArray() throws IOException;

   void fromByteArray(byte[] bytes) throws IOException;

   int getId();

   void setId(int id);

   String chaveSecundaria();

}
