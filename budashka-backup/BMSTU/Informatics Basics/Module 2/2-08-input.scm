(define (read-words)
  (define (f words word c)
      (cond ((and (eof-object? c) (not (null? words))) (reverse words))
            ((and (eof-object? c) (not (null? word))) (f (cons (list->string (reverse word)) words) '() (read-char)))
            ((eof-object? c) (reverse words))
            ((and (or (equal? c #\newline) (equal? c #\tab) (equal? c #\space)) (null? word)) (f words word (read-char)))
            ((or (equal? c #\newline) (equal? c #\tab) (equal? c #\space)) (f (cons (list->string (reverse word)) words) '() (read-char)))
            (else (f words (cons c word) (read-char)))))
  (f '() '() (read-char)))