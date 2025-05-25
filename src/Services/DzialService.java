package Services;

import Model.Dzial;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import Exception.NotUniqueNameException;
import java.util.stream.Collectors;


public class DzialService {
    private static final Path FILE_PATH = Paths.get("src", "dane", "działy.txt");
    private static final String HEADER = "nazwa";
    private static final List<Dzial> dzialy = new ArrayList<>();

    static {
        try {
            // tworzenie katalogu, jeśli nie istnieje
            Path folder = FILE_PATH.getParent();
            if (folder != null && !Files.exists(folder)) {
                Files.createDirectories(folder);
            }
            // tworzenie pliku, jeśli nie ma
            if (!Files.exists(FILE_PATH)) {
                Files.write(FILE_PATH, List.of(HEADER), StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE);
            }
            // wczytanie zawartości pliku (pomijamy nagłówek)
            List<String> lines = Files.readAllLines(FILE_PATH, StandardCharsets.UTF_8);
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(";");
                // parts[0] = nazwa, w przyszłości można rozszerzyć o kolejne pola
                Dzial d = Dzial.createDzial(parts[0]);
                dzialy.add(d);
            }
        } catch (IOException | NotUniqueNameException e) {
            e.printStackTrace();
        }
    }

    public static void addDzial(Dzial dzial){
        dzialy.add(dzial);
        saveAll();
    }
    public static List<Dzial> getDzialy(){
        return dzialy;
    }
    public static List<String> getNazwyDzialow() {
        return dzialy.stream()
                .map(Dzial::getNazwa_dzialu)
                .collect(Collectors.toList());
    }
    public static void removeDzial(Dzial dzial){
        dzialy.remove(dzial);
        saveAll();
    }

    private static void saveAll(){
        List<String> lines = new ArrayList<>();
        lines.add(HEADER);
        for(Dzial dzial: dzialy){
            lines.add(dzial.getNazwa_dzialu());
        }
        try{
            Files.write(FILE_PATH, lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
