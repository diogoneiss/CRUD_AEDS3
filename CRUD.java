import java.io.File;
import java.io.*;

public class CRUD {

    public final String diretorio = "dados";

    public RandomAccessFile arquivo;
    public HashExtensivel índiceDireto;
    public ArvoreBMais_String_Int índiceIndireto;
  
    public CRUD(String nomeArquivo) throws Exception {

        File d = new File(this.diretorio);
        if( !d.exists() )
            d.mkdir();

        arquivo = new RandomAccessFile(this.diretorio+"/"+nomeArquivo+".db", "rw");
        if(arquivo.length()<4)
            arquivo.writeInt(0);  // cabeçalho do arquivo

        índiceDireto = new HashExtensivel( 10, 
                           "diretorio."+nomeArquivo+".idx", 
                           "cestos."+nomeArquivo+".idx");

        índiceIndireto = new ArvoreBMais_String_Int( 10,
                           this.diretorio+"/arvoreB."+nomeArquivo+".idx");
    }

    /**
     * 
     * @param nome
     * @param email
     * @param senha
     * @return id do usuario criado no db
     * @throws Exception
     */
    public int create(String nome, String email, String senha) throws Exception {

        arquivo.seek(0);
        int id = arquivo.readInt() + 1;// ler o ultimo id usado e somar um para obter o novo id

        Usuario temp = new Usuario(id, nome, email, senha);
        long endereco = arquivo.length();

        arquivo.seek(arquivo.length());// aponta o ponteiro para o fim do arquivo

        arquivo.writeChar(' ');// escreve a lapide
        arquivo.writeInt(temp.toByteArray().length);//escreve o tamanho
        arquivo.write(temp.toByteArray());// escreve o registro
        arquivo.seek(0);
        arquivo.writeInt(id);// atualiza o id
        índiceDireto.create(id, endereco);
        índiceIndireto.create(email,id);
        return id;
    }
    public Usuario read(int id) throws Exception{
        long endereco=índiceDireto.read(id);
        arquivo.seek(endereco);
        char lapide=arquivo.readChar();
        if(lapide!=' ') throw new Exception("Erro! arquivo deletado");
        int tamanho=arquivo.readInt();
        byte[] byteUsuario=new byte[tamanho];
        arquivo.read(byteUsuario);
        Usuario usuario=new Usuario();
        usuario.fromByteArray(byteUsuario);    
        return usuario;     
    }
    public Usuario read(String email) throws Exception{
        int id=índiceIndireto.read(email);
        Usuario usuario=this.read(id);
        return usuario;
    }
    public boolean update(Usuario novoUsuario) throws Exception{
        long endereco=índiceDireto.read(novoUsuario.getId());
        arquivo.seek(endereco);
        char lapide =arquivo.readChar();
        if(lapide!=' ') return false;
        int tamanho=arquivo.readInt();
        byte[] byteUsuarioNovo=novoUsuario.toByteArray();
        if(byteUsuarioNovo.length==tamanho){
            arquivo.write(byteUsuarioNovo);//substitui os antigos dados pelos novos
        }
        else{
            long novoEndereco=arquivo.length();
            arquivo.seek(novoEndereco);
            arquivo.writeChar(' ');// escreve a lapide
            arquivo.writeInt(byteUsuarioNovo.length);
            arquivo.write(byteUsuarioNovo);// escreve o registro
            //atualizar os indices
            índiceDireto.update(novoUsuario.getId(), novoEndereco);
            índiceIndireto.update(novoUsuario.getEmail(), novoUsuario.getId());
            //marcar para exclusão o registro antigo
            arquivo.seek(endereco);
            arquivo.writeChar('*');
        }
        return true;
    }
    public void delete(int id) throws Exception{
        long endereco=índiceDireto.read(id);
        arquivo.seek(endereco);
        arquivo.writeChar('*');
        int tamanho=arquivo.readInt();
        byte[] byteUsuario=new byte[tamanho];
        arquivo.read(byteUsuario);
        Usuario usuario=new Usuario();
        usuario.fromByteArray(byteUsuario);
        índiceIndireto.delete(usuario.chaveSecundaria());
        índiceDireto.delete(id);
    }
    
}
