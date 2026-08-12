// ocr_reader.rs — Rust версия

use std::env;
use std::fs;
use std::time::Instant;
use rusty_tesseract::{Image, Args};

fn main() -> Result<(), Box<dyn std::error::Error>> {
    let args: Vec<String> = env::args().collect();
    let mut image_path = None;
    let mut lang = "eng".to_string();
    let mut output_file = None;

    let mut i = 1;
    while i < args.len() {
        match args[i].as_str() {
            "-l" | "--lang" => {
                lang = args[i+1].clone();
                i += 2;
            }
            "-o" | "--output" => {
                output_file = Some(args[i+1].clone());
                i += 2;
            }
            _ => {
                if !args[i].starts_with("-") {
                    image_path = Some(args[i].clone());
                }
                i += 1;
            }
        }
    }

    let image_path = image_path.ok_or("Usage: cargo run -- <image> [-l lang] [-o output]")?;

    println!("\x1b[36m📖 OCR Reader (Rust)\x1b[0m");
    println!("📂 Обработка: {}", image_path);
    println!("⏳ Распознавание...");

    let start = Instant::now();
    let img = Image::from_path(&image_path)?;
    let args = Args::default().lang(&lang);
    let text = rusty_tesseract::image_to_string(&img, &args)?;
    let text = text.trim();
    let elapsed = start.elapsed().as_secs_f64();

    println!("\n\x1b[32mРезультат:\x1b[0m");
    println!("{}", "─".repeat(50));
    println!("{}", text);
    println!("{}", "─".repeat(50));

    let words: Vec<&str> = text.split_whitespace().collect();
    println!("\x1b[36m📊 Статистика:\x1b[0m");
    println!("  Слов: {}", words.len());
    println!("  Символов: {}", text.len());
    println!("  Время: {:.2} сек", elapsed);

    let out_file = output_file.unwrap_or_else(|| {
        let path = std::path::Path::new(&image_path);
        path.file_stem().unwrap().to_str().unwrap().to_string() + ".txt"
    });
    fs::write(&out_file, text)?;
    println!("\x1b[32m💾 Сохранено в: {}\x1b[0m", out_file);

    Ok(())
}
