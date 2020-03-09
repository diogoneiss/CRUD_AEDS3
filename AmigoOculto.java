public class AmigoOculto {
   public static void main(String args[]) {
      System.out.println("Teste");
      try {
          CRUD amigoOculto = new CRUD("amigos");  
          amigoOculto.create("diogo", "diogo@", "1234");

      } catch (Exception e) {
          //TODO: handle exception
      }
  }
}