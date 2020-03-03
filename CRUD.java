import java.io.File;
import java.io.RandomAccessFile;

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
                           this.diretorio+"/diretorio."+nomeArquivo+".idx", 
                           this.diretorio+"/cestos."+nomeArquivo+".idx");

        índiceIndireto = new ArvoreBMais_String_Int( 10,
                           this.diretorio+"/arvoreB."+nomeArquivo+".idx");
    }
}