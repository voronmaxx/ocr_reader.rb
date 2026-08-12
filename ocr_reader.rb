# ocr_reader.rb — Ruby версия

require 'rtesseract'
require 'optparse'
require 'time'

options = {}
OptionParser.new do |opts|
  opts.banner = "Usage: ruby ocr_reader.rb <image> [-l lang] [-o output]"
  opts.on("-l", "--lang LANG", "Язык OCR (по умолчанию eng)") { |l| options[:lang] = l }
  opts.on("-o", "--output FILE", "Файл для сохранения результата") { |o| options[:output] = o }
end.parse!

image_path = ARGV[0]
unless image_path && File.exist?(image_path)
  puts "❌ Файл не найден: #{image_path}"
  exit 1
end

lang = options[:lang] || 'eng'

puts "\e[36m📖 OCR Reader (Ruby)\e[0m"
puts "📂 Обработка: #{image_path}"
puts "⏳ Распознавание..."

start = Time.now
begin
  text = RTesseract.new(image_path, lang: lang).to_s.strip
  elapsed = Time.now - start

  puts "\n\e[32mРезультат:\e[0m"
  puts "─" * 50
  puts text
  puts "─" * 50

  words = text.split(/\s+/).reject(&:empty?).size
  chars = text.size
  puts "\e[36m📊 Статистика:\e[0m"
  puts "  Слов: #{words}"
  puts "  Символов: #{chars}"
  puts "  Время: #{elapsed.round(2)} сек"

  output_file = options[:output] || File.basename(image_path, ".*") + ".txt"
  File.write(output_file, text)
  puts "\e[32m💾 Сохранено в: #{output_file}\e[0m"
rescue => e
  puts "\e[31m❌ Ошибка: #{e.message}\e[0m"
  exit 1
end
