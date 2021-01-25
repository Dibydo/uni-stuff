(define (read-words)
  (define (subf words word var)
    (cond
      ((and (eof-object? var) (not (null? words))) (reverse words))
      ((and (eof-object? var) (not (null? word))) (subf (cons (list->string (reverse word)) words) '() (read-char)))
      ((eof-object? var) (reverse words))
      ((and (or (equal? var #\newline) (equal? var #\tab) (equal? var #\space)) (null? word)) (subf words word (read-char)))
      ((or (equal? var #\newline) (equal? var #\tab) (equal? var #\space)) (subf (cons (list->string (reverse word)) words) '() (read-char)))
      (else (subf words (cons var word) (read-char)))))
  (subf '() '() (read-char)))

(read-words)