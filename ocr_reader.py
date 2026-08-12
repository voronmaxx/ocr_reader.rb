

### 1. `ocr_reader.py` (Python)

```python
# ocr_reader.py — Python версия

import sys
import os
import time
import argparse
from PIL import Image
import pytesseract
from colorama import init, Fore, Style

init(autoreset=True)

def ocr_image(image_path, lang='eng'):
    """Распознаёт текст с изображения с помощью Tesseract."""
    try:
        image = Image.open(image_path)
        text = pytesseract.image_to_string(image, lang=lang)
        return text.strip()
    except Exception as e:
        return f"❌ Ошибка: {e}"

def save_result(text, output_path):
    """Сохраняет результат в файл."""
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(text)
    print(f"{Fore.GREEN}💾 Сохранено в: {output_path}")

def main():
    parser = argparse.ArgumentParser(description='OCR Reader')
    parser.add_argument('image', help='Путь к изображению')
    parser.add_argument('-l', '--lang', default='eng', help='Язык (по умолчанию eng)')
    parser.add_argument('-o', '--output', help='Файл для сохранения результата')
    args = parser.parse_args()

    if not os.path.exists(args.image):
        print(f"{Fore.RED}❌ Файл не найден: {args.image}")
        sys.exit(1)

    print(f"{Fore.CYAN}📖 OCR Reader (Python)")
    print(f"📂 Обработка: {args.image}")
    print("⏳ Распознавание...")

    start = time.time()
    text = ocr_image(args.image, args.lang)
    elapsed = time.time() - start

    if text.startswith("❌"):
        print(text)
        sys.exit(1)

    print(f"\n{Fore.GREEN}Результат:{Style.RESET_ALL}")
    print("─" * 50)
    print(text)
    print("─" * 50)

    words = len(text.split())
    chars = len(text)
    print(f"{Fore.CYAN}📊 Статистика:{Style.RESET_ALL}")
    print(f"  Слов: {words}")
    print(f"  Символов: {chars}")
    print(f"  Время: {elapsed:.2f} сек")

    if args.output:
        save_result(text, args.output)
    else:
        default_output = os.path.splitext(args.image)[0] + ".txt"
        save_result(text, default_output)

if __name__ == "__main__":
    main()
