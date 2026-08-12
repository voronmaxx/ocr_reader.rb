// ocr_reader.java — Java версия

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ocr_reader {
    public static void main(String[] args) {
        String imagePath = null;
        String lang = "eng";
        String outputFile = null;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-l") || args[i].equals("--lang")) {
                lang = args[++i];
            } else if (args[i].equals("-o") || args[i].equals("--output")) {
                outputFile = args[++i];
            } else if (!args[i].startsWith("-")) {
                imagePath = args[i];
            }
        }

        if (imagePath == null) {
            System.out.println("Usage: java ocr_reader <image> [-l lang] [-o output]");
            System.exit(1);
        }

        File image = new File(imagePath);
        if (!image.exists()) {
            System.out.println("\u001B[31m❌ Файл не найден: " + imagePath + "\u001B[0m");
            System.exit(1);
        }

        System.out.println("\u001B[36m📖 OCR Reader (Java)\u001B[0m");
        System.out.println("📂 Обработка: " + imagePath);
        System.out.println("⏳ Распознавание...");

        long start = System.currentTimeMillis();
        Tesseract tesseract = new Tesseract();
        tesseract.setLanguage(lang);

        try {
            String text = tesseract.doOCR(image);
            text = text.trim();
            long elapsed = (System.currentTimeMillis() - start) / 1000;

            System.out.println("\n\u001B[32mРезультат:\u001B[0m");
            System.out.println("─".repeat(50));
            System.out.println(text);
            System.out.println("─".repeat(50));

            String[] words = text.split("\\s+");
            int wordCount = text.isEmpty() ? 0 : words.length;
            int charCount = text.length();
            System.out.println("\u001B[36m📊 Статистика:\u001B[0m");
            System.out.println("  Слов: " + wordCount);
            System.out.println("  Символов: " + charCount);
            System.out.println("  Время: " + elapsed + " сек");

            String outFile = outputFile != null ? outputFile : imagePath.replaceFirst("\\.[^.]+$", "") + ".txt";
            try (FileWriter fw = new FileWriter(outFile)) {
                fw.write(text);
            }
            System.out.println("\u001B[32m💾 Сохранено в: " + outFile + "\u001B[0m");
        } catch (TesseractException e) {
            System.out.println("\u001B[31m❌ Ошибка: " + e.getMessage() + "\u001B[0m");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("\u001B[31m❌ Ошибка сохранения: " + e.getMessage() + "\u001B[0m");
        }
    }
}
