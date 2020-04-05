package twisk.outils;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.Runtime;
import java.util.stream.Collectors;

public class KitC {
    public KitC() { }

    /**
     * copie un fichier de src vers dest
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copier(InputStream source, File dest) throws IOException {
        InputStream sourceFile = source;
        OutputStream destinationFile = new FileOutputStream(dest);

        //Lecture par segment de 0.5Mo
        byte buffer[] = new byte[512*1024];
        int nbLecture;
        while ((nbLecture = sourceFile.read(buffer)) != -1){
            destinationFile.write(buffer, 0, nbLecture);
        }
        destinationFile.close();
        sourceFile.close();
    }

    /**
     * cree l'environnement
     */
    public void creerEnvironnement() {
        try {
            // création du répertoire twisk sous /tmp. Ne déclenche pas d’erreur si le répertoire existe déjà
            Path directories = Files.createDirectories(Paths.get("/tmp/twisk"));
            // copie des deux fichiers programmeC.o et def.h depuis le projet sous /tmp/twisk
            String[] liste = {"programmeC.o", "def.h", "codeNatif.o"};
            for (String nom : liste) {
                /*Path source = Paths.get(getClass().getResource("/codeC/" + nom).getPath());
                Path newdir = Paths.get("/tmp/twisk/");
                Files.copy(source, newdir.resolve(source.getFileName()), REPLACE_EXISTING);*/
                InputStream source = getClass().getClassLoader().getResourceAsStream("codeC/"+nom);
                System.out.println(source.toString());
                File destination = new File("/tmp/twisk/"+nom);
                copier(source, destination);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * cree le fichierC
     * @param codeC
     */
    public void creerFichier(String codeC){
        FileWriter flot;
        String finDeLigne = System.getProperty("line.separator");
        try {
            flot=new FileWriter("/tmp/twisk/client.c");
            flot.write(codeC+finDeLigne);
            flot.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compile le fichier C
     */
    public void compiler() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process p=runtime.exec("gcc -Wall -fPIC -c /tmp/twisk/client.c -o /tmp/twisk/client.o -lm");
            BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String ligne ;
            while ((ligne = output.readLine()) != null) {
                System.out.println(ligne);
            }
            while ((ligne = error.readLine()) != null) {
                System.out.println(ligne);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Construit la librairie
     */
    public void construireLaLibrairie() {
        String commande = "gcc -shared /tmp/twisk/programmeC.o /tmp/twisk/codeNatif.o /tmp/twisk/client.o -o /tmp/twisk/libTwisk.so";
        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(commande);
            BufferedReader output = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String ligne ;
            ligne = output.readLine();
            while (ligne != null){System.out.println(ligne);}
            ligne = error.readLine();
            while (ligne != null){System.out.println(ligne);}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
