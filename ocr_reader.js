// ocr_reader.js — JavaScript версия

const Tesseract = require('tesseract.js');
const fs = require('fs');
const path = require('path');

async function main() {
    const args = process.argv.slice(2);
    let imagePath = null;
    let lang = 'eng';
    let outputFile = null;

    for (let i = 0; i < args.length; i++) {
        if (args[i] === '-l' || args[i] === '--lang') {
            lang = args[++i];
        } else if (args[i] === '-o' || args[i] === '--output') {
            outputFile = args[++i];
        } else if (!args[i].startsWith('-')) {
            imagePath = args[i];
        }
    }

    if (!imagePath) {
        console.log('Usage: node ocr_reader.js <image> [-l lang] [-o output]');
        process.exit(1);
    }

    if (!fs.existsSync(imagePath)) {
        console.error(`\x1b[31m❌ Файл не найден: ${imagePath}\x1b[0m`);
        process.exit(1);
    }

    console.log('\x1b[36m📖 OCR Reader (JavaScript)\x1b[0m');
    console.log(`📂 Обработка: ${imagePath}`);
    console.log('⏳ Распознавание...');

    const start = Date.now();
    try {
        const result = await Tesseract.recognize(imagePath, lang);
        const text = result.data.text.trim();
        const elapsed = (Date.now() - start) / 1000;

        console.log(`\n\x1b[32mРезультат:\x1b[0m`);
        console.log('─'.repeat(50));
        console.log(text);
        console.log('─'.repeat(50));

        const words = text.split(/\s+/).filter(w => w).length;
        const chars = text.length;
        console.log(`\x1b[36m📊 Статистика:\x1b[0m`);
        console.log(`  Слов: ${words}`);
        console.log(`  Символов: ${chars}`);
        console.log(`  Время: ${elapsed.toFixed(2)} сек`);

        const outFile = outputFile || path.basename(imagePath, path.extname(imagePath)) + '.txt';
        fs.writeFileSync(outFile, text);
        console.log(`\x1b[32m💾 Сохранено в: ${outFile}\x1b[0m`);
    } catch (err) {
        console.error(`\x1b[31m❌ Ошибка: ${err.message}\x1b[0m`);
        process.exit(1);
    }
}

main().catch(console.error);
