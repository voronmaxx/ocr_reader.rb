// ocr_reader.go — Go версия

package main

import (
	"flag"
	"fmt"
	"os"
	"strings"
	"time"

	"github.com/otiai10/gosseract/v2"
)

func main() {
	lang := flag.String("l", "eng", "Язык OCR (по умолчанию eng)")
	output := flag.String("o", "", "Файл для сохранения результата")
	flag.Parse()

	if flag.NArg() < 1 {
		fmt.Println("Usage: go run ocr_reader.go <image> [-l lang] [-o output]")
		os.Exit(1)
	}
	imagePath := flag.Arg(0)

	fmt.Println("\x1b[36m📖 OCR Reader (Go)\x1b[0m")
	fmt.Printf("📂 Обработка: %s\n", imagePath)
	fmt.Println("⏳ Распознавание...")

	start := time.Now()
	client := gosseract.NewClient()
	defer client.Close()
	client.SetLanguage(*lang)
	client.SetImage(imagePath)
	text, err := client.Text()
	elapsed := time.Since(start).Seconds()

	if err != nil {
		fmt.Printf("\x1b[31m❌ Ошибка: %v\x1b[0m\n", err)
		os.Exit(1)
	}

	text = strings.TrimSpace(text)
	fmt.Printf("\n\x1b[32mРезультат:\x1b[0m\n")
	fmt.Println(strings.Repeat("─", 50))
	fmt.Println(text)
	fmt.Println(strings.Repeat("─", 50))

	words := len(strings.Fields(text))
	chars := len(text)
	fmt.Printf("\x1b[36m📊 Статистика:\x1b[0m\n")
	fmt.Printf("  Слов: %d\n", words)
	fmt.Printf("  Символов: %d\n", chars)
	fmt.Printf("  Время: %.2f сек\n", elapsed)

	if *output != "" {
		os.WriteFile(*output, []byte(text), 0644)
		fmt.Printf("\x1b[32m💾 Сохранено в: %s\x1b[0m\n", *output)
	} else {
		outFile := imagePath[:len(imagePath)-len(extension(imagePath))] + ".txt"
		os.WriteFile(outFile, []byte(text), 0644)
		fmt.Printf("\x1b[32m💾 Сохранено в: %s\x1b[0m\n", outFile)
	}
}

func extension(path string) string {
	for i := len(path) - 1; i >= 0 && path[i] != '.'; i-- {
		if path[i] == '/' || path[i] == '\\' {
			return ""
		}
	}
	if i := strings.LastIndex(path, "."); i != -1 {
		return path[i:]
	}
	return ""
}
