<?php
// ocr_reader.php — PHP версия

require_once 'vendor/autoload.php';

use thiagoalessio\TesseractOCR\TesseractOCR;

$imagePath = null;
$lang = 'eng';
$outputFile = null;

$args = array_slice($argv, 1);
for ($i = 0; $i < count($args); $i++) {
    if ($args[$i] == '-l' || $args[$i] == '--lang') {
        $lang = $args[++$i];
    } elseif ($args[$i] == '-o' || $args[$i] == '--output') {
        $outputFile = $args[++$i];
    } elseif (!str_starts_with($args[$i], '-')) {
        $imagePath = $args[$i];
    }
}

if (!$imagePath) {
    echo "Usage: php ocr_reader.php <image> [-l lang] [-o output]\n";
    exit(1);
}

if (!file_exists($imagePath)) {
    echo "\033[31m❌ Файл не найден: $imagePath\033[0m\n";
    exit(1);
}

echo "\033[36m📖 OCR Reader (PHP)\033[0m\n";
echo "📂 Обработка: $imagePath\n";
echo "⏳ Распознавание...\n";

$start = microtime(true);
try {
    $ocr = new TesseractOCR($imagePath);
    $ocr->lang($lang);
    $text = trim((string) $ocr);
    $elapsed = microtime(true) - $start;

    echo "\n\033[32mРезультат:\033[0m\n";
    echo str_repeat("─", 50) . "\n";
    echo $text . "\n";
    echo str_repeat("─", 50) . "\n";

    $words = count(array_filter(explode(' ', $text)));
    $chars = strlen($text);
    echo "\033[36m📊 Статистика:\033[0m\n";
    echo "  Слов: $words\n";
    echo "  Символов: $chars\n";
    echo "  Время: " . number_format($elapsed, 2) . " сек\n";

    $outFile = $outputFile ?: pathinfo($imagePath, PATHINFO_FILENAME) . ".txt";
    file_put_contents($outFile, $text);
    echo "\033[32m💾 Сохранено в: $outFile\033[0m\n";
} catch (Exception $e) {
    echo "\033[31m❌ Ошибка: " . $e->getMessage() . "\033[0m\n";
    exit(1);
}
?>
