public class AmigoOculto {
    public static void main(String args[]) {
        System.out.println("Teste");
        try {
            CRUD amigoOculto = new CRUD("amigos");
            amigoOculto.create("diogo", "diogo@gmail.com", "1234");
            Usuario a = amigoOculto.read(1);
            System.out.println(" 11 " + a.getNome());
            a = amigoOculto.read("diogo@gmail.com");
            System.out.println(" 222 " + a.getEmail());
            Usuario b = new Usuario(1, "henrique", "rique@bbb.com", "123334");
            amigoOculto.update(b);
            a = amigoOculto.read(1);
            System.out.println(" aaa " + a.getEmail() + " " + a.getNome());
            amigoOculto.delete(1);
            a = amigoOculto.read(1);
        } catch (Exception e) {
            System.out.println("erro");
            e.printStackTrace();
        }
    }
}