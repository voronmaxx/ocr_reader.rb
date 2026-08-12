## 📖 OCR Reader — извлеки текст из любого изображения за секунду

> «Картинка стоит тысячи слов, но мы прочитаем их все»

**OCR Reader** — это набор консольных утилит для распознавания текста на изображениях с помощью движка Tesseract OCR.  
Программа принимает на вход изображение (JPG, PNG, BMP, TIFF) и возвращает распознанный текст на английском языке.

## 🚀 Особенности
- 🔍 Распознавание текста с изображений (JPG, PNG, BMP, TIFF, PDF).
- 🇬🇧 Оптимизировано для английского языка (можно расширить на другие языки).
- 📄 Поддержка нескольких страниц (для PDF — где реализовано).
- 🎨 Цветной вывод в терминале.
- 💾 Сохранение результата в текстовый файл.
- 📊 Вывод статистики: количество слов, символов, время обработки.
- 🖼️ Автоматическое определение ориентации текста (где поддерживается).

## 🛠️ Установка и запуск

Для каждого языка — минимальные зависимости (в основном стандартные библиотеки + Tesseract).

| Язык       | Библиотека/пакет                         | Команда запуска                         |
|------------|------------------------------------------|-----------------------------------------|
| Python     | `pytesseract`, `Pillow`                  | `python ocr_reader.py image.png`        |
| Go         | `gosseract` или `gotesseract`            | `go run ocr_reader.go image.png`        |
| JavaScript | `tesseract.js` (Node.js)                 | `node ocr_reader.js image.png`          |
| Java       | `Tess4J`                                 | `javac -cp .:tess4j.jar ... && java ...` |
| C#         | `Tesseract.NET`                          | `dotnet run image.png`                  |
| Rust       | `tesseract-rs` или `rusty-tesseract`     | `cargo run -- image.png`                |
| Ruby       | `rtesseract`                             | `ruby ocr_reader.rb image.png`          |
| PHP        | `thiagoalessio/tesseract_ocr`            | `php ocr_reader.php image.png`          |

> Для работы всех скриптов требуется предварительно установленный Tesseract OCR на системе.
> На Ubuntu/Debian: `sudo apt install tesseract-ocr`
> На macOS: `brew install tesseract`
> На Windows: скачайте установщик с [GitHub](https://github.com/tesseract-ocr/tesseract/releases)

## 📖 Пример использования

```bash
$ python ocr_reader.py screenshot.png
Вывод:

text
📖 OCR Reader (Python)
📂 Обработка: screenshot.png
⏳ Распознавание...

Результат:
─────────────────────────────────────────
The quick brown fox jumps over the lazy dog.
This is a sample text for OCR testing.

─────────────────────────────────────────
📊 Статистика:
  Слов: 16
  Символов: 72
  Время: 1.23 сек
💾 Сохранено в: screenshot.txt
🤝 Вклад
Принимаются улучшения, новые языки, фичи.

📜 Лицензия
MIT — используйте свободно.

Автор: Ваш покорный слуга
