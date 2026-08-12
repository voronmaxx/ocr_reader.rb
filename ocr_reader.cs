// ocr_reader.cs — C# версия

using System;
using System.IO;
using System.Linq;
using Tesseract;

class OCRReader {
    static void Main(string[] args) {
        string imagePath = null;
        string lang = "eng";
        string outputFile = null;

        for (int i = 0; i < args.Length; i++) {
            if (args[i] == "-l" || args[i] == "--lang") {
                lang = args[++i];
            } else if (args[i] == "-o" || args[i] == "--output") {
                outputFile = args[++i];
            } else if (!args[i].StartsWith("-")) {
                imagePath = args[i];
            }
        }

        if (imagePath == null) {
            Console.WriteLine("Usage: dotnet run <image> [-l lang] [-o output]");
            return;
        }

        if (!File.Exists(imagePath)) {
            Console.WriteLine($"\u001B[31m❌ Файл не найден: {imagePath}\u001B[0m");
            return;
        }

        Console.WriteLine("\u001B[36m📖 OCR Reader (C#)\u001B[0m");
        Console.WriteLine($"📂 Обработка: {imagePath}");
        Console.WriteLine("⏳ Распознавание...");

        var start = DateTime.Now;
        try {
            using (var engine = new TesseractEngine("./tessdata", lang, EngineMode.Default)) {
                using (var img = Pix.LoadFromFile(imagePath)) {
                    using (var page = engine.Process(img)) {
                        string text = page.GetText().Trim();
                        var elapsed = (DateTime.Now - start).TotalSeconds;

                        Console.WriteLine("\n\u001B[32mРезультат:\u001B[0m");
                        Console.WriteLine(new string('─', 50));
                        Console.WriteLine(text);
                        Console.WriteLine(new string('─', 50));

                        var words = text.Split(new[] { ' ', '\n', '\r' }, StringSplitOptions.RemoveEmptyEntries);
                        Console.WriteLine("\u001B[36m📊 Статистика:\u001B[0m");
                        Console.WriteLine($"  Слов: {words.Length}");
                        Console.WriteLine($"  Символов: {text.Length}");
                        Console.WriteLine($"  Время: {elapsed:F2} сек");

                        string outFile = outputFile ?? Path.GetFileNameWithoutExtension(imagePath) + ".txt";
                        File.WriteAllText(outFile, text);
                        Console.WriteLine($"\u001B[32m💾 Сохранено в: {outFile}\u001B[0m");
                    }
                }
            }
        } catch (Exception e) {
            Console.WriteLine($"\u001B[31m❌ Ошибка: {e.Message}\u001B[0m");
        }
    }
}
