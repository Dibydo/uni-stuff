(define (save-data data filename)
  (with-output-to-file filename
    (lambda ()
      (write data (current-output-port))
      (newline (current-output-port)))))

(define (load-data filename)
  (with-input-from-file filename
    (lambda ()
      (let ((var (read)))
        (write var)
        (newline)))))

(define (read-string input-port)
  (let ((line (read-char input-port)))
    (cond
      ((eof-object? line) line); проверка текста на конец-файла
      ((eq? line #\newline) '()); 
      (else (cons line (read-string input-port))))))

(define (line-count filename)
  (let ((var (open-input-file filename)))
    (define (count number_string)
      (let ((expr (read-string var)))
        (if (eof-object? expr)
            (begin 
              (close-input-port var) 
              number_string)
            (if (not (null? expr))
                (count (+ number_string 1))
                (count number_string)))))
    (count 0)))