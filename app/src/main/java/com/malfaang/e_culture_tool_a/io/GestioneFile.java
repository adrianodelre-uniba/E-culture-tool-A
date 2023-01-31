package com.malfaang.e_culture_tool_a.io;

import android.os.Build;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 *
 *  @author adrianodelre
 */
public class GestioneFile {
    private static final Pattern SLASH = Pattern.compile(" / ");
    private static final Pattern SLASH2 = Pattern.compile("/");

    public GestioneFile(){ /* TODO document why this constructor is empty */ }

    public boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /*
    public void creaFile() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Inserisci il nome del file: ");
        String nomeFile = null;
        try {
            nomeFile = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = "E-culture_tool_A/";
        String pathname = path + nomeFile;
        setFile(new File(pathname));
    }
    */

    public void creaFile(String nomeFile) throws IOException {
        String path = "E-culture_tool_A" + SLASH2;
        String pathname = path + nomeFile;
        File file = cercaFile(pathname);
        if (file.exists()) {
            System.out.println("Il file e' gia' presente in memoria");
        } else {
            file.mkdirs();
        }
    }

    public final String leggiDaFile(String nomeFile) {
        String testo;
        StringBuilder testoFinal = null;
        StringBuilder testo2 = new StringBuilder();
        try(BufferedReader in = new BufferedReader(new FileReader(nomeFile))) {
            while((testo = in.readLine()) != null) {
                testoFinal = testo2.append(testo).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(testoFinal).toString();
    }

    public final void scriviSuFile(String nomeFile, String testo) {
        String testoOutput;
        BufferedReader in = new BufferedReader(new StringReader(testo));
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(nomeFile)))) {
            while((testoOutput = in.readLine()) != null) {
                out.println(testoOutput);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void eliminaRigaDaFile(String nomeFile, String parolaPerRiconoscimentoRiga) {
        String testo = leggiDaFile(nomeFile);
        BufferedReader in = new BufferedReader(new StringReader(testo));
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(nomeFile, false)))) {
            String testo2;
            while((testo2 = in.readLine()) != null) {
                String[] dati = SLASH.split(testo2);
                for (String s : dati) {
                    if (s.equals(parolaPerRiconoscimentoRiga)) {
                        continue;
                    } else {
                        out.println(testo2);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void eliminaFile(String nomeFile) throws IOException {
        String path = "E-culture_tool_A" + SLASH2;
        String pathname = path + nomeFile;
        File file = cercaFile(pathname);
        if (file.exists()) {
            file.delete();
        }else{
            System.out.println("File non trovato, eliminazione non riuscita");
        }
    }

    public static File cercaPerNome(Path path, String nome) throws IOException {
        List<Path> result = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try(Stream<Path> pathStream = Files.find(path, Integer.MAX_VALUE,
                    (p, basicFileAttributes) ->{
                        if(Files.isDirectory(p) || !Files.isReadable(p)){
                            return false;
                        }
                        return p.getFileName().toString().equalsIgnoreCase(nome);
                    })
            ){
                result = pathStream.collect(Collectors.toList());
                result.forEach(System.out::println);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(result.stream().findFirst().isPresent()){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    return result.stream().findFirst().get().toFile();
                }
            }else{
                return null;
            }
        }
        return null;
    }

    public File cercaFile(String nomeFile) throws IOException {
        String path = "E-culture_tool_A" + SLASH2;
        String pathname = path + nomeFile;
        Path path2 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            path2 = Path.class.cast(path);
        }
        File file = cercaPerNome(path2, nomeFile);
        if (file != null && file.exists()) {
            return file;
        }
        return null;
    }



    public final void serializzazione(String nomeFile, Object obj) {
        try (FileOutputStream outFile = new FileOutputStream(new File(nomeFile))) {
            try(ObjectOutput outStream = new ObjectOutputStream(outFile)){
                outStream.writeObject(obj);
            }catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final Object deSerializzazione(String nomeFile) throws IOException {
        Object risultato = null;
        File file = cercaFile(nomeFile);
        try(FileInputStream inFile = new FileInputStream(file)) {
            try(ObjectInput inStream = new ObjectInputStream(inFile)){
                risultato = inStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return risultato;
    }
}

