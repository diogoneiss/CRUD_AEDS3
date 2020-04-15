import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class CRUD<T extends Entidade> {

    public final String diretorio = "dados";

    public RandomAccessFile arquivo;
    public HashExtensivel índiceDireto;
    public ArvoreBMais_String_Int índiceIndireto;

    //passar o construtor da classe genérica que será utilizada
    Constructor<T> construtor;

    public CRUD(String nomeArquivo, Constructor<T> construtor) throws Exception {
        this.construtor=construtor;
        File d = new File(this.diretorio);
        if (!d.exists())
            d.mkdir();

        arquivo = new RandomAccessFile(this.diretorio + "/" + nomeArquivo + ".db", "rw");
        if (arquivo.length() < 4)
            arquivo.writeInt(0);  // cabeçalho do arquivo

        índiceDireto = new HashExtensivel(10,

                this.diretorio + "/" + "diretorio." + nomeArquivo + ".idx",
                this.diretorio + "/" + "cestos." + nomeArquivo + ".idx");

        índiceIndireto = new ArvoreBMais_String_Int(10,
                this.diretorio + "/" + "arvoreB." + nomeArquivo + ".idx");
    }

    /**
     * @return id do usuario criado no db
     * @throws Exception
     */
    public int create(T objetoCriado) throws Exception {

        arquivo.seek(0);
        int id = arquivo.readInt() + 1;// ler o ultimo id usado e somar um para obter o novo id

        long endereco = arquivo.length();
        objetoCriado.setId(id);

        arquivo.seek(arquivo.length());// aponta o ponteiro para o fim do arquivo

        arquivo.writeChar(' ');// escreve a lapide
        arquivo.writeInt(objetoCriado.toByteArray().length);//escreve o tamanho
        arquivo.write(objetoCriado.toByteArray());// escreve o registro
        arquivo.seek(0);
        arquivo.writeInt(id);// atualiza o id
        índiceDireto.create(id, endereco);
        índiceIndireto.create(objetoCriado.chaveSecundaria(), objetoCriado.getId());
        return id;
    }

    // O problema está neste método.
    public T read(int id) throws Exception {
        long endereco = índiceDireto.read(id);

        if (endereco < 0) return null;

        arquivo.seek(endereco);
        char lapide = arquivo.readChar();;
        if (lapide != ' ')
            throw new Exception("Erro! arquivo deletado");

        int tamanho = arquivo.readInt(); 
        byte[] byteUsuario = new byte[tamanho];
        arquivo.read(byteUsuario);
        Usuario a=new Usuario(byteUsuario);
        //como se fosse um new T()
        T objeto = this.construtor.newInstance();
        objeto.fromByteArray(byteUsuario);
        return objeto;
    }

    public T read(String chaveSec) throws Exception {
        //procurar o id com a string fornecida
        int id = índiceIndireto.read(chaveSec);
        return this.read(id);
    }

    public boolean update(T novoObjeto) throws Exception {
        long endereco = índiceDireto.read(novoObjeto.getId());
        arquivo.seek(endereco);
        char lapide = arquivo.readChar();
        if (lapide != ' ') return false;
        int tamanho = arquivo.readInt();
        byte[] byteUsuarioNovo = novoObjeto.toByteArray();
        if (byteUsuarioNovo.length == tamanho) {
            arquivo.write(byteUsuarioNovo);//substitui os antigos dados pelos novos
        } else {
            long novoEndereco = arquivo.length();
            arquivo.seek(novoEndereco);
            arquivo.writeChar(' ');// escreve a lapide
            arquivo.writeInt(byteUsuarioNovo.length);
            arquivo.write(byteUsuarioNovo);// escreve o registro
            //atualizar os indices
            índiceDireto.update(novoObjeto.getId(), novoEndereco);
            índiceIndireto.update(novoObjeto.chaveSecundaria(), novoObjeto.getId());
            //marcar para exclusão o registro antigo
            arquivo.seek(endereco);
            arquivo.writeChar('*');
        }
        return true;
    }

    public void delete(int id) throws Exception {
        long endereco = índiceDireto.read(id);
        arquivo.seek(endereco);
        arquivo.writeChar('*');
        int tamanho = arquivo.readInt();
        byte[] byteUsuario = new byte[tamanho];
        arquivo.read(byteUsuario);

        //criação de um new T
        T obj = this.construtor.newInstance();
        obj.fromByteArray(byteUsuario);
        índiceIndireto.delete(obj.chaveSecundaria());
        índiceDireto.delete(id);
    }

}